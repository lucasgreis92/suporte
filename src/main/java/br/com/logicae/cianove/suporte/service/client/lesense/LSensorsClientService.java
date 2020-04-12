package br.com.logicae.cianove.suporte.service.client.lesense;

import br.com.logicae.cianove.suporte.dto.RestResponsePage;
import br.com.logicae.cianove.suporte.dto.SensorsDtoV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LSensorsClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${lesense.rest.path}")
    private String lesenseRestPath;

    @Value("${report.pagination.size}")
    private String reportPaginationSize;

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

    public List<SensorsDtoV1> findByFilter( String device,
                                            Long port,
                                            LocalDateTime collectedIni,
                                            LocalDateTime collectedFim,
                                            boolean paginated) {
        if (paginated) {

            int page = 0;
            Page<SensorsDtoV1> pageCrane = findByFilter(device,
                    port,
                    collectedIni,
                    collectedFim,
                    PageRequest.of(page++,
                            Integer.valueOf(reportPaginationSize),
                            Sort.by("on")));

            List<SensorsDtoV1> retorno = new ArrayList<>(Integer.valueOf(reportPaginationSize) * pageCrane.getTotalPages());
            retorno.addAll(pageCrane.getContent());

            while(page <= pageCrane.getTotalPages()) {
                retorno.addAll(findByFilter(device,
                        port,
                        collectedIni,
                        collectedFim,
                        PageRequest.of(page++,
                                Integer.valueOf(reportPaginationSize),
                                Sort.by("on")))
                        .getContent());
            }

            return retorno;

        }
        return findByFilter(device,port,collectedIni,collectedFim);
    }

    public Page<SensorsDtoV1> findByFilter(String device,
                                           Long port,
                                           LocalDateTime collectedIni,
                                           LocalDateTime collectedFim,
                                           Pageable pageable) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(lesenseRestPath+"/rs/v1/sensors/pagination/filter")
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
//                .queryParam("sort", pageable.getSort().toString())
                .queryParam("device", device)
                .queryParam("collectedIni", collectedIni)
                .queryParam("collectedFim", collectedFim);
        if (port != null) {
            builder.queryParam("port", port);
        }

        ResponseEntity<RestResponsePage<SensorsDtoV1>> response = restTemplate
                .exchange(builder.toUriString(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<RestResponsePage<SensorsDtoV1>>() {});

        return response.getBody();
    }

}
