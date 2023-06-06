package lv.vladislavs.ewallet.exception.authentication;

import lv.vladislavs.ewallet.exception.EWalletException;
import org.springframework.http.HttpStatus;

public class AuthenticationFailedException extends EWalletException {
    public AuthenticationFailedException() {
        super("AUTHENTICATION_FAILED",
                "Authentication failed",
                HttpStatus.UNAUTHORIZED);
    }
}
