package lv.vladislavs.ewallet.service;

import lv.vladislavs.ewallet.exception.transaction.amount.*;
import lv.vladislavs.ewallet.exception.transaction.prohibited.TransactionProhibitedIncomingException;
import lv.vladislavs.ewallet.model.database.transaction.Transaction;
import lv.vladislavs.ewallet.model.database.transaction.TransactionStatus;
import lv.vladislavs.ewallet.model.database.transaction.TransactionType;
import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.database.wallet.Wallet;
import lv.vladislavs.ewallet.model.dto.transaction.CreateTransactionRequest;
import lv.vladislavs.ewallet.repository.TransactionRepository;
import lv.vladislavs.ewallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lv.vladislavs.ewallet.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    TransactionService transactionService;

    @Mock
    WalletRepository walletRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Test
    void testTransactionMinAmount() {
        Wallet testWallet = setupWalletMock();

        assertThrows(TransactionMinAmountException.class, () ->
                transactionService.createTransaction(
                        createDepositTransactionRequest(testWallet, BigDecimal.ZERO),
                        testWallet.getUser()));

        final Transaction transaction = assertAndCaptureTransactionSave();
        assertEquals(testWallet.getUser(), transaction.getInitiator());
        assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
        assertEquals(BigDecimal.ZERO, transaction.getTargetWallet().getAmount());
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testTransactionMaxAmount() {
        Wallet testWallet = setupWalletMock();

        assertThrows(TransactionMaxAmountException.class, () ->
                transactionService.createTransaction(
                        createDepositTransactionRequest(testWallet, TRANSACTION_MAX_AMOUNT.add(BigDecimal.ONE)),
                        testWallet.getUser()));

        final Transaction transaction = assertAndCaptureTransactionSave();
        assertEquals(testWallet.getUser(), transaction.getInitiator());
        assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
        assertEquals(BigDecimal.ZERO, transaction.getTargetWallet().getAmount());
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testTransactionSuspiciousAmount() {
        Wallet testWallet = setupWalletMock();

        assertThrows(TransactionSuspiciousException.class, () ->
                transactionService.createTransaction(
                        createDepositTransactionRequest(testWallet, TRANSACTION_SUSPICIOUS_AMOUNT.add(BigDecimal.ONE)),
                        testWallet.getUser()));

        final Transaction transaction = assertAndCaptureTransactionSave();
        assertEquals(testWallet.getUser(), transaction.getInitiator());
        assertTrue(transaction.isSuspicious());
        assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
        assertEquals(BigDecimal.ZERO, transaction.getTargetWallet().getAmount());
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testTransactionHourlyLimitSetsProhibitNewTransactions() {
        Wallet testWallet = setupWalletMock();
        List<Transaction> pastTransactions = new ArrayList<>();
        for (int i = 0; i < TRANSACTION_PROHIBIT_HOURLY_COUNT-1; i++) {
            pastTransactions.add(Transaction.builder()
                    .initiator(testWallet.getUser())
                    .creationDateTime(ZonedDateTime.now())
                    .type(TransactionType.DEPOSIT)
                    .amount(new BigDecimal("10.50"))
                    .build());
        }
        when(transactionRepository.findByInitiatorIdAndCreationDateTimeBetween(eq(testWallet.getUser().getId()), any(), any()))
                .thenReturn(pastTransactions);

        transactionService.createTransaction(
                createDepositTransactionRequest(testWallet, new BigDecimal("10.50")),
                testWallet.getUser());

        final Transaction transaction = assertAndCaptureTransactionSave();
        assertNotNull(transaction.getInitiator().getProhibitOutgoingTransactions());
        assertNotNull(transaction.getInitiator().getProhibitIncomingTransactions());
        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testTransactionProhibitIncoming() {
        Wallet testWallet = setupWalletMock();
        testWallet.getUser().setProhibitIncomingTransactions(ZonedDateTime.now().plusDays(1));

        assertThrows(TransactionProhibitedIncomingException.class, () ->
            transactionService.createTransaction(
                    createDepositTransactionRequest(testWallet, new BigDecimal("10.50")),
                    testWallet.getUser()));

        final Transaction transaction = assertAndCaptureTransactionSave();
        assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
        assertEquals(BigDecimal.ZERO, transaction.getTargetWallet().getAmount());
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testTransactionNotEnoughFunds() {
        Wallet testWallet = setupWalletMock();

        assertThrows(TransactionNotEnoughFundsException.class, () ->
                transactionService.createTransaction(
                        createWithdrawalTransactionRequest(testWallet, new BigDecimal("10.50")),
                        testWallet.getUser()));

        final Transaction transaction = assertAndCaptureTransactionSave();
        assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
        assertEquals(BigDecimal.ZERO, transaction.getSourceWallet().getAmount());
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testTransactionWithdrawalLimit() {
        Wallet testWallet = setupWalletMock();
        List<Transaction> pastTransactions = List.of(Transaction.builder()
                .initiator(testWallet.getUser())
                .creationDateTime(ZonedDateTime.now())
                .type(TransactionType.WITHDRAW)
                .amount(TRANSACTION_WITHDRAWAL_DAILY_LIMIT)
                .build());
        when(transactionRepository.findByInitiatorIdAndCreationDateTimeBetween(eq(testWallet.getUser().getId()), any(), any()))
                .thenReturn(pastTransactions);
        testWallet.setAmount(BigDecimal.TEN);

        assertThrows(TransactionWithdrawalLimitException.class, () ->
            transactionService.createTransaction(
                    createWithdrawalTransactionRequest(testWallet, BigDecimal.ONE),
                    testWallet.getUser()));

        final Transaction transaction = assertAndCaptureTransactionSave();
        assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
        assertEquals(BigDecimal.TEN, transaction.getSourceWallet().getAmount());
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testTransactionSuccessChangesWalletAmount() {
        Wallet testWallet = setupWalletMock();

        transactionService.createTransaction(
                createDepositTransactionRequest(testWallet, new BigDecimal("10.50")),
                testWallet.getUser());

        final Transaction transaction = assertAndCaptureTransactionSave();
        assertEquals(testWallet.getUser(), transaction.getInitiator());
        assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        assertEquals(new BigDecimal("10.50"), transaction.getTargetWallet().getAmount());
    }

    private Wallet setupWalletMock() {
        User testUser = User.builder()
                .id(1L)
                .email("testuser@gmail.com")
                .build();
        Wallet testWallet = Wallet.builder()
                .id(1L)
                .user(testUser)
                .title("Test wallet")
                .build();
        when(walletRepository.findById(testWallet.getId())).thenReturn(Optional.of(testWallet));
        when(transactionRepository.save(any())).thenReturn(new Transaction());
        return testWallet;
    }

    private CreateTransactionRequest createDepositTransactionRequest(Wallet wallet, BigDecimal amount) {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType("deposit");
        createTransactionRequest.setTargetWalletId(wallet.getId());
        createTransactionRequest.setAmount(amount);
        return createTransactionRequest;
    }

    private CreateTransactionRequest createWithdrawalTransactionRequest(Wallet wallet, BigDecimal amount) {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType("withdraw");
        createTransactionRequest.setSourceWalletId(wallet.getId());
        createTransactionRequest.setAmount(amount);
        return createTransactionRequest;
    }

    private Transaction assertAndCaptureTransactionSave() {
        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1)).save(transactionArgumentCaptor.capture());
        return transactionArgumentCaptor.getValue();
    }
}