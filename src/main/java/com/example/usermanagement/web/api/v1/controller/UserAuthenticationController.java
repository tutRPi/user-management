package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.web.api.common.delegate.AuthenticationDelegate;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.AuthenticationRequest;
import com.example.usermanagement.web.api.v1.response.AuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserAuthenticationController {
    public static final String PATH = "/user/auth";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationDelegate authenticationDelegate;

    @PostMapping(path = PATH)
    public ResponseEntity<AuthenticationResponse> doAuthentication(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        final Authentication authentication =
                this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        log.debug("After authentication: " + authentication + ", name: " + authentication.getName() + ", principal: " + authentication.getPrincipal() + ", credentials: " + authentication.getCredentials());
        final CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        final AuthenticationResponse authenticationResponse = this.authenticationDelegate.doAuthentication(customUserDetails.getUser(), !customUserDetails.getUser().isYn2faEnabled());
        return ResponseEntity.ok().body(authenticationResponse);
    }
}