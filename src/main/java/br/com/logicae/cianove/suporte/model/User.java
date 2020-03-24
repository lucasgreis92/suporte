package br.com.logicae.cianove.suporte.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "cd_user")
  private Long cdUser;

  @Column(name = "ds_user")
  private String dsUser;

  @Column(name = "ds_password")
  private String dsPassword;

  @Column(name = "ds_name")
  private String dsName;

  @ManyToMany
  @JoinTable(
          name = "user_client",
          joinColumns = @JoinColumn(name = "cd_user"),
          inverseJoinColumns = @JoinColumn(name = "cd_client"))
  private List<Client> clients;


  public Long getCdUser() {
    return cdUser;
  }

  public void setCdUser(Long cdUser) {
    this.cdUser = cdUser;
  }

  public String getDsUser() {
    return dsUser;
  }

  public void setDsUser(String dsUser) {
    this.dsUser = dsUser;
  }


  public String getDsPassword() {
    return dsPassword;
  }

  public void setDsPassword(String dsPassword) {
    this.dsPassword = dsPassword;
  }


  public String getDsName() {
    return dsName;
  }

  public void setDsName(String dsName) {
    this.dsName = dsName;
  }

  public List<Client> getClients() {
    return clients;
  }

  public void setClients(List<Client> clients) {
    this.clients = clients;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return clients;
  }

  @Override
  public String getPassword() {
    return dsPassword;
  }

  @Override
  public String getUsername() {
    return dsUser;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
