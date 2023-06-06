package lv.vladislavs.ewallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import lv.vladislavs.ewallet.Constants;
import lv.vladislavs.ewallet.authentication.JwtUtil;
import lv.vladislavs.ewallet.converter.user.UserMapper;
import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.dto.user.JwtUserInfo;
import lv.vladislavs.ewallet.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BaseIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected Jackson2ObjectMapperBuilder mapperBuilder;

    protected ObjectWriter objectWriter;

    protected User testUser;
    protected String testUserAuthorization;

    @BeforeAll
    public void init() {
        objectWriter = mapperBuilder.build().writer().withDefaultPrettyPrinter();
        testUser = addTestUser("testuser@gmail.com");
        testUserAuthorization = getAuthorization(testUser);
    }

    protected User addTestUser(String email) {
        return userRepository.save(User.builder()
                .email(email)
                .build());
    }

    protected String getAuthorization(User user) {
        JwtUserInfo jwtUserInfo = UserMapper.INSTANCE.userToJwtUserInfo(user);
        return "Bearer " + JwtUtil.generateToken(jwtUserInfo, Constants.AUTH_TOKEN_VALID_HOURS);
    }

    protected String asJsonString(Object object) {
        try {
            return objectWriter.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
