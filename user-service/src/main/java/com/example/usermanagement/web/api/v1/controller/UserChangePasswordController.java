package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import com.example.usermanagement.web.api.common.response.SuccessResponse;
import com.example.usermanagement.web.api.common.response.exception.CodeRuntimeException;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.ChangePasswordRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_ADMIN, SecurityRole.Names.ROLE_USER})
@Tag(name = "user-details")
public class UserChangePasswordController implements SecuredRestController {
    public static final String PATH = "/user/password";

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @PostMapping(path = PATH)
    public ResponseEntity<SuccessResponse> doChangePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // The only data that can be updated is the data related to the logged user
        Optional<User> optUser = userService.findById(customUserDetails.getUser().getId());
        User toUpdate;
        if (optUser.isPresent()) {
            toUpdate = optUser.get();
        } else {
            throw new CodeRuntimeException(ErrorsEnum.INVALID_USER_ID);
        }

        if (passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), toUpdate.getPassword())) {
            ResponseEntity<SuccessResponse> result;
            toUpdate.setPassword(this.passwordEncoder.encode(changePasswordRequest.getPassword()));
            userService.save(toUpdate);
            customUserDetails.getUser().setPassword(toUpdate.getPassword());
            result = ResponseEntity.ok().body(new SuccessResponse());
            return result;
        } else {
            throw new CodeRuntimeException(ErrorsEnum.PASSWORD_DOES_NOT_MATCH);
        }

    }
}