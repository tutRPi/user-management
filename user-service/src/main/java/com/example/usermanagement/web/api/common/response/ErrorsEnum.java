package com.example.usermanagement.web.api.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorsEnum {
    GENERIC_AUTH(50, HttpStatus.UNAUTHORIZED, "Authentication failed"),
    USERNAME_NOT_FOUND(51, HttpStatus.UNAUTHORIZED, "The username does not exists"),
    BAD_CREDENTIALS(52, HttpStatus.UNAUTHORIZED, "The credentials are not valid"),
    CREDENTIALS_EXPIRED(53,HttpStatus.UNAUTHORIZED,  "The credentials are expired"),
    ACCOUNT_LOCKED(54, HttpStatus.UNAUTHORIZED, "The account is locked"),
    ACCOUNT_DISABLED(55, HttpStatus.UNAUTHORIZED, "The account is disabled"),
    ACCOUNT_EXPIRED(56, HttpStatus.UNAUTHORIZED, "The account is expired"),
    GENERIC_JWT_PROCESS(57, HttpStatus.UNAUTHORIZED, "Process of JWT failed"),
    JWT_EXPIRED(58, HttpStatus.UNAUTHORIZED, "The JWT is expired"),
    JWT_MALFORMED(59, HttpStatus.UNAUTHORIZED, "The JWT is malformed"),
    JWT_SIGNATURE_INVALID(60,HttpStatus.UNAUTHORIZED,  "The signature of the JWT is not valid, token may be tampered"),
    INVALID_2FA_CODE(61, HttpStatus.UNAUTHORIZED, "The 2FA code is not valid"),
    PASSWORD_DOES_NOT_MATCH(62, HttpStatus.NOT_FOUND, "The current password does not match"),
    INVALID_USER_USER(901, HttpStatus.BAD_REQUEST,"Invalid User ID"),

    // Confirmation
    CONFIRM_TOKEN_NOT_FOUND(70, HttpStatus.BAD_REQUEST,"Token not found."),
    CONFIRM_TOKEN_EXPIRED(71, HttpStatus.BAD_REQUEST,"Token is expired."),
    ACCOUNT_ALREADY_CONFIRMED(72, HttpStatus.BAD_REQUEST,"Account is already confirmed"),
    EMAIL_ALREADY_CONFIRMED(73, HttpStatus.BAD_REQUEST,"Email is already confirmed"),

    INVALID_ARGUMENTS(80, HttpStatus.BAD_REQUEST,"Invalid arguments"),

    ;

    private int code;
    private HttpStatus httpStatus;
    private String message;

    ErrorsEnum(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
