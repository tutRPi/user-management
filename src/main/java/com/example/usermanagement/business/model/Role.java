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
@Table(name = "tbl_roles")
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
        @NamedQuery(name = "Role.findByNmId", query = "SELECT r FROM Role r WHERE r.nmId = :nmId"),
        @NamedQuery(name = "Role.findByDsName", query = "SELECT r FROM Role r WHERE r.dsName = :dsName"),
        @NamedQuery(name = "Role.findByDtCreatedOn", query = "SELECT r FROM Role r WHERE r.dtCreatedOn = :dtCreatedOn"),
        @NamedQuery(name = "Role.findByDtDeletedOn", query = "SELECT r FROM Role r WHERE r.dtDeletedOn = :dtDeletedOn")})
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "nm_id")
    private Integer nmId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ds_name")
    private String dsName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dt_created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtCreatedOn;
    @Column(name = "dt_deleted_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtDeletedOn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nmRoleId")
    private Collection<RoleByUser> usersByRoleCollection;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nmId != null ? nmId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.nmId == null && other.nmId != null) || (this.nmId != null && !this.nmId.equals(other.nmId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.usermanagement.business.security.model.Role{" +
                "nmId=" + nmId +
                ", dsName='" + dsName + '\'' +
                ", dtCreatedOn=" + dtCreatedOn +
                ", dtDeletedOn=" + dtDeletedOn +
                ", usersByRoleCollection=" + usersByRoleCollection +
                '}';
    }
}
