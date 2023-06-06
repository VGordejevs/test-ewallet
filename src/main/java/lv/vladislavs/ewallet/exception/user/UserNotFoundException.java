package lv.vladislavs.ewallet.exception.user;

import lv.vladislavs.ewallet.exception.EWalletException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends EWalletException {
    public UserNotFoundException(String email) {
        super("USER_NOT_FOUND",
                "User not found, email=" + email,
                HttpStatus.NOT_FOUND);
    }
}
