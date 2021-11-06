package com.example.usermanagement.business.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tbl_users")
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findByNmId", query = "SELECT u FROM User u WHERE u.nmId = :nmId"),
        @NamedQuery(name = "User.findByDsEmail", query = "SELECT u FROM User u WHERE u.dsEmail = :dsEmail"),
        @NamedQuery(name = "User.findByDsFirstName", query = "SELECT u FROM User u WHERE u.dsFirstName = :dsFirstName"),
        @NamedQuery(name = "User.findByDsLastName", query = "SELECT u FROM User u WHERE u.dsLastName = :dsLastName"),
        @NamedQuery(name = "User.findByDtEmailVerifiedOn", query = "SELECT u FROM User u WHERE u.dtEmailVerifiedOn = :dtEmailVerifiedOn"),
        @NamedQuery(name = "User.findByYn2faEnabled", query = "SELECT u FROM User u WHERE u.yn2faEnabled = :yn2faEnabled"),
        @NamedQuery(name = "User.findByDtCreatedOn", query = "SELECT u FROM User u WHERE u.dtCreatedOn = :dtCreatedOn"),
        @NamedQuery(name = "User.findByDtLastLoginOn", query = "SELECT u FROM User u WHERE u.dtLastLoginOn = :dtLastLoginOn"),
        @NamedQuery(name = "User.findByDtDeletedOn", query = "SELECT u FROM User u WHERE u.dtExpiredOn = :dtDeletedOn"),
        @NamedQuery(name = "User.existsByEmail", query = "SELECT (count(u.nmId) > 0) as exists FROM User u WHERE u.dsEmail = :email and u.nmId <> :userId")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nm_id")
    private Long nmId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ds_email")
    private String dsEmail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ds_first_name")
    private String dsFirstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ds_last_name")
    private String dsLastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ds_password")
    private String dsPassword;
    @Column(name = "dt_email_verified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtEmailVerifiedOn;
    @NotNull
    @Column(name = "yn_2fa_enabled")
    private boolean yn2faEnabled;
    @Size(min = 1, max = 100)
    @Column(name = "ds_2fa_secret")
    private String ds2faSecret;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dt_created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCreatedOn;
    @Column(name = "dt_last_login_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtLastLoginOn;
    @Column(name = "dt_locked_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtLockedOn;
    @Column(name = "dt_disabled_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtDisabledOn;
    @Column(name = "dt_expired_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtExpiredOn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nmUserId")
    private Collection<RoleByUser> rolesByUserCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nmUserId")
    private Collection<ConfirmationToken> confirmationTokensByUserCollection;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nmId != null ? nmId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.nmId == null && other.nmId != null) || (this.nmId != null && !this.nmId.equals(other.nmId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.usermanagement.business.security.model.User{" +
                "nmId=" + nmId +
                ", dsEmail='" + dsEmail + '\'' +
                ", dsFirstName='" + dsFirstName + '\'' +
                ", dsLastName='" + dsLastName + '\'' +
                ", dsPassword='" + dsPassword + '\'' +
                ", dtEmailVerifiedOn=" + dtEmailVerifiedOn +
                ", yn2faEnabled=" + yn2faEnabled +
                ", ds2faSecret='" + ds2faSecret + '\'' +
                ", dtCreatedOn=" + dtCreatedOn +
                ", dtLastLoginOn=" + dtLastLoginOn +
                ", dtLockedOn=" + dtLockedOn +
                ", dtDisabledOn=" + dtDisabledOn +
                ", dtExpiredOn=" + dtExpiredOn +
                ", rolesByUserCollection=" + rolesByUserCollection +
                ", confirmationTokensByUserCollection=" + confirmationTokensByUserCollection +
                '}';
    }
}
