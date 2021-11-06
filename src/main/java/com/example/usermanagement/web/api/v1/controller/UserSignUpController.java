package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.common.SecurityHelper;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.UserSignUpRequest;
import com.example.usermanagement.web.api.v1.response.UserAccountDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
public class UserSignUpController {
    public static final String PATH = "/user/signup";

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @PostMapping(path = PATH)
    public ResponseEntity<UserAccountDataResponse> doUserSignUp(@RequestBody @Valid UserSignUpRequest userSignUpRequest) {
        User toSignUp = new User();
        toSignUp.setDsEmail(userSignUpRequest.getEmail());
        toSignUp.setDsFirstName(userSignUpRequest.getFirstName());
        toSignUp.setDsLastName(userSignUpRequest.getLastName());
        toSignUp.setDsPassword(this.passwordEncoder.encode(userSignUpRequest.getPassword()));
        toSignUp.setYn2faEnabled(userSignUpRequest.isT2FAEnabled());
        toSignUp.setDtCreatedOn(new Date());

        if (userSignUpRequest.isT2FAEnabled()) {
            //Generate 2FA random secret
            toSignUp.setDs2faSecret(SecurityHelper.generateSecretKey());
        }
        User signedUp = this.userService.signUp(toSignUp);
        UserAccountDataResponse response = new UserAccountDataResponse();
        if (userSignUpRequest.isT2FAEnabled()) {
            //Generate 2FA qr code image url
            response.setT2FAQRCodeImageURL(SecurityHelper.generate2FAQRCodeImageURL(signedUp));
        }
        response.setT2FAEnabled(signedUp.isYn2faEnabled());
        return ResponseEntity.ok(response);
    }
}