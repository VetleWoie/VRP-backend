package no.sintef.vrp.vrp_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VrpBackendApplication {

	//http://localhost:8080/swagger-ui/index.html
	public static void main(String[] args) {
		SpringApplication.run(VrpBackendApplication.class, args);
	}

}
