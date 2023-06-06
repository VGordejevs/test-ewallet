package lv.vladislavs.ewallet.exception.transaction.amount;

import lv.vladislavs.ewallet.exception.EWalletException;
import org.springframework.http.HttpStatus;

public class TransactionAmountException extends EWalletException {
    public TransactionAmountException(String code, String message, HttpStatus httpStatusCode) {
        super(code, message, httpStatusCode);
    }
}
