package com.example.usermanagement.business.common;

import com.example.usermanagement.business.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SecurityHelperTest {

    @Test
    void generateSecretKey() {
        String result = SecurityHelper.generateSecretKey();
        Assertions.assertTrue(result.length() == 32);
    }

    @Test
    void generate2FAQRCodeImageURL() {
        String secretKey = "testSecretKey";
        String account = "test@example.com";

        User user = new User();
        user.setDs2faSecret(secretKey);
        user.setDsEmail(account);

        String result = SecurityHelper.generate2FAQRCodeImageURL(user);
        Assertions.assertTrue(result.endsWith(
                "otpauth://totp/" +
                        "MY_APP%3Atest%40example.com" +
                        "?secret=testSecretKey" +
                        "&issuer=MY_APP"
        ));
    }
}
