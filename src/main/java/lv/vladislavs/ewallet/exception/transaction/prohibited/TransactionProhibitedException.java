package lv.vladislavs.ewallet.exception.transaction.prohibited;

import lv.vladislavs.ewallet.exception.EWalletException;
import org.springframework.http.HttpStatus;

public class TransactionProhibitedException extends EWalletException {
    public TransactionProhibitedException(String code, String message, HttpStatus httpStatusCode) {
        super(code, message, httpStatusCode);
    }
}
