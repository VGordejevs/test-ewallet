package lv.vladislavs.ewallet.repository;

import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.database.wallet.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class WalletRepositoryTest extends BaseRepositoryTest {
    private final static String TEST_TITLE = "Test Wallet";

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    UserRepository userRepository;
 
    @Test
    void testWalletRepository() {
        Wallet newWallet = Wallet.builder()
                .id(null)
                .user(userRepository.save(User.builder()
                        .email("testuser@gmail.com")
                        .build()))
                .title(TEST_TITLE)
                .build();

        final Wallet wallet = walletRepository.save(newWallet);

        assertNotNull(wallet.getId());
        assertNotNull(wallet.getUser());
        assertEquals(TEST_TITLE, wallet.getTitle());
        assertNotNull(wallet.getCreationDateTime());
        assertEquals(BigDecimal.ZERO, wallet.getAmount());
    }

    @Test
    void testWalletRepositoryUserDataIntegrity() {
        Wallet newWallet = new Wallet();

        assertThrows(DataIntegrityViolationException.class, () -> walletRepository.save(newWallet));
    }
}