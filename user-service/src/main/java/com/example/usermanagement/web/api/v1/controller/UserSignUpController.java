package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.common.SecurityHelper;
import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.ConfirmationTokenService;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.util.RandomStringUtil;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.UserSignUpRequest;
import com.example.usermanagement.web.api.v1.response.UserAccountDataResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@Tag(name = "registration")
public class UserSignUpController {
    public static final String PATH = "/user/signup";

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @PostMapping(path = PATH)
    public ResponseEntity<UserAccountDataResponse> doUserSignUp(HttpServletRequest request, @RequestBody @Valid UserSignUpRequest userSignUpRequest) {
        User toSignUp = new User();
        toSignUp.setEmail(userSignUpRequest.getEmail());
        toSignUp.setFirstName(userSignUpRequest.getFirstName());
        toSignUp.setLastName(userSignUpRequest.getLastName());
        toSignUp.setPassword(this.passwordEncoder.encode(userSignUpRequest.getPassword()));
        toSignUp.setTwoFaEnabled(userSignUpRequest.isT2FAEnabled());
        toSignUp.setCreatedOn(new Date());

        ConfirmationToken token = new ConfirmationToken();
        token.setToken(RandomStringUtil.getAlphaNumericString());
        token.setUser(toSignUp);
        token.setCreatedOn(new Date());
        token.setExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.DAYS)));

        if (userSignUpRequest.isT2FAEnabled()) {
            //Generate 2FA random secret
            toSignUp.setTwoFaSecret(SecurityHelper.generateSecretKey());
        }
        User signedUp = this.userService.signUp(toSignUp, token);

        // send email
        confirmationTokenService.sendConfirmationLink(signedUp.getEmail(), token, request);

        // Build the response
        UserAccountDataResponse response = new UserAccountDataResponse();
        if (userSignUpRequest.isT2FAEnabled()) {
            //Generate 2FA qr code image url
            response.setT2FAQRCodeImageURL(SecurityHelper.generate2FAQRCodeImageURL(signedUp));
        }
        response.setT2FAEnabled(signedUp.isTwoFaEnabled());
        return ResponseEntity.ok(response);
    }
}