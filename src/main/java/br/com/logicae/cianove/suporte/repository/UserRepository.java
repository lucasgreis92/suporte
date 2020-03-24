package br.com.logicae.cianove.suporte.repository;

import br.com.logicae.cianove.suporte.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByDsUser(String dsUser);

}
