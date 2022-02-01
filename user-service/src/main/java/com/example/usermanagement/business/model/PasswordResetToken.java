package com.example.usermanagement.business.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_password_reset_tokens")
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Long id;

    @Basic(optional = false)
    @NotNull
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    private User user;

    @Basic(optional = false)
    @NotNull
    @Column(name = "expires_at")
    private Instant expiresAt;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiresAt = Instant.now().plus(EXPIRATION, ChronoUnit.MINUTES);
    }

    public boolean isValid() {
        return this.getExpiresAt().isAfter(Instant.now());
    }
}
