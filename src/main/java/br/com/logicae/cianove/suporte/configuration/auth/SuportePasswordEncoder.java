package br.com.logicae.cianove.suporte.configuration.auth;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;

public class SuportePasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword)  {
        return Base64.getEncoder().encodeToString(rawPassword.toString().getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword)  {
        return encode(rawPassword).equals(encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
}
