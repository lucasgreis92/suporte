package br.com.logicae.cianove.suporte.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_role")
    private Long cdRole;

    @Basic(optional = false)
    @Column(name = "ds_role")
    private String dsRole;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Long getCdRole() {
        return cdRole;
    }

    public void setCdRole(Long cdRole) {
        this.cdRole = cdRole;
    }

    public String getDsRole() {
        return dsRole;
    }

    public void setDsRole(String dsRole) {
        this.dsRole = dsRole;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
