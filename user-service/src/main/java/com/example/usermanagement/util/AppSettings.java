package com.example.usermanagement.util;

import javax.servlet.http.HttpServletRequest;

public class AppSettings {

    public static String getAppUrl(HttpServletRequest request) {
        String port = (request.getServerPort() != 80) ? ":" + request.getServerPort() : "";
        return "https://" + request.getServerName() + port + request.getContextPath();
    }
}
