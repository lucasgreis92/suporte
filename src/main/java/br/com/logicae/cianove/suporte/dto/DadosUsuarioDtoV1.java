package br.com.logicae.cianove.suporte.dto;

import br.com.logicae.cianove.suporte.model.RoleEnum;

import java.util.List;

public class DadosUsuarioDtoV1 {

    private Long cdUser;

    private String dsUser;

    private String dsName;

    private List<ClientDtoV1> clients;

    private List<String> roles;

    public boolean isAdmin() {
        return roles == null || roles.isEmpty() ? false : roles.stream().filter(role -> {
            return role.equalsIgnoreCase(RoleEnum.ADMIN.getRole());
        }).findFirst().isPresent();
    }

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

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public List<ClientDtoV1> getClients() {
        return clients;
    }

    public void setClients(List<ClientDtoV1> clients) {
        this.clients = clients;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
