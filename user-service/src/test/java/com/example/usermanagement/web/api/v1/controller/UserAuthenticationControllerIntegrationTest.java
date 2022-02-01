package com.example.usermanagement.web.api.v1.controller;


import com.example.usermanagement.UserManagementApplication;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import com.example.usermanagement.web.api.v1.request.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = UserManagementApplication.class)
class UserAuthenticationControllerIntegrationTest extends SetupUserHelper {

    private static String LOGIN_URL = "/api/v1/user/auth";

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        loadDbData(true);
    }

    @Test
    void doAuthentication_userNotConfirmed() throws Exception {
        AuthenticationRequest dto = getAuthenticationRequest();

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("errorDetails", hasSize(1)));
        resultActions.andExpect(jsonPath("errorDetails[0].field", is("username")));
        resultActions.andExpect(jsonPath("errorDetails[0].code", is(775)));
    }

    @Test
    void doAuthentication_userDisabled() throws Exception {
        user.setDisabledOn(Instant.now());
        entityManager.persist(user);
        entityManager.flush();

        AuthenticationRequest dto = getAuthenticationRequest();

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("errorDetails", hasSize(1)));
        resultActions.andExpect(jsonPath("errorDetails[0].message").exists());
        resultActions.andExpect(jsonPath("errorDetails[0].code", is(775)));
    }

    @Test
    void doAuthentication_userLocked() throws Exception {
        user.setLockedOn(Instant.now());
        entityManager.persist(user);
        entityManager.flush();

        AuthenticationRequest dto = getAuthenticationRequest();

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("errorDetails", hasSize(1)));
        resultActions.andExpect(jsonPath("errorDetails[0].field", is("username")));
        resultActions.andExpect(jsonPath("errorDetails[0].code", is(775)));
    }

    @Test
    void doAuthentication_wrongPassword() throws Exception {
        user.setEmailVerifiedOn(Instant.now());
        entityManager.persist(user);
        entityManager.flush();

        AuthenticationRequest dto = getAuthenticationRequest();
        dto.setPassword("wrong");

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("errorCode", is(52)));
    }

    @Test
    void doAuthentication_success() throws Exception {
        user.setEmailVerifiedOn(Instant.now());
        entityManager.persist(user);
        entityManager.flush();

        AuthenticationRequest dto = getAuthenticationRequest();

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.errorDetails").doesNotExist());
        resultActions.andExpect(jsonPath("$.timestamp").exists());
        resultActions.andExpect(jsonPath("$.mustVerify2FACode", is(true)));
        resultActions.andExpect(jsonPath("$.jwt").exists());
    }

    private AuthenticationRequest getAuthenticationRequest() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(user.getEmail());
        authenticationRequest.setPassword("Passw0rd!123");
        return authenticationRequest;
    }

}
