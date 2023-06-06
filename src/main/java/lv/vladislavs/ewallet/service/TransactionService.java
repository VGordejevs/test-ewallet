package lv.vladislavs.ewallet.service;

import jakarta.transaction.Transactional;
import lv.vladislavs.ewallet.converter.transaction.CreateTransactionRequestMapper;
import lv.vladislavs.ewallet.exception.transaction.amount.*;
import lv.vladislavs.ewallet.exception.transaction.prohibited.TransactionProhibitedException;
import lv.vladislavs.ewallet.exception.transaction.prohibited.TransactionProhibitedIncomingException;
import lv.vladislavs.ewallet.exception.transaction.prohibited.TransactionProhibitedOutgoingException;
import lv.vladislavs.ewallet.model.database.transaction.Transaction;
import lv.vladislavs.ewallet.model.database.transaction.TransactionStatus;
import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.database.wallet.Wallet;
import lv.vladislavs.ewallet.model.dto.transaction.CreateTransactionRequest;
import lv.vladislavs.ewallet.repository.TransactionRepository;
import lv.vladislavs.ewallet.repository.WalletRepository;
import lv.vladislavs.ewallet.util.BusinessDaysUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

import static lv.vladislavs.ewallet.Constants.*;
import static lv.vladislavs.ewallet.model.database.transaction.TransactionStatus.DECLINED;
import static lv.vladislavs.ewallet.model.database.transaction.TransactionType.WITHDRAW;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    WalletRepository walletRepository;

    @Transactional(dontRollbackOn={
            TransactionAmountException.class,
            TransactionProhibitedException.class})
    public void createTransaction(@NonNull CreateTransactionRequest createTransactionRequest, @NonNull User initiator) {
        Transaction transaction = CreateTransactionRequestMapper.INSTANCE
                .createTransactionRequestToTransaction(createTransactionRequest, walletRepository);
        transaction.setInitiator(initiator);

        try {
            checkTransactionAmounts(transaction);
            checkTransactionsForUsersAllowed(transaction);
        } catch (TransactionAmountException | TransactionProhibitedException e) {
            transaction.setStatus(DECLINED);
            transactionRepository.save(transaction);
            throw e;
        }

        updateTransactionWallets(transaction);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
    }

    private void checkTransactionAmounts(Transaction transaction) throws TransactionAmountException {
        Wallet sourceWallet = transaction.getSourceWallet();
        BigDecimal amount = transaction.getAmount();

        if (amount.compareTo(TRANSACTION_MIN_AMOUNT) <= 0) {
            throw new TransactionMinAmountException(TRANSACTION_MIN_AMOUNT, amount);
        }

        if (amount.compareTo(TRANSACTION_SUSPICIOUS_AMOUNT) > 0) {
            transaction.setSuspicious(true);
            throw new TransactionSuspiciousException(amount);
        }

        switch (transaction.getType()) {
            case WITHDRAW, TRANSFER -> {
                if (sourceWallet.getAmount().compareTo(amount) < 0) {
                    throw new TransactionNotEnoughFundsException(sourceWallet.getId(), amount);
                }
            }
        }

        if (amount.compareTo(TRANSACTION_MAX_AMOUNT) > 0) {
            throw new TransactionMaxAmountException(TRANSACTION_MAX_AMOUNT, amount);
        }
    }

    private void checkTransactionsForUsersAllowed(Transaction transaction) {
        User initiator = transaction.getInitiator();
        ZonedDateTime now = ZonedDateTime.now();

        switch (transaction.getType()) {
            case DEPOSIT -> {
                User targetWalletUser = transaction.getTargetWallet().getUser();
                ZonedDateTime targetProhibitIncoming = targetWalletUser.getProhibitIncomingTransactions();

                if (targetProhibitIncoming != null && now.isBefore(targetProhibitIncoming)) {
                    throw new TransactionProhibitedIncomingException(targetWalletUser.getId(), targetProhibitIncoming);
                }
            }
            case WITHDRAW -> {
                User sourceWalletUser = transaction.getSourceWallet().getUser();
                ZonedDateTime sourceProhibitOutgoing = sourceWalletUser.getProhibitOutgoingTransactions();

                if (sourceProhibitOutgoing != null && now.isBefore(sourceProhibitOutgoing)) {
                    throw new TransactionProhibitedOutgoingException(sourceWalletUser.getId(), sourceProhibitOutgoing);
                }

                List<Transaction> dayTransactions = transactionRepository.findByInitiatorIdAndCreationDateTimeBetween(initiator.getId(), now.with(LocalTime.MIN), now);
                BigDecimal dayWithdrawSum = dayTransactions.stream()
                        .filter(t -> WITHDRAW.equals(t.getType()))
                        .filter(t -> !DECLINED.equals(t.getStatus()))
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (dayWithdrawSum.add(transaction.getAmount()).compareTo(TRANSACTION_WITHDRAWAL_DAILY_LIMIT) > 0) {
                    throw new TransactionWithdrawalLimitException(TRANSACTION_WITHDRAWAL_DAILY_LIMIT);
                }
            }
            case TRANSFER -> {
                User sourceWalletUser = transaction.getSourceWallet().getUser();
                User targetWalletUser = transaction.getTargetWallet().getUser();
                ZonedDateTime sourceProhibitOutgoing = sourceWalletUser.getProhibitOutgoingTransactions();
                ZonedDateTime targetProhibitIncoming = targetWalletUser.getProhibitIncomingTransactions();

                if (sourceProhibitOutgoing != null && now.isBefore(sourceProhibitOutgoing)) {
                    throw new TransactionProhibitedOutgoingException(sourceWalletUser.getId(), sourceProhibitOutgoing);
                }
                if (targetProhibitIncoming != null && now.isBefore(targetProhibitIncoming)) {
                    throw new TransactionProhibitedIncomingException(sourceWalletUser.getId(), targetProhibitIncoming);
                }
            }
        }

        List<Transaction> pastHourTransactions = transactionRepository
                .findByInitiatorIdAndCreationDateTimeBetween(initiator.getId(), now.minusHours(1), now);
        if (pastHourTransactions.size() + 1 > TRANSACTION_SUSPICIOUS_HOURLY_COUNT) {
            initiator.setSuspicious(true);
        }
        if (pastHourTransactions.size() + 1 >= TRANSACTION_PROHIBIT_HOURLY_COUNT) {
            ZonedDateTime prohibitTransactionsUntil = BusinessDaysUtil.getNextBusinessDay().with(LocalTime.MIN);
            initiator.setProhibitOutgoingTransactions(prohibitTransactionsUntil);
            initiator.setProhibitIncomingTransactions(prohibitTransactionsUntil);
        }
    }

    private void updateTransactionWallets(Transaction transaction) {
        Wallet sourceWallet = transaction.getSourceWallet();
        Wallet targetWallet = transaction.getTargetWallet();

        switch (transaction.getType()) {
            case DEPOSIT -> targetWallet.setAmount(targetWallet.getAmount().add(transaction.getAmount()));
            case WITHDRAW -> sourceWallet.setAmount(sourceWallet.getAmount().subtract(transaction.getAmount()));
            case TRANSFER -> {
                sourceWallet.setAmount(sourceWallet.getAmount().subtract(transaction.getAmount()));
                targetWallet.setAmount(targetWallet.getAmount().add(transaction.getAmount()));
            }
        }
    }
}