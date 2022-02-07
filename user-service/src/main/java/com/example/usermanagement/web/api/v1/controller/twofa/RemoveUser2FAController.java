package com.example.usermanagement.web.api.v1.controller.twofa;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.business.model.TwoFactorRecoveryCode;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import com.example.usermanagement.web.api.common.response.SuccessResponse;
import com.example.usermanagement.web.api.common.response.exception.CodeRuntimeException;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.controller.SecuredRestController;
import com.example.usermanagement.web.api.v1.request.PasswordVerificationRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_ADMIN, SecurityRole.Names.ROLE_USER})
@Tag(name = "2fa")
public class RemoveUser2FAController implements SecuredRestController {
    public static final String PATH = "/user/remove_2fa";

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @DeleteMapping(path = PATH)
    public ResponseEntity<SuccessResponse> remove2FA(@RequestBody @Valid PasswordVerificationRequest passwordVerificationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();

        if (passwordEncoder.matches(passwordVerificationRequest.getCurrentPassword(), user.getPassword())) {

            if (!user.isTwoFaEnabled()) {
                throw new CodeRuntimeException(ErrorsEnum.TWO_FACTOR_ALREADY_DISABLED);
            }

            user.setTwoFactorRecoveryCodes(new ArrayList<>());
            user.setTwoFaEnabled(false);
            user.setTwoFaSecret(null);
            this.userService.save(user);

            return ResponseEntity.ok(new SuccessResponse());
        } else {
            throw new CodeRuntimeException(ErrorsEnum.PASSWORD_DOES_NOT_MATCH);
        }


    }
}