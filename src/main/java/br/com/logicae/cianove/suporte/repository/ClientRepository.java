package br.com.logicae.cianove.suporte.repository;


import br.com.logicae.cianove.suporte.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {


}
