package lv.vladislavs.ewallet.controller;

import lv.vladislavs.ewallet.model.dto.user.LoginRequest;
import lv.vladislavs.ewallet.model.dto.user.RegistrationRequest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIT extends BaseIntegrationTest {
    private final static String USER_BASEURL = "/api/user";
    private final static String USER_REGISTER_URL = USER_BASEURL + "/register";
    private final static String USER_LOGIN_URL = USER_BASEURL + "/login";

    private final static String TEST_USER_EMAIL = "vladislavs.gordejevs@gmail.com";
    private final static String TEST_USER_PASSWORD = "PasswordHASH";
    private final static String TEST_NOT_EXISTING_USER_EMAIL = "this-user-doesnt-exist@gmail.com";

    @Test
    @Order(1)
    void testRegisterNewUserSuccess() throws Exception {
        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(getRegistrationRequest())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void testUserAlreadyRegistered() throws Exception {
        mockMvc.perform(post(USER_REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(getRegistrationRequest())))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(TEST_USER_EMAIL);
        loginRequest.setPasswordHash(TEST_USER_PASSWORD);
        mockMvc.perform(post(USER_LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(loginRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.jwtToken").value(not(emptyOrNullString())))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void testLoginWrongCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(TEST_NOT_EXISTING_USER_EMAIL);
        loginRequest.setPasswordHash(TEST_USER_PASSWORD);
        mockMvc.perform(post(USER_LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(loginRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private RegistrationRequest getRegistrationRequest() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(TEST_USER_EMAIL);
        registrationRequest.setPasswordHash(TEST_USER_PASSWORD);
        registrationRequest.setName("Vladislavs");
        registrationRequest.setSurname("Gordejevs");
        registrationRequest.setPhoneNumber("+371 26734444");
        return registrationRequest;
    }
}
