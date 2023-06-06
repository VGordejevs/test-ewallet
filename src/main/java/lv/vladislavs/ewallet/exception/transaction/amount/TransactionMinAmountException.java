package lv.vladislavs.ewallet.exception.transaction.amount;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class TransactionMinAmountException extends TransactionAmountException {
    public TransactionMinAmountException(BigDecimal minAmount, BigDecimal amount) {
        super("TRANSACTION_MIN_AMOUNT",
                "Minimal transaction amount, minAmount=" + minAmount + ", amount=" + amount,
                HttpStatus.BAD_REQUEST);
    }
}
