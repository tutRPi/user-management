package com.example.usermanagement.web.api.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.usermanagement.web.api.Constants;
import com.example.usermanagement.web.api.common.WebSecurityConstants;
import com.example.usermanagement.web.api.common.delegate.JWTProcessorDelegate;
import com.example.usermanagement.web.api.common.response.BaseResponse;
import com.example.usermanagement.web.api.common.response.ErrorsEnum;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTProcessorFilter extends OncePerRequestFilter {
    @Autowired
    private JWTProcessorDelegate jwtProcessorDelegate;

    @Autowired
    private ServletContext servletContext;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String servletContextPath = this.servletContext.getContextPath();
        String header = request.getHeader(WebSecurityConstants.HEADER_STRING);
        return !request.getRequestURI().startsWith(servletContextPath + Constants.API_PATH)
                || header == null
                || !header.startsWith(WebSecurityConstants.JWT_PREFIX);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc) throws ServletException, IOException {
        String header = req.getHeader(WebSecurityConstants.HEADER_STRING);
        String jwt = header.substring(WebSecurityConstants.JWT_PREFIX.length());
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = this.jwtProcessorDelegate.buildAuthenticationFromJWT(jwt, true);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            fc.doFilter(req, res);
        } catch (AuthenticationException | JwtException e) {

            ErrorsEnum errorsEnum;
            if (e instanceof UsernameNotFoundException) {
                errorsEnum = ErrorsEnum.USERNAME_NOT_FOUND;
            } else if (e instanceof CredentialsExpiredException) {
                errorsEnum = ErrorsEnum.CREDENTIALS_EXPIRED;
            } else if (e instanceof LockedException) {
                errorsEnum = ErrorsEnum.ACCOUNT_LOCKED;
            } else if (e instanceof DisabledException) {
                errorsEnum = ErrorsEnum.ACCOUNT_DISABLED;
            } else if (e instanceof AccountExpiredException) {
                errorsEnum = ErrorsEnum.ACCOUNT_EXPIRED;
            } else if (e instanceof ExpiredJwtException) {
                errorsEnum = ErrorsEnum.JWT_EXPIRED;
            } else if (e instanceof MalformedJwtException) {
                errorsEnum = ErrorsEnum.JWT_MALFORMED;
            } else if (e instanceof SignatureException) {
                errorsEnum = ErrorsEnum.JWT_SIGNATURE_INVALID;
            } else {
                errorsEnum = ErrorsEnum.GENERIC_JWT_PROCESS;
            }

            int httpStatusCode = HttpStatus.UNAUTHORIZED.value();
            ObjectMapper jacksonObjectMapper = new ObjectMapper();
            BaseResponse toRet = new BaseResponse();
            toRet.addResponseError(errorsEnum);
            res.setStatus(httpStatusCode);
            res.setContentType("application/json");
            res.getOutputStream().write(jacksonObjectMapper.writeValueAsBytes(toRet));
        }
    }
}