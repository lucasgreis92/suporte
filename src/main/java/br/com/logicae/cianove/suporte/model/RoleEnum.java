package br.com.logicae.cianove.suporte.model;

public enum RoleEnum {
    ADMIN("ADMIN");

    RoleEnum(String role) {
        this.role = role;
    }

    private String role;

    public String getRole() {
        return role;
    }
}
