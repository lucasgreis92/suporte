package br.com.logicae.cianove.suporte.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestClientConfig {


    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();


        return restTemplate;
    }
}
