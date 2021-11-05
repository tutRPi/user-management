package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.web.api.common.delegate.JWTProcessorDelegate;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.JWTVerificationRequest;
import com.example.usermanagement.web.api.v1.response.JWTVerificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
public class JWTController {
    public static final String PATH = "/jwt/verify";

    @Autowired
    JWTProcessorDelegate jwtProcessorDelegate;

    @PostMapping(path = PATH)
    public ResponseEntity<JWTVerificationResponse> doVerification(@RequestBody @Valid JWTVerificationRequest jwtVerificationRequest) {
        JWTVerificationResponse response = new JWTVerificationResponse();
        jwtProcessorDelegate.buildAuthenticationFromJWT(jwtVerificationRequest.getJwt(), false);
        response.setJwtValid(true);
        return ResponseEntity.ok().body(response);
    }
}
