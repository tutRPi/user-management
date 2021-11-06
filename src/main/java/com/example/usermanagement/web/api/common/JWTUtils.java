package com.example.usermanagement.web.api.common;

import com.example.usermanagement.business.common.SecurityRole;
import com.example.usermanagement.business.model.RoleByUser;
import com.example.usermanagement.business.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;

public class JWTUtils {

    public static String buildJWT(User user, Collection<RoleByUser> rolesByUser, boolean set2FACodeVerificationRole) {
        Date tokenIssuedAtDate = new Date();
        Date tokenExpirationDate = new Date(tokenIssuedAtDate.getTime() + WebSecurityConstants.JWT_EXPIRATION_MILLIS);

        Claims claims = Jwts.claims();
        claims.setSubject(user.getDsEmail())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(tokenIssuedAtDate)
                .setExpiration(tokenExpirationDate);

        List<String> securityRolesList = new ArrayList<>();

        if (set2FACodeVerificationRole) {
            //Set only 2FA_CODE_VERIFICATION_ROLE
            securityRolesList.add(SecurityRole.ROLE_2FA_CODE_VERIFICATION.getName());
        } else {
            //Set all real user security roles from db
            rolesByUser.stream().forEach(roleByUser -> {
                securityRolesList.add(roleByUser.getRole().getDsName());
            });
        }
        claims.put(WebSecurityConstants.JWT_ROLES_CLAIM_KEY, securityRolesList);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, WebSecurityConstants.JWT_SECRET)
                .compact();
    }

    public static Claims parseClaims(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(WebSecurityConstants.JWT_SECRET)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    public static List<String> getSecurityRoles(Claims claims) {
        return (List<String>) claims.get(WebSecurityConstants.JWT_ROLES_CLAIM_KEY);
    }
}
