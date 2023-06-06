package lv.vladislavs.ewallet.controller;

import lv.vladislavs.ewallet.model.database.transaction.TransactionType;
import lv.vladislavs.ewallet.model.database.user.User;
import lv.vladislavs.ewallet.model.database.wallet.Wallet;
import lv.vladislavs.ewallet.model.dto.transaction.CreateTransactionRequest;
import lv.vladislavs.ewallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static lv.vladislavs.ewallet.Constants.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerIT extends BaseIntegrationTest {
    private final static String TRANSACTION_BASEURL = "/api/transaction";

    private final static String UNSUPPORTED_TRANSACTION_TYPE = "unsupported-transaction-type";

    @Autowired
    private WalletRepository walletRepository;

    private long testWallet1Id;
    private long testWallet2Id;

    @BeforeAll
    void beforeAll() {
        testWallet1Id = walletRepository.save(Wallet.builder()
                .user(testUser)
                .title("Test wallet 1")
                .build()
        ).getId();

        testWallet2Id = walletRepository.save(Wallet.builder()
                .user(testUser)
                .title("Test wallet 2")
                .build()
        ).getId();
    }

    @Test
    @Order(1)
    void testTransactionTypeUnsupported() throws Exception {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType(UNSUPPORTED_TRANSACTION_TYPE);

        mockMvc.perform(post(TRANSACTION_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(createTransactionRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.message").value(containsString(UNSUPPORTED_TRANSACTION_TYPE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    void testTransactionMinAmount() throws Exception {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType(TransactionType.DEPOSIT.name().toLowerCase());
        createTransactionRequest.setTargetWalletId(testWallet1Id);
        createTransactionRequest.setAmount(BigDecimal.ZERO);

        mockMvc.perform(post(TRANSACTION_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(createTransactionRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.errorCode").value("TRANSACTION_MIN_AMOUNT"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void testTransactionMaxAmount() throws Exception {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType(TransactionType.DEPOSIT.name().toLowerCase());
        createTransactionRequest.setTargetWalletId(testWallet1Id);
        createTransactionRequest.setAmount(TRANSACTION_MAX_AMOUNT.add(BigDecimal.ONE));

        mockMvc.perform(post(TRANSACTION_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(createTransactionRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.errorCode").value("TRANSACTION_MAX_AMOUNT"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    void testTransactionSuspiciousAmount() throws Exception {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType(TransactionType.DEPOSIT.name().toLowerCase());
        createTransactionRequest.setTargetWalletId(testWallet1Id);
        createTransactionRequest.setAmount(TRANSACTION_SUSPICIOUS_AMOUNT.add(BigDecimal.ONE));

        mockMvc.perform(post(TRANSACTION_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(createTransactionRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.errorCode").value("TRANSACTION_SUSPICIOUS"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void testDepositTransactionSuccess() throws Exception {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType(TransactionType.DEPOSIT.name().toLowerCase());
        createTransactionRequest.setTargetWalletId(testWallet1Id);
        createTransactionRequest.setAmount(new BigDecimal("100.00"));

        mockMvc.perform(post(TRANSACTION_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(createTransactionRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void testWithdrawalTransactionNotEnoughFunds() throws Exception {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType(TransactionType.WITHDRAW.name().toLowerCase());
        createTransactionRequest.setSourceWalletId(testWallet1Id);
        createTransactionRequest.setAmount(new BigDecimal("101.00"));

        mockMvc.perform(post(TRANSACTION_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(createTransactionRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.errorCode").value("TRANSACTION_NOT_ENOUGH_FUNDS"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void testWithdrawalTransactionSuccess() throws Exception {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType(TransactionType.WITHDRAW.name().toLowerCase());
        createTransactionRequest.setSourceWalletId(testWallet1Id);
        createTransactionRequest.setAmount(new BigDecimal("25.00"));

        mockMvc.perform(post(TRANSACTION_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(createTransactionRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void testTransferTransactionSuccess() throws Exception {
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType(TransactionType.TRANSFER.name().toLowerCase());
        createTransactionRequest.setSourceWalletId(testWallet1Id);
        createTransactionRequest.setTargetWalletId(testWallet2Id);
        createTransactionRequest.setAmount(new BigDecimal("25.00"));

        mockMvc.perform(post(TRANSACTION_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, testUserAuthorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(createTransactionRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void testTransactionTooManyHourlyTransactions() throws Exception {
        User newTestUser = addTestUser("newtestuser@gmail.com");
        String newTestUserAuthorization = getAuthorization(newTestUser);
        long newTestWalletId = walletRepository.save(Wallet.builder()
                .user(newTestUser)
                .title("Test wallet 1")
                .build()
        ).getId();

        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType(TransactionType.DEPOSIT.name().toLowerCase());
        createTransactionRequest.setTargetWalletId(newTestWalletId);
        createTransactionRequest.setAmount(new BigDecimal("50.00"));

        for (int i = 0; i < TRANSACTION_PROHIBIT_HOURLY_COUNT; i++) {
            mockMvc.perform(post(TRANSACTION_BASEURL)
                            .header(HttpHeaders.AUTHORIZATION, newTestUserAuthorization)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(asJsonString(createTransactionRequest)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        mockMvc.perform(post(TRANSACTION_BASEURL)
                        .header(HttpHeaders.AUTHORIZATION, newTestUserAuthorization)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(createTransactionRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.errorCode").value("TRANSACTION_PROHIBITED_INCOMING"))
                .andExpect(status().isTooManyRequests());
    }
}
