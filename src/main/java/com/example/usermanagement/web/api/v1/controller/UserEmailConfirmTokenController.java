package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.service.ConfirmationTokenService;
import com.example.usermanagement.web.api.v1.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@Tag(name = "registration")
public class UserEmailConfirmTokenController {
    public static final String PATH = "/user/confirm";

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @GetMapping(path = PATH)
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {

        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found."));

        if (confirmationToken.getNmUserId().getDtEmailVerifiedOn() != null) {
            // TODO change with better error message
            throw new IllegalStateException("User already confirmed.");
        }

        if (confirmationToken.getDtConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed.");
        }

        Date expiresAt = confirmationToken.getDtExpiresAt();

        if (expiresAt.before(new Date())) {
            throw new IllegalStateException("Token expired");
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);

        return ResponseEntity.ok("ok"); // TODO
    }
}
