package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.web.api.common.delegate.AuthenticationDelegate;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.T2FACodeVerificationRequest;
import com.example.usermanagement.web.api.v1.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_2FA_CODE_VERIFICATION})
@Tag(name = "login")
public class User2FAController implements SecuredRestController {
    public static final String PATH = "/user/2fa";

    @Autowired
    AuthenticationDelegate authenticationDelegate;

    @PostMapping(path = PATH)
    public ResponseEntity<AuthenticationResponse> doVerification(@RequestBody @Valid T2FACodeVerificationRequest t2FACodeVerificationRequest) {
        ResponseEntity<AuthenticationResponse> toRet;
        AuthenticationResponse authenticationResponse;

        log.debug("T2FACodeVerificationController verifying 2FA code '" + t2FACodeVerificationRequest.getT2FACode() + "'.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // Verify 2fa
        Totp totp = new Totp(customUserDetails.getUser().getDs2faSecret());
        if (totp.verify(t2FACodeVerificationRequest.getT2FACode())) {
            authenticationResponse = this.authenticationDelegate.doAuthentication(customUserDetails.getUser(), true);
            toRet = ResponseEntity.ok().body(authenticationResponse);
        } else {
            authenticationResponse = new AuthenticationResponse();
            authenticationResponse.addResponseError(ErrorsEnum.INVALID_2FA_CODE);
            authenticationResponse.setMustVerify2FACode(true);
            toRet = ResponseEntity.status(HttpStatus.NOT_FOUND).body(authenticationResponse);
        }

        return toRet;
    }
}