package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.model.ConfirmationToken;
import com.example.usermanagement.business.service.ConfirmationTokenService;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import com.example.usermanagement.web.api.common.response.exception.CodeRuntimeException;
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
                .orElseThrow(() -> new CodeRuntimeException(ErrorsEnum.CONFIRM_TOKEN_NOT_FOUND));

        if (confirmationToken.getUser().getEmailVerifiedOn() != null) {
            throw new CodeRuntimeException(ErrorsEnum.ACCOUNT_ALREADY_CONFIRMED);
        }

        if (confirmationToken.getConfirmedAt() != null) {
            throw new CodeRuntimeException(ErrorsEnum.EMAIL_ALREADY_CONFIRMED);
        }

        Date expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.before(new Date())) {
            throw new CodeRuntimeException(ErrorsEnum.CONFIRM_TOKEN_EXPIRED);
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);

        return ResponseEntity.ok("ok"); // TODO
    }
}
