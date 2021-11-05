package com.example.usermanagement.business.common;

import com.example.usermanagement.business.model.User;
import org.apache.commons.codec.binary.Base32;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class SecurityHelper {
    //TODO: Externalize
    private static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";

    public static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public static String generate2FAQRCodeImageURL(User user) {
        return QR_PREFIX + getGoogleAuthenticatorBarCode(user.getDs2faSecret(), user.getDsEmail(), "MY_APP");
    }

    private static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
        return "otpauth://totp/"
                + URLEncoder.encode(issuer + ":" + account, StandardCharsets.UTF_8).replace("+", "%20")
                + "?secret=" + URLEncoder.encode(secretKey, StandardCharsets.UTF_8).replace("+", "%20")
                + "&issuer=" + URLEncoder.encode(issuer, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
