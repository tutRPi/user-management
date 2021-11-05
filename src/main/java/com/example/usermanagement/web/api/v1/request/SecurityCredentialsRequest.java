package com.example.usermanagement.web.api.v1.request;

public interface SecurityCredentialsRequest {
    String getPassword();
    String getPasswordConfirmation();
}