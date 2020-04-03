package br.com.logicae.cianove.suporte.mapper;

import br.com.logicae.cianove.suporte.dto.ClientDtoV1;
import br.com.logicae.cianove.suporte.model.Client;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class ClientMapperV1 extends AbstractMapper<Client, ClientDtoV1> {

    @Override
    public Client convertToModel(ClientDtoV1 dto, Client model) {

        throw new NotImplementedException("Not implemented!");
    }

    @Override
    public ClientDtoV1 convertToDto(Client model) {
        ClientDtoV1 dto = new ClientDtoV1();
        dto.setCdClient(model.getCdClient());
        dto.setDsClient(model.getDsClient());
        dto.setDsCpfcnpj(model.getDsCpfcnpj());
        return dto;
    }
}
