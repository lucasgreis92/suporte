package br.com.logicae.cianove.suporte.service;

import br.com.logicae.cianove.suporte.dto.DadosUsuarioDtoV1;
import br.com.logicae.cianove.suporte.exception.BusinessException;
import br.com.logicae.cianove.suporte.mapper.UserMapperV1;
import br.com.logicae.cianove.suporte.model.User;
import br.com.logicae.cianove.suporte.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapperV1 userMapperV1;

    public DadosUsuarioDtoV1 findByNmUsuario(String nmUsuario) throws BusinessException {
        Optional<User> user = userRepository.findByDsUser(nmUsuario);
        if (user.isPresent()){
            return userMapperV1.convertToDto(user.get());
        }
        throw new BusinessException("Usuário não encontrado");
    }
}
