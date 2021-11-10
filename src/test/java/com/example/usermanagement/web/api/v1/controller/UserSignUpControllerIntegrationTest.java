package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.UserManagementApplication;
import com.example.usermanagement.web.api.v1.request.UserSignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = UserManagementApplication.class)
public class UserSignUpControllerIntegrationTest extends SetupUserHelper {

    private static String SIGNUP_URL = "/api/v1/user/signup";

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void doUserSignUp_existingEmail() throws Exception {

        UserSignUpRequest dto = validUserSignUpRequest();
        dto.setEmail(user.getEmail());

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = this.mockMvc.perform(post(SIGNUP_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("errors", hasSize(1)));
        resultActions.andExpect(jsonPath("errors[0].field", is("email")));
        resultActions.andExpect(jsonPath("errors[0].code", is(775)));
    }

    @Test
    void doUserSignUp_invalidEmail() throws Exception {

        UserSignUpRequest dto = validUserSignUpRequest();
        dto.setEmail("invalidEmail");

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = this.mockMvc.perform(post(SIGNUP_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("errors", hasSize(1)));
        resultActions.andExpect(jsonPath("errors[0].field", is("email")));
        resultActions.andExpect(jsonPath("errors[0].code", is(775)));
    }

    @Test
    void doUserSignUp_success() throws Exception {

        UserSignUpRequest dto = validUserSignUpRequest();

        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions resultActions = this.mockMvc.perform(post(SIGNUP_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.errors").doesNotExist());
        resultActions.andExpect(jsonPath("$.timestamp").exists());
        resultActions.andExpect(jsonPath("$.t2FAEnabled", is(true)));
        resultActions.andExpect(jsonPath("$.t2FAQRCodeImageURL").exists());
    }


    UserSignUpRequest validUserSignUpRequest() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setFirstName("Test");
        userSignUpRequest.setLastName("User");
        userSignUpRequest.setT2FAEnabled(true);
        userSignUpRequest.setEmail("test@example.com");
        userSignUpRequest.setPassword("P4ssw0rd!123");
        userSignUpRequest.setPasswordConfirmation("P4ssw0rd!123");
        return userSignUpRequest;
    }
}
