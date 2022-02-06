package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.common.SecurityHelper;
import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.UserDataRequest;
import com.example.usermanagement.web.api.v1.response.UserAccountDataResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_ADMIN, SecurityRole.Names.ROLE_USER})
@Tag(name = "user-details")
public class UserUpdateController implements SecuredRestController {
    public static final String PATH = "/user";

    @Autowired
    UserService userService;

    @PutMapping(path = PATH)
    public ResponseEntity<UserAccountDataResponse> doUpdate(@RequestBody @Valid UserDataRequest userDataRequest) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //The only data that can be updated is the data related to the logged user
        User toUpdate = userService.findById(customUserDetails.getUser().getId()).get();
        toUpdate.setFirstName(userDataRequest.getFirstName());
        toUpdate.setLastName(userDataRequest.getLastName());

        UserAccountDataResponse response = new UserAccountDataResponse();
        response.setT2FAEnabled(toUpdate.isTwoFaEnabled());
        this.userService.save(toUpdate);

        return ResponseEntity.ok(response);
    }
}
