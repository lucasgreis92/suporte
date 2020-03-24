package br.com.logicae.cianove.suporte.service;

import br.com.logicae.cianove.suporte.exception.BusinessException;
import br.com.logicae.cianove.suporte.model.User;
import br.com.logicae.cianove.suporte.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

/*    public User login(String dsUser, String dsPassword) throws BusinessException {
        User user = validateUser(dsUser,dsPassword);

        return user;
    }

    public User validateUser(String dsUser, String dsPassword) throws BusinessException {
        Optional<User> user = userRepository.findByDsUser(dsUser);
        if (!user.isPresent()) {
            throw new BusinessException("Usuário não encontrado");
        }

        String passwordBase = new String(Base64.getDecoder().decode(user.get().getDsPassword()));
        if (!passwordBase.equals(dsPassword)) {
            throw new BusinessException("senha incorreta");
        }
        return user.get();
    }*/

}
