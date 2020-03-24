package br.com.logicae.cianove.suporte.configuration.auth;

import java.time.LocalDateTime;
import java.util.List;

public class JwtDataDto {

    private Long cdUser;
    private String dsUser;
    private String dsName;
    private LocalDateTime dtGenerated;

    private List<String> roles;

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

    public LocalDateTime getDtGenerated() {
        return dtGenerated;
    }

    public void setDtGenerated(LocalDateTime dtGenerated) {
        this.dtGenerated = dtGenerated;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
