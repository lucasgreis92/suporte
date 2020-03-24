package br.com.logicae.cianove.suporte.model;

import javax.persistence.*;

@Entity
@Table(name = "user_client")
public class UserClient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "cd_user_client")
  private Long cdUserClient;

  @Column(name = "cd_user")
  private Long cdUser;

  @Column(name = "cd_client")
  private Long cdClient;


  public Long getCdUserClient() {
    return cdUserClient;
  }

  public void setCdUserClient(Long cdUserClient) {
    this.cdUserClient = cdUserClient;
  }

  public Long getCdUser() {
    return cdUser;
  }

  public void setCdUser(Long cdUser) {
    this.cdUser = cdUser;
  }

  public Long getCdClient() {
    return cdClient;
  }

  public void setCdClient(Long cdClient) {
    this.cdClient = cdClient;
  }
}
