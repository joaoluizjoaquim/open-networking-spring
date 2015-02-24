package br.com.joaojoaquim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
@Import(RepositoryRestMvcConfiguration.class)
public class Application {
		
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}