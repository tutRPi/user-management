package com.example.usermanagement.web.api.v1.controller.twofa;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.business.model.TwoFactorRecoveryCode;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.util.RandomStringUtil;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import com.example.usermanagement.web.api.common.response.exception.CodeRuntimeException;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.controller.SecuredRestController;
import com.example.usermanagement.web.api.v1.request.T2FACodeVerificationRequest;
import com.example.usermanagement.web.api.v1.response.Confirm2FACodeVerificationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_ADMIN, SecurityRole.Names.ROLE_USER})
@Tag(name = "2fa")
public class ConfirmUser2FAController implements SecuredRestController {
    public static final String PATH = "/user/confirm_2fa";

    @Autowired
    UserService userService;

    @PostMapping(path = PATH)
    public ResponseEntity<Confirm2FACodeVerificationResponse> doVerification(@RequestBody @Valid T2FACodeVerificationRequest t2FACodeVerificationRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User toUpdate = customUserDetails.getUser();

        // Verify 2fa
        Totp totp = new Totp(customUserDetails.getUser().getTwoFaSecret());
        if (totp.verify(t2FACodeVerificationRequest.getT2FACode())) {
            List<String> rawCodes = new ArrayList<>();
            List<TwoFactorRecoveryCode> codes = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                String code = RandomStringUtil.getAlphaNumericString(8).toLowerCase();
                rawCodes.add(code);
                TwoFactorRecoveryCode twoFactorRecoveryCode = new TwoFactorRecoveryCode();
                twoFactorRecoveryCode.setCode(code);
                twoFactorRecoveryCode.setUser(toUpdate);
                twoFactorRecoveryCode.setUsed(false);
                codes.add(twoFactorRecoveryCode);
            }

            toUpdate.setTwoFactorRecoveryCodesCollection(codes);
            toUpdate.setTwoFaEnabled(true);
            this.userService.save(toUpdate);

            Confirm2FACodeVerificationResponse response = new Confirm2FACodeVerificationResponse();
            response.setCodes(rawCodes);
            return ResponseEntity.ok().body(response);
        } else {
            throw new CodeRuntimeException(ErrorsEnum.INVALID_2FA_CODE);
        }
    }
}