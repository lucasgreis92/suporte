package br.com.logicae.cianove.suporte.service.client.geo;

import br.com.logicae.cianove.suporte.dto.SensorsDtoV1;
import br.com.logicae.cianove.suporte.dto.TagsDtoV1;
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
public class GTagsClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${geo.rest.path}")
    private String geoRestPath;

    public List<TagsDtoV1> findByDeviceSerial(String device) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(geoRestPath + "/rs/v1/tags/" + device);

        ResponseEntity<List<TagsDtoV1>> response = restTemplate
                .exchange(builder.toUriString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<TagsDtoV1>>() {});

        return response.getBody();
    }
}
