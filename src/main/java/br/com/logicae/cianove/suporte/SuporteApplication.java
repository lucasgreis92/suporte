package br.com.logicae.cianove.suporte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableScheduling
public class SuporteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuporteApplication.class, args);
	}

}
