package com.example.usermanagement.business.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "tbl_users")
public class User extends Auditable<String> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "first_name")
    private String firstName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "last_name")
    private String lastName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;

    @Column(name = "email_verified_on")
    private Instant emailVerifiedOn;

    @NotNull
    @Column(name = "two_fa_enabled")
    private boolean twoFaEnabled;

    @Size(min = 1, max = 100)
    @Column(name = "two_fa_secret")
    private String twoFaSecret;

    @Column(name = "last_login_on")
    private Instant lastLoginOn;

    @Column(name = "locked_on")
    private Instant lockedOn;

    @Column(name = "disabled_on")
    private Instant disabledOn;

    @Column(name = "expired_on")
    private Instant expiredOn;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<RoleByUser> rolesByUserCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<ConfirmationToken> confirmationTokensByUserCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<TwoFactorRecoveryCode> twoFactorRecoveryCodesCollection;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.usermanagement.business.security.model.User{" +
                "nmId=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", emailVerifiedOn=" + emailVerifiedOn +
                ", twoFaEnabled=" + twoFaEnabled +
                ", twoFaSecret='" + twoFaSecret + '\'' +
                ", creationDate=" + creationDate +
                ", lastLoginOn=" + lastLoginOn +
                ", lockedOn=" + lockedOn +
                ", disabledOn=" + disabledOn +
                ", expiredOn=" + expiredOn +
                ", rolesByUserCollection=" + rolesByUserCollection +
                ", confirmationTokensByUserCollection=" + confirmationTokensByUserCollection +
                '}';
    }
}
