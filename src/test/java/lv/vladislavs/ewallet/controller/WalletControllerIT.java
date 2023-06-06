package lv.vladislavs.ewallet.controller;

import lv.vladislavs.ewallet.model.dto.wallet.CreateWalletRequest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletControllerIT extends BaseIntegrationTest {
    private final static String WALLET_BASEURL = "/api/wallet";

    private final static String TEST_WALLET_TITLE = "Test wallet";

    @Test
    @Order(0)
    void testGetUserWalletsEmpty() throws Exception {
        mockMvc.perform(get(WALLET_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(1)
    void testCreateWalletSuccess() throws Exception {
        CreateWalletRequest createWalletRequest = new CreateWalletRequest();
        createWalletRequest.setTitle(TEST_WALLET_TITLE);

        mockMvc.perform(post(WALLET_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(createWalletRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void testGetUserWalletsReturnsWallet() throws Exception {
        mockMvc.perform(get(WALLET_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", equalTo(TEST_WALLET_TITLE)))
                .andExpect(status().isOk());
    }
}
