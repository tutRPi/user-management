package com.example.usermanagement.business.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_2fa_recovery_codes")
public class TwoFactorRecoveryCode {

    @SequenceGenerator(name = "recovery_code_sequence", sequenceName = "recovery_code_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recovery_code_sequence")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;

    @Basic(optional = false)
    @NotNull
    @Column(name = "code")
    private String code;

    @Column(name = "usedAt")
    private Instant usedAt;

}
