package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.UserManagementApplication;
import com.example.usermanagement.web.api.v1.request.LockUserRequest;
import com.example.usermanagement.web.api.v1.request.T2FACodeVerificationRequest;
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
public class LockUserAdminControllerIntegrationTest extends SetupUserHelper {

    private static String LOGIN_URL = "/api/v1/admin/lock-user";

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        loadDbData(true);
    }


    @Test
    void lockUser_wrongUserId() throws Exception {
        LockUserRequest dto = new LockUserRequest();
        dto.setUserId(99999L);
        dto.setLock(true);

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        resultActions.andExpect(status().isBadRequest());
    }
}
