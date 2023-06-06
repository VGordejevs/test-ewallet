package lv.vladislavs.ewallet.exception.transaction;

import lv.vladislavs.ewallet.exception.EWalletException;
import org.springframework.http.HttpStatus;

public class TransactionStatusUnknownException extends EWalletException {
    public TransactionStatusUnknownException(String transactionStatus) {
        super("TRANSACTION_STATUS_UNKNOWN",
                "Unknown transaction status, transactionStatus=" + transactionStatus,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
