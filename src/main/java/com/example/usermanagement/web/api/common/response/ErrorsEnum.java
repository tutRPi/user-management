package com.example.usermanagement.web.api.common.response;

import lombok.Getter;

@Getter
public enum ErrorsEnum {
    GENERIC_AUTH(50, "Authentication failed"),
    USERNAME_NOT_FOUND(51, "The username does not exists"),
    BAD_CREDENTIALS(52, "The credentials are not valid"),
    CREDENTIALS_EXPIRED(53, "The credentials are expired"),
    ACCOUNT_LOCKED(54, "The account is locked"),
    ACCOUNT_DISABLED(55, "The account is disabled"),
    ACCOUNT_EXPIRED(56, "The account is expired"),
    GENERIC_JWT_PROCESS(57, "Process of JWT failed"),
    JWT_EXPIRED(58, "The JWT is expired"),
    JWT_MALFORMED(59, "The JWT is malformed"),
    JWT_SIGNATURE_INVALID(60, "The signature of the JWT is not valid, token may be tampered"),
    INVALID_2FA_CODE(61, "The 2FA code is not valid");

    private int code;
    private String message;

    ErrorsEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
