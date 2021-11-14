package com.example.usermanagement.web.api.config;

import com.example.usermanagement.business.service.CustomUserDetailsService;
import com.example.usermanagement.web.api.common.filter.JWTProcessorFilter;
import com.example.usermanagement.web.api.v1.Constants;
import com.example.usermanagement.web.api.v1.controller.*;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    JWTProcessorFilter jwtProcessorFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //TODO: Adjust CORS origins here !
                registry.addMapping(Constants.API_VERSION_PATH + "/**").allowedOrigins("http://localhost:8080");
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .and()
                .antMatcher(com.example.usermanagement.web.api.Constants.API_PATH + "/**")
                .authorizeRequests()
                .antMatchers(Constants.API_VERSION_PATH + UserAuthenticationController.PATH)
                .permitAll()
                .antMatchers(Constants.API_VERSION_PATH + UserSignUpController.PATH)
                .permitAll()
                .antMatchers(Constants.API_VERSION_PATH + JWTController.PATH)
                .permitAll()
                .antMatchers(Constants.API_VERSION_PATH + UserEmailConfirmTokenController.PATH)
                .permitAll()
                .antMatchers(Constants.API_VERSION_PATH + ResetPasswordController.PATH)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtProcessorFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
