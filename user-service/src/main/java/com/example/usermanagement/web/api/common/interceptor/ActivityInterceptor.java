package com.example.usermanagement.web.api.common.interceptor;

import com.example.usermanagement.business.config.EventPublisherConfiguration;
import com.example.usermanagement.business.dto.UserActivity;
import com.example.usermanagement.business.model.CustomUserDetails;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ActivityInterceptor implements HandlerInterceptor {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) userAgent = request.getHeader("user-agent");
        String expires = response.getHeader("Expires");
        UserActivity activity = new UserActivity();
        activity.setIp(this.getClientIpAddress(request));
        activity.setExpires(expires);
        activity.setRequestMethod(request.getMethod());
        activity.setUrl(request.getRequestURI());

        if (userAgent != null) {
            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(userAgent);
            if (m.find()) {
                activity.setUserAgent(m.group(1));
            }
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                // when Anonymous Authentication is enabled
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken)) {
            CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            activity.setUserId(customUserDetails.getUser().getId());
            activity.setUserEmail(customUserDetails.getUser().getEmail());

            if (!activity.getUrl().contains("image") && !activity.getUrl().equals("/")) {
                rabbitTemplate.convertAndSend(EventPublisherConfiguration.QUEUE_USER_ACTIVITY_LOG, activity);
                return true;
            }
        }
        return true;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
            // we only want the client
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }

}