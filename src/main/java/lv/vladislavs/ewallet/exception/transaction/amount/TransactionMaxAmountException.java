package lv.vladislavs.ewallet.exception.transaction.amount;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class TransactionMaxAmountException extends TransactionAmountException {
    public TransactionMaxAmountException(BigDecimal maxAmount, BigDecimal amount) {
        super("TRANSACTION_MAX_AMOUNT",
                "Single transaction limit exceeded, maxAmount=" + maxAmount + ", amount=" + amount,
                HttpStatus.BAD_REQUEST);
    }
}
