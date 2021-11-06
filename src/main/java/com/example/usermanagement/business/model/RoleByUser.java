package com.example.usermanagement.business.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tbl_user_roles")
@NamedQueries({
        @NamedQuery(name = "RoleByUser.findAll", query = "SELECT u FROM RoleByUser u"),
        @NamedQuery(name = "RoleByUser.findByNmId", query = "SELECT u FROM RoleByUser u WHERE u.nmId = :nmId"),
        @NamedQuery(name = "RoleByUser.findByUserId", query = "SELECT ur FROM RoleByUser ur WHERE ur.nmUserId.nmId = :userId")})
public class RoleByUser {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nm_id")
    private Integer nmId;
    @JoinColumn(name = "nm_user_id", referencedColumnName = "nm_id")
    @ManyToOne(optional = false)
    private User nmUserId;
    @JoinColumn(name = "role", referencedColumnName = "ds_name")
    @ManyToOne(optional = false)
    private Role role;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nmId != null ? nmId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RoleByUser)) {
            return false;
        }
        RoleByUser other = (RoleByUser) object;
        return (this.nmId != null || other.nmId == null) && (this.nmId == null || this.nmId.equals(other.nmId));
    }

    @Override
    public String toString() {
        return "com.example.usermanagement.business.security.model.RoleByUser{" +
                "nmId=" + nmId +
                ", nmUserId=" + nmUserId +
                ", role=" + role +
                '}';
    }
}
