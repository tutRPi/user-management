package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.model.PasswordResetToken;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.PasswordResetService;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.business.service.mail.EmailService;
import com.example.usermanagement.util.AppSettings;
import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.ResetPasswordRequest;
import com.example.usermanagement.web.api.v1.request.SimpleChangePasswordRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@Tag(name = "login")
public class ResetPasswordController {
    public static final String PATH = "/user/reset-password";

    @Autowired
    UserService userService;

    @Autowired
    PasswordResetService passwordResetService;

    @Autowired
    EmailService emailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MessageSource emailMessageSource;

    @Autowired
    Environment env;

    @PostMapping(path = PATH)
    public ResponseEntity<BaseResponse> sendResetPasswordLink(HttpServletRequest request, @RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {

        Optional<User> user = userService.findByEmail(resetPasswordRequest.getEmail());

        if (user.isPresent()) {
            PasswordResetToken passwordResetToken = userService.createPasswordResetTokenForUser(user.get());
            try {
                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("url", AppSettings.getAppUrl(request) + PATH + "?token=" + passwordResetToken.getToken());
                emailSender.sendMessageUsingThymeleafTemplate(user.get().getEmail(), emailMessageSource.getMessage("title.resetPassword", null, request.getLocale()), "passwordResetLink.html", templateModel);
            } catch (IOException | MessagingException e) {
                log.error(e.getLocalizedMessage()); // TODO better handling
            }
        }

        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = PATH)
    public ResponseEntity<BaseResponse> verifyResetPassword(
            HttpServletRequest request,
            @RequestParam @NotNull @Size(min = UserService.TOKEN_LENGTH, max = UserService.TOKEN_LENGTH) String token,
            @RequestBody @Valid SimpleChangePasswordRequest changePasswordRequest
    ) {

        Optional<PasswordResetToken> passwordResetToken = passwordResetService.findByToken(token);

        if (passwordResetToken.isPresent()) {
            Optional<User> userToUpdate = userService.findById(passwordResetToken.get().getUser().getId());
            if (userToUpdate.isPresent()) {
                // change password
                userToUpdate.get().setPassword(this.passwordEncoder.encode(changePasswordRequest.getPassword()));
                userService.save(userToUpdate.get());
                // send confirmation email
                try {
                    emailSender.sendMessageUsingThymeleafTemplate(userToUpdate.get().getEmail(), emailMessageSource.getMessage("title.passwordHasBeenReset", null, request.getLocale()), "confirmPasswordReset.html", new HashMap<>());
                } catch (IOException | MessagingException e) {
                    log.error(e.getLocalizedMessage()); // TODO better handling
                }
                // delete token
                passwordResetService.deleteToken(passwordResetToken.get());
            }
        }

        return ResponseEntity.ok().build();
    }
}
