package com.example.usermanagement.business.common;

import com.example.usermanagement.business.model.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return Optional.empty();
        }

        return Optional.of(((CustomUserDetails) authentication.getPrincipal()).getUser().getId().toString()); // currently no username, therefore use id
    }

}