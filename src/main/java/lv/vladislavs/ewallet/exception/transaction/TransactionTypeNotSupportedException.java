package lv.vladislavs.ewallet.exception.transaction;

import lv.vladislavs.ewallet.exception.EWalletException;
import org.springframework.http.HttpStatus;

public class TransactionTypeNotSupportedException extends EWalletException {
    public TransactionTypeNotSupportedException(String transactionType) {
        super("TRANSACTION_TYPE_NOT_SUPPORTED",
                "Transaction type not supported, transactionType=" + transactionType,
                HttpStatus.BAD_REQUEST);
    }
}
