package com.example.usermanagement.web.api.common;

public class WebSecurityConstants {
    //TODO: Extract this value to a property @value{} ?
    public static final String JWT_SECRET = "SecretKeyToGenJWTs";
    public static final String JWT_PREFIX = "Bearer ";
    public static final String JWT_ROLES_CLAIM_KEY = "roles";
    public static final String HEADER_STRING = "Authorization";
    public static final long JWT_EXPIRATION_MILLIS = 864000000; // 10 days
}
