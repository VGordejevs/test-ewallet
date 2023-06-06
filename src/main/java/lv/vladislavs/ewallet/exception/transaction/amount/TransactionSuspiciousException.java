package lv.vladislavs.ewallet.exception.transaction.amount;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class TransactionSuspiciousException extends TransactionAmountException {
    public TransactionSuspiciousException(BigDecimal amount) {
        super("TRANSACTION_SUSPICIOUS",
                "Transaction is suspicious, amount=" + amount,
                HttpStatus.BAD_REQUEST);
    }
}
