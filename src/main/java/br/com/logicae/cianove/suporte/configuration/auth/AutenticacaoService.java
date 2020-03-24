package br.com.logicae.cianove.suporte.configuration.auth;

import br.com.logicae.cianove.suporte.model.User;
import br.com.logicae.cianove.suporte.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> usuario = userRepository.findByDsUser(username);
		if (usuario.isPresent()) {
			return usuario.get();
		}
		
		throw new UsernameNotFoundException("Usuário não encontrado!");
	}

}
