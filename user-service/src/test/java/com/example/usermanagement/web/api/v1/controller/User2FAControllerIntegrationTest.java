package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.UserManagementApplication;
import com.example.usermanagement.web.api.v1.request.T2FACodeVerificationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = UserManagementApplication.class)
class User2FAControllerIntegrationTest extends SetupUserHelper {

    private static String LOGIN_URL = "/api/v1/user/2fa";

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        loadDbData(true);
    }


    @Test
    void doVerification_shortCode() throws Exception {
        T2FACodeVerificationRequest dto = new T2FACodeVerificationRequest();
        dto.setT2FACode("0");

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        resultActions.andExpect(status().isBadRequest());
    }
}
