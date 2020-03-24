package br.com.logicae.cianove.suporte.controller.v1;

import br.com.logicae.cianove.suporte.configuration.auth.LoginForm;
import br.com.logicae.cianove.suporte.configuration.auth.TokenDto;
import br.com.logicae.cianove.suporte.configuration.auth.TokenService;
import br.com.logicae.cianove.suporte.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public TokenDto autenticar(@RequestBody @Valid LoginForm form) throws BusinessException {
        UsernamePasswordAuthenticationToken dadosLogin = form.converter();
        try {
            Authentication authentication = authManager.authenticate(dadosLogin);
            return tokenService.gerarToken(authentication);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Erro na autenticação",e);
        } catch (Exception ex) {
            throw new BusinessException("Usuário o senha inválidos", ex);
        }
    }

}
