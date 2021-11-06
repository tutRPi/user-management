package com.example.usermanagement.business.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private User user;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getDsPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getDsEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.getDtExpiredOn() == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getDtLockedOn() == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getDtDisabledOn() == null && this.user.getDtEmailVerifiedOn() != null;
    }
}
