package lv.vladislavs.ewallet.exception.user;

import lv.vladislavs.ewallet.exception.EWalletException;
import org.springframework.http.HttpStatus;

public class UserRegistrationFailedException extends EWalletException {
    public UserRegistrationFailedException(String message, HttpStatus httpStatus) {
        super("USER_REGISTRATION_FAILED",
                "Registration failed, reason: " + message,
                httpStatus);
    }
}
