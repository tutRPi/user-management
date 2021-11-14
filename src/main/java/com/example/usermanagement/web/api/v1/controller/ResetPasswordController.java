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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

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
    MessageSource messages;

    @Autowired
    Environment env;

    @PostMapping(path = PATH)
    public ResponseEntity<BaseResponse> sendResetPasswordLink(HttpServletRequest request, @RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {

        Optional<User> user = userService.findByEmail(resetPasswordRequest.getEmail());

        if (user.isPresent()) {
            String token = RandomStringUtil.getAlphaNumericString();
            userService.createPasswordResetTokenForUser(user.get(), token);
            emailSender.send(constructResetTokenEmail(request, token, user.get()));
        }

        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = PATH)
    public ResponseEntity<BaseResponse> verifyResetPassword(@RequestParam String token) {

        Optional<PasswordResetToken> passwordResetToken = passwordResetService.findByToken(token);

        if (passwordResetToken.isPresent()) {
            // TODO create subclass of ChangePasswordRequest

            // check password

            // change password

            // send confirmation email

            // delete token
        }

        return ResponseEntity.ok().build();
    }

    private SimpleMailMessage constructResetTokenEmail(HttpServletRequest request, final String token, final User user) {
        final String url = AppSettings.getAppUrl(request) + "/" + PATH + "?token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, request.getLocale());
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
