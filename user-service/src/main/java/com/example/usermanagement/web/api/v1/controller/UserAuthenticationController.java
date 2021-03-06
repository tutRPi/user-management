package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.web.api.common.delegate.AuthenticationDelegate;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.AuthenticationRequest;
import com.example.usermanagement.web.api.v1.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@Tag(name = "login")
public class UserAuthenticationController {
    public static final String PATH = "/user/auth";

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthenticationDelegate authenticationDelegate;

    @PostMapping(path = PATH)
    @Operation(summary = "Login to get JWT")
    public ResponseEntity<AuthenticationResponse> doAuthentication(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        final Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        log.debug("After authentication: " + authentication + ", name: " + authentication.getName() + ", principal: " + authentication.getPrincipal() + ", credentials: " + authentication.getCredentials());
        final CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        final AuthenticationResponse authenticationResponse = authenticationDelegate.doAuthentication(customUserDetails.getUser(), !customUserDetails.getUser().isTwoFaEnabled());
        return ResponseEntity.ok().body(authenticationResponse);
    }
}