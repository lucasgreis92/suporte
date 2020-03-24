package br.com.logicae.cianove.suporte.model;


import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "client")
public class Client  implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "cd_client")
  private Long cdClient;

  @Column(name = "ds_client")
  private String dsClient;

  @Column(name = "ds_cpfcnpj")
  private String dsCpfcnpj;

  @ManyToMany(mappedBy = "clients")
  private List<User> users;


  public Long getCdClient() {
    return cdClient;
  }

  public void setCdClient(Long cdClient) {
    this.cdClient = cdClient;
  }

  public String getDsClient() {
    return dsClient;
  }

  public void setDsClient(String dsClient) {
    this.dsClient = dsClient;
  }


  public String getDsCpfcnpj() {
    return dsCpfcnpj;
  }

  public void setDsCpfcnpj(String dsCpfcnpj) {
    this.dsCpfcnpj = dsCpfcnpj;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  @Override
  public String getAuthority() {
    return dsCpfcnpj;
  }
}
