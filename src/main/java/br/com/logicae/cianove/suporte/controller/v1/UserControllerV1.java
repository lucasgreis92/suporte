package br.com.logicae.cianove.suporte.controller.v1;


import br.com.logicae.cianove.suporte.dto.DadosUsuarioDtoV1;
import br.com.logicae.cianove.suporte.exception.BusinessException;
import br.com.logicae.cianove.suporte.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rs/v1/user")
public class UserControllerV1 {

    @Autowired
    private UserService userService;

    @GetMapping("{nmUsuario}")
    public DadosUsuarioDtoV1 findByNmUsuario(@PathVariable("nmUsuario") String nmUsuario) throws BusinessException {
        return userService.findByNmUsuario(nmUsuario);
    }

}
