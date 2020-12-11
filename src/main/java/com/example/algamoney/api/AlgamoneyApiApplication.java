package com.example.algamoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyApiProperty.class)
public class AlgamoneyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlgamoneyApiApplication.class, args);
	}

	/* 
	 * Permissão de CORS no nível da aplicação: Serão permitidas requisições CORS p/ os métodos GET, 
	 * 	HEAD, e POST, p/ a origem http://localhost:8000, em toda aplicação. O cache de preflight (período 
	 * 	em q uma nova req preflight ñ será enviada) é de 30 mins.
	 * 
	 *  OBS: Infelizmente, conforme explicado na aula 6.9, as funcionalidades de CORS embutidas no Spring ainda
	 *  	não trabalham bem com autenticação do Spring security oauth 2. Por isso elas não poderão ser usadas,
	 *  	preferindo-se a solução alternativa mostrada na aula 6.10. Elas foram apenas mostradas por curiosidade,
	 *  	para conhecimento, mas depois removidas (comentadas). 
	 * */	
/*	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/categorias").allowedOrigins("http://localhost:8000");
			}
			
		};
	}
*/	
}
