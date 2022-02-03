package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.config.EventPublisherConfiguration;
import com.example.usermanagement.business.dto.SendMailTemplateMessage;
import com.example.usermanagement.business.model.PasswordResetToken;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.PasswordResetService;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.util.AppSettings;
import com.example.usermanagement.web.api.common.response.SuccessResponse;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.ResetPasswordRequest;
import com.example.usermanagement.web.api.v1.request.SimpleChangePasswordRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;
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
    PasswordEncoder passwordEncoder;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping(path = PATH)
    public ResponseEntity<SuccessResponse> sendResetPasswordLink(HttpServletRequest request, @RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {

        Optional<User> user = userService.findByEmail(resetPasswordRequest.getEmail());

        if (user.isPresent()) {
            PasswordResetToken passwordResetToken = userService.createPasswordResetTokenForUser(user.get());
            try {
                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("url", AppSettings.getAppUrl(request) + PATH + "?token=" + passwordResetToken.getToken());

                rabbitTemplate.convertAndSend(EventPublisherConfiguration.QUEUE_SEND_MAIL,
                        new SendMailTemplateMessage(
                                user.get().getEmail(),
                                request.getLocale().getLanguage(),
                                "title.resetPassword",
                                "passwordResetLink.html",
                                templateModel));

            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
        }

        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = PATH)
    public ResponseEntity<SuccessResponse> verifyResetPassword(
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
                    rabbitTemplate.convertAndSend(EventPublisherConfiguration.QUEUE_SEND_MAIL,
                            new SendMailTemplateMessage(
                                    userToUpdate.get().getEmail(),
                                    request.getLocale().getLanguage(),
                                    "title.passwordHasBeenReset",
                                    "confirmPasswordReset.html",
                                    new HashMap<>()
                            ));
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage());
                }
                // delete token
                passwordResetService.deleteToken(passwordResetToken.get());
            }
        }

        return ResponseEntity.ok().build();
    }
}
