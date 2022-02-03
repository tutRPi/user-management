package com.example.usermanagement.web.api.v1.controller.admin;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.User;
import com.example.usermanagement.business.repository.UserRepository;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import com.example.usermanagement.web.api.common.response.SuccessResponse;
import com.example.usermanagement.web.api.common.response.exception.CodeRuntimeException;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.request.LockUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_VERSION_PATH)
@RolesAllowed({SecurityRole.Names.ROLE_ADMIN})
@Tag(name = "admin")
public class LockUserAdminController {

    public static final String PATH = "/admin/lock-user";

    @Autowired
    UserRepository userRepository;

    @PostMapping(path = PATH)
    @Operation(summary = "Lock or unlock a User")
    public ResponseEntity<SuccessResponse> lockUser(@RequestBody @Valid LockUserRequest lockUserRequest) {

        Optional<User> user = userRepository.findById(lockUserRequest.getUserId());
        if (user.isEmpty()) {
            throw new CodeRuntimeException(ErrorsEnum.INVALID_USER_USER);
        }

        if (lockUserRequest.isLock()) {
            user.get().setLockedOn(Instant.now());
        } else {
            user.get().setLockedOn(null);
        }
        userRepository.save(user.get());

        SuccessResponse response = new SuccessResponse();
        return ResponseEntity.ok(response);
    }

}
