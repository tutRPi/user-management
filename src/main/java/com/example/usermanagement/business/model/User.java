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
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.existsByEmail", query = "SELECT (count(u.id) > 0) as exists FROM User u WHERE u.email = :email and u.id <> :userId")})
public class User implements Serializable {
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date emailVerifiedOn;
    @NotNull
    @Column(name = "two_fa_enabled")
    private boolean twoFaEnabled;
    @Size(min = 1, max = 100)
    @Column(name = "two_fa_secret")
    private String twoFaSecret;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Column(name = "last_login_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginOn;
    @Column(name = "locked_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lockedOn;
    @Column(name = "disabled_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date disabledOn;
    @Column(name = "expired_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredOn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<RoleByUser> rolesByUserCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<ConfirmationToken> confirmationTokensByUserCollection;

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
                ", createdOn=" + createdOn +
                ", lastLoginOn=" + lastLoginOn +
                ", lockedOn=" + lockedOn +
                ", disabledOn=" + disabledOn +
                ", expiredOn=" + expiredOn +
                ", rolesByUserCollection=" + rolesByUserCollection +
                ", confirmationTokensByUserCollection=" + confirmationTokensByUserCollection +
                '}';
    }
}
