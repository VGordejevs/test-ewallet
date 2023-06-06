package lv.vladislavs.ewallet.exception.transaction.prohibited;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class TransactionProhibitedOutgoingException extends TransactionProhibitedException {
    public TransactionProhibitedOutgoingException(long userId, ZonedDateTime until) {
        super("TRANSACTION_PROHIBITED_OUTGOING",
                "Outgoing transactions are prohibited for user, userId=" + userId + ", until=" + until,
                HttpStatus.TOO_MANY_REQUESTS);
    }
}
