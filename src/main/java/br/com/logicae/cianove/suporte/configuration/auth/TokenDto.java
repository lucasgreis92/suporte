package br.com.logicae.cianove.suporte.configuration.auth;

import java.util.Date;

public class TokenDto {

    private String token;
    private String refresh_token;
    private String type;
    private Long expiration;
    private Long actual_date = new Date().getTime();

    public TokenDto(String token, String refresh_token, String type, Long expiration) {
        this.token = token;
        this.refresh_token = refresh_token;
        this.type = type;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Long getActual_date() {
        return actual_date;
    }

    public void setActual_date(Long actual_date) {
        this.actual_date = actual_date;
    }
}
