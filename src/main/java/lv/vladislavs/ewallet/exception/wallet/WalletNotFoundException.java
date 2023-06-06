package lv.vladislavs.ewallet.exception.wallet;

import lv.vladislavs.ewallet.exception.EWalletException;
import org.springframework.http.HttpStatus;

public class WalletNotFoundException extends EWalletException {
    public WalletNotFoundException(long walletId) {
        super("WALLET_NOT_FOUND_ERROR",
                "Wallet not found, walletId=" + walletId,
                HttpStatus.NOT_FOUND);
    }
}
