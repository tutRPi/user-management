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
import com.example.usermanagement.web.api.v1.request.ResetTwoFactorRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_2FA_CODE_VERIFICATION})
@Tag(name = "2fa")
public class ResetUser2FAController implements SecuredRestController {
    public static final String PATH = "/user/reset_2fa";

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @DeleteMapping(path = PATH)
    @Transactional
    public ResponseEntity<SuccessResponse> reset2FA(@RequestBody @Valid ResetTwoFactorRequest resetTwoFactorRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // The only data that can be updated is the data related to the logged user
        Optional<User> optUser = userService.findById(customUserDetails.getUser().getId());
        User user;
        if (optUser.isPresent()) {
            user = optUser.get();
        } else {
            throw new CodeRuntimeException(ErrorsEnum.INVALID_USER_ID);
        }

        if (!user.isTwoFaEnabled()) {
            throw new CodeRuntimeException(ErrorsEnum.TWO_FACTOR_ALREADY_DISABLED);
        }

        Collection<TwoFactorRecoveryCode> recoveryCodes = user.getTwoFactorRecoveryCodes();
        for (TwoFactorRecoveryCode recoveryCode : recoveryCodes) {
            if (recoveryCode.getCode().equals(resetTwoFactorRequest.getCode())) {
                if (recoveryCode.getUsedAt() == null) {

                    recoveryCode.setUsedAt(Instant.now());
                    user.setTwoFaEnabled(false);
                    userService.save(user);

                    return ResponseEntity.ok(new SuccessResponse());
                } else {
                    // recovery code already used
                    throw new CodeRuntimeException(ErrorsEnum.RECOVERY_CODE_USED);
                }
            }
        }
        throw new CodeRuntimeException(ErrorsEnum.INVALID_RECOVERY_CODE);
    }
}