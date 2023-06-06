package lv.vladislavs.ewallet.converter.user;

import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.dto.user.RegistrationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegistrationRequestMapperTest {
    private final static String TEST_EMAIL = "testuser@gmail.com";
    private final static String TEST_PASSWORD_HASH = "PasswordHASH";
    private final static String TEST_NAME = "Vladislavs";
    private final static String TEST_SURNAME = "Gordejevs";
    private final static String TEST_PHONE_NUMBER = "+371 26734444";

    RegistrationRequestMapper mapper = RegistrationRequestMapper.INSTANCE;
    
    @Test
    void testRegistrationRequestMapper() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(TEST_EMAIL);
        registrationRequest.setPasswordHash(TEST_PASSWORD_HASH);
        registrationRequest.setName(TEST_NAME);
        registrationRequest.setSurname(TEST_SURNAME);
        registrationRequest.setPhoneNumber(TEST_PHONE_NUMBER);

        final User user = mapper.registrationRequestToUser(registrationRequest);

        assertNull(user.getId());
        assertEquals(TEST_EMAIL, user.getEmail());
        assertEquals(TEST_PASSWORD_HASH, user.getPasswordHash());
        assertEquals(TEST_NAME, user.getName());
        assertEquals(TEST_SURNAME, user.getSurname());
        assertEquals(TEST_PHONE_NUMBER, user.getPhoneNumber());
        assertFalse(user.isSuspicious());
        assertNull(user.getProhibitOutgoingTransactions());
        assertNull(user.getProhibitIncomingTransactions());
    }
}
