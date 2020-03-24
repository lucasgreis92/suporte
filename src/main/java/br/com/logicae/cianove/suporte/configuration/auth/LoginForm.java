package br.com.logicae.cianove.suporte.configuration.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginForm {

    private String usuario;
    private String senha;

    public String getSenha() {
        return senha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsernamePasswordAuthenticationToken converter() {
        return new UsernamePasswordAuthenticationToken(usuario, senha);
    }

}
