package com.example.usermanagement.business.dto;

import com.example.usermanagement.business.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;

@Component
@Getter
@Setter
@AllArgsConstructor
@JsonInclude
public class UserActivity implements Serializable {
    Instant timestamp;
    String userAgent;
    String ip;
    String expires;
    Long userId;
    String userEmail;
    String requestMethod;
    String url;

    public UserActivity() {
        this.timestamp = Instant.now();
    }
}
