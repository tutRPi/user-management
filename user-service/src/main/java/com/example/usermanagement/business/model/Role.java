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
@Table(name = "tbl_roles")
@NamedQueries({
        @NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r"),
        @NamedQuery(name = "Role.findByName", query = "SELECT r FROM Role r WHERE r.name = :name")
})
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_on")
    private Instant createdOn;
    @Column(name = "deleted_on")
    private Instant deletedOn;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private Collection<RoleByUser> usersByRoleCollection;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Role)) {
            return false;
        }
        Role other = (Role) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.usermanagement.business.security.model.Role{" +
                ", dsName='" + name + '\'' +
                ", dtCreatedOn=" + createdOn +
                ", dtDeletedOn=" + deletedOn +
                ", usersByRoleCollection=" + usersByRoleCollection +
                '}';
    }
}
