package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.model.PasswordResetToken;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.PasswordResetService;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.business.service.mail.EmailService;
import com.example.usermanagement.util.AppSettings;
import com.example.usermanagement.util.RandomStringUtil;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    MessageSource messages;

    @Autowired
    Environment env;

    @PostMapping(path = PATH)
    public ResponseEntity<BaseResponse> sendResetPasswordLink(HttpServletRequest request, @RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {

        Optional<User> user = userService.findByEmail(resetPasswordRequest.getEmail());

        if (user.isPresent()) {
            PasswordResetToken passwordResetToken = userService.createPasswordResetTokenForUser(user.get());
            emailSender.send(constructResetTokenEmail(request, passwordResetToken.getToken(), user.get()));
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
                emailSender.send(constructConfirmPasswordResetEmail(request, userToUpdate.get()));
                // delete token
                passwordResetService.deleteToken(passwordResetToken.get());
            }
        }

        return ResponseEntity.ok().build();
    }

    private SimpleMailMessage constructResetTokenEmail(HttpServletRequest request, final String token, final User user) {
        final String url = AppSettings.getAppUrl(request) + PATH + "?token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, request.getLocale());
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructConfirmPasswordResetEmail(HttpServletRequest request, final User user) {
        final String message = messages.getMessage("message.passwordHasBeenReset", null, request.getLocale());
        return constructEmail("Reset Password", message, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(Objects.requireNonNull(env.getProperty("support.email")));
        return email;
    }
}
