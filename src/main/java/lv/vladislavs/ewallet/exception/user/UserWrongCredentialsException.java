package lv.vladislavs.ewallet.exception.user;

import lv.vladislavs.ewallet.exception.EWalletException;
import org.springframework.http.HttpStatus;

public class UserWrongCredentialsException extends EWalletException {
    public UserWrongCredentialsException() {
        super("USER_WRONG_CREDENTIALS",
                "Wrong user credentials specified",
                HttpStatus.NOT_FOUND);
    }
}
