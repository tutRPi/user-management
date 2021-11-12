package com.example.usermanagement.web.api.v1.controller;

import com.example.usermanagement.business.model.CustomUserDetails;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.service.UserService;
import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.ChangePasswordRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@Tag(name = "login")
public class ResetPasswordController {
    public static final String PATH = "/user/reset-password";

    @Autowired
    UserService userService;

    @PostMapping(path = PATH)
    public ResponseEntity<BaseResponse> doChangePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {

        return ResponseEntity.ok().build();
    }
}
