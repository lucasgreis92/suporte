package br.com.logicae.cianove.suporte.mapper;

import br.com.logicae.cianove.suporte.dto.DadosUsuarioDtoV1;
import br.com.logicae.cianove.suporte.model.User;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserMapperV1 extends AbstractMapper<User, DadosUsuarioDtoV1> {

    @Autowired
    private ClientMapperV1 clientMapperV1;

    @Override
    public User convertToModel(DadosUsuarioDtoV1 dto, User user) {

        throw new NotImplementedException("Not implemented!");
    }

    @Override
    public DadosUsuarioDtoV1 convertToDto(User user) {
        DadosUsuarioDtoV1 dto = new DadosUsuarioDtoV1();
        dto.setCdUser(user.getCdUser());
        dto.setDsName(user.getDsName());
        dto.setDsUser(user.getDsUser());
        if (user.getClients() != null) {
            dto.setClients(new ArrayList<>());
            user.getClients().forEach( c -> {
                dto.getClients().add(clientMapperV1.convertToDto(c));
            });
        }
        if (user.getRoles() != null) {
            dto.setRoles(new ArrayList<>());
            user.getRoles().forEach( r -> {
                dto.getRoles().add(r.getDsRole());
            });
        }
        return dto;
    }
}
