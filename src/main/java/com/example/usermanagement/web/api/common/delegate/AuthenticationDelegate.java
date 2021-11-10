package com.example.usermanagement.web.api.common.delegate;

import com.example.usermanagement.business.model.RoleByUser;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.RoleByUserService;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.web.api.common.JWTUtils;
import com.example.usermanagement.web.api.v1.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
public class AuthenticationDelegate {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleByUserService roleByUserService;

    public AuthenticationResponse doAuthentication(User user, boolean loginProcessCompleted) {
        Collection<RoleByUser> rolesByUser = null;
        if (loginProcessCompleted) {
            //Updating last login date for the user
            user.setLastLoginOn(new Date());
            this.userService.save(user);
            rolesByUser = this.roleByUserService.findByUserId(user.getId());
        }

        String jwt = JWTUtils.buildJWT(user, rolesByUser, !loginProcessCompleted);

        AuthenticationResponse toRet = new AuthenticationResponse();
        toRet.setJwt(jwt);
        toRet.setMustVerify2FACode(!loginProcessCompleted);
        return toRet;
    }
}