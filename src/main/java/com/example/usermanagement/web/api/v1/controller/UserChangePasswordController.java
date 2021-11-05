package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_ADMIN, SecurityRole.Names.ROLE_USER})
public class UserChangePasswordController implements SecuredRestController {
    public static final String PATH = "/user/password";

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @PostMapping(path = PATH)
    public ResponseEntity<BaseResponse> doChangePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //The only data that can be updated is the data related to the logged user
        User toUpdate = userService.findById(customUserDetails.getUser().getNmId()).get();

        ResponseEntity<BaseResponse> toRet;
        if (this.passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), toUpdate.getDsPassword())) {
            toUpdate.setDsPassword(this.passwordEncoder.encode(changePasswordRequest.getPassword()));
            this.userService.save(toUpdate);
            customUserDetails.getUser().setDsPassword(toUpdate.getDsPassword());
            toRet = ResponseEntity.ok().build();
        } else {
            BaseResponse baseResponse = new BaseResponse();
            //TODO: Error Codes and rules for changing passwords (not equal or whatever)
            baseResponse.addResponseError(99, "The current password does not match.");
            toRet = ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }

        return toRet;
    }
}