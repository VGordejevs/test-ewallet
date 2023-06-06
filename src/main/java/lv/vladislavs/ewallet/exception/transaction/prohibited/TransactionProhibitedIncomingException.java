package lv.vladislavs.ewallet.exception.transaction.prohibited;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class TransactionProhibitedIncomingException extends TransactionProhibitedException {
    public TransactionProhibitedIncomingException(long userId, ZonedDateTime until) {
        super("TRANSACTION_PROHIBITED_INCOMING",
                "Incoming transactions are prohibited for user, userId=" + userId + ", until=" + until,
                HttpStatus.TOO_MANY_REQUESTS);
    }
}
