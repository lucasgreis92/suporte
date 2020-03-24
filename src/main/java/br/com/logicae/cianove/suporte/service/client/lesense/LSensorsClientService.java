package br.com.logicae.cianove.suporte.service.client.lesense;

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
public class LSensorsClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${lesense.rest.path}")
    private String lesenseRestPath;

    public List<SensorsDtoV1> findByFilter(String device,
                                           Long port,
                                           LocalDateTime collectedIni,
                                           LocalDateTime collectedFim) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(lesenseRestPath+"/rs/v1/sensors/filter")
                .queryParam("device", device)
                .queryParam("collectedIni", collectedIni)
                .queryParam("collectedFim", collectedFim);
        if (port != null) {
            builder.queryParam("port", port);
        }

        ResponseEntity<List<SensorsDtoV1>> response = restTemplate
                .exchange(builder.toUriString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<SensorsDtoV1>>() {});

        return response.getBody();
    }
}
