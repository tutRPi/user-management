package com.example.usermanagement.web.api.v1.controller.twofa;

import com.example.usermanagement.business.common.SecurityHelper;
import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import com.example.usermanagement.web.api.common.response.exception.CodeRuntimeException;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.controller.SecuredRestController;
import com.example.usermanagement.web.api.v1.request.Add2FACodeVerificationRequest;
import com.example.usermanagement.web.api.v1.response.Add2FACodeVerificationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_ADMIN, SecurityRole.Names.ROLE_USER})
@Tag(name = "2fa")
public class AddUser2FAController implements SecuredRestController {
    public static final String PATH = "/user/add_2fa";

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(path = PATH)
    public ResponseEntity<Add2FACodeVerificationResponse> add2FA(@RequestBody @Valid Add2FACodeVerificationRequest add2FACodeVerificationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User toUpdate = customUserDetails.getUser();

        if (passwordEncoder.matches(add2FACodeVerificationRequest.getCurrentPassword(), toUpdate.getPassword())) {

            toUpdate.setTwoFaSecret(SecurityHelper.generateSecretKey());
            this.userService.save(toUpdate);

            Add2FACodeVerificationResponse response = new Add2FACodeVerificationResponse();
            response.setT2FAQRCodeImageURL(SecurityHelper.generate2FAQRCodeImageURL(toUpdate));

            return ResponseEntity.ok(response);
        } else {
            throw new CodeRuntimeException(ErrorsEnum.PASSWORD_DOES_NOT_MATCH);
        }


    }
}