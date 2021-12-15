package com.example.usermanagement.util;

import java.util.UUID;

public class RandomStringUtil {

    private RandomStringUtil() {}

    public static String getAlphaNumericString() {
        return getAlphaNumericString(64);
    }

    public static String getAlphaNumericString(int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i <= (n / 32) ; i++ ) {
            result.append(UUID.randomUUID().toString().replace("-", ""));
        }
        result = new StringBuilder(result.substring(0, n));
        return result.toString();
    }
}
