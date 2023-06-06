package lv.vladislavs.ewallet.exception.transaction.amount;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class TransactionNotEnoughFundsException extends TransactionAmountException {
    public TransactionNotEnoughFundsException(long walletId, BigDecimal amount) {
        super("TRANSACTION_NOT_ENOUGH_FUNDS",
                "Minimal transaction amount, walletId=" + walletId + ", amount=" + amount,
                HttpStatus.BAD_REQUEST);
    }
}
