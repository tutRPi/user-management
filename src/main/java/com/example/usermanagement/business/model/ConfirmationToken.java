package com.example.usermanagement.business.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_confirmation_tokens")
@NamedQueries({
        @NamedQuery(name = "ConfirmationToken.findAll", query = "SELECT c FROM ConfirmationToken c"),
        @NamedQuery(name = "ConfirmationToken.findByNmId", query = "SELECT c FROM ConfirmationToken c WHERE c.nmId = :nmId"),
        @NamedQuery(name = "ConfirmationToken.findByDsToken", query = "SELECT c FROM ConfirmationToken c WHERE c.dsToken = :dsToken"),
        @NamedQuery(name = "ConfirmationToken.findByUserId", query = "SELECT c FROM ConfirmationToken c WHERE c.nmUserId.nmId = :userId")})
public class ConfirmationToken {

    @SequenceGenerator(name = "confirmation_token_sequence", sequenceName = "confirmation_token_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirmation_token_sequence")
    @Basic(optional = false)
    @Column(name = "nm_id")
    private Long nmId;

    @NotNull
    @Column(nullable = false)
    private String dsToken;

    @JoinColumn(name = "nm_user_id", referencedColumnName = "nm_id")
    @ManyToOne(optional = false)
    private User nmUserId;

    @Basic(optional = false)
    @NotNull
    @Column(name = "dt_created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCreatedOn;

    @Basic(optional = false)
    @NotNull
    @Column(name = "dt_expires_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtExpiresAt;

    @Column(name = "dt_confirmed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtConfirmedAt;

}
