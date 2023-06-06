package lv.vladislavs.ewallet.exception.transaction.amount;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class TransactionWithdrawalLimitException extends TransactionAmountException {
    public TransactionWithdrawalLimitException(BigDecimal maxAmount) {
        super("TRANSACTION_WITHDRAWAL_LIMIT",
                "Daily withdrawal limit exceeded, maxAmount=" + maxAmount,
                HttpStatus.BAD_REQUEST);
    }
}
