package br.com.logicae.cianove.suporte.service.client.geo;

import br.com.logicae.cianove.suporte.dto.CraneDataDtoV1;
import br.com.logicae.cianove.suporte.dto.SensorsDtoV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GCraneDataClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${geo.rest.path}")
    private String geoRestPath;

    public List<CraneDataDtoV1> findByFilter( String device,
                                            Long port,
                                            LocalDateTime collectedIni,
                                            LocalDateTime collectedFim) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(geoRestPath+"/rs/v1/cranedata/filter")
                .queryParam("device", device)
                .queryParam("collectedIni", collectedIni)
                .queryParam("collectedFim", collectedFim);
        if (port != null) {
            builder.queryParam("port", port);
        }

        ResponseEntity<List<CraneDataDtoV1>> response = restTemplate
                .exchange(builder.toUriString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<CraneDataDtoV1>>() {});

        return response.getBody();
    }

}
