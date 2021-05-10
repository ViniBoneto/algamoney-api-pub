package com.example.algamoney.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/*
 * Aula 6.3:
 * 
 * Precisaremos criar uma nova classe de configuração para evitarmos problemas com algumas das dependências que usamos. 
 *  Sem essa classe, não será possível injetar a propriedade AuthenticationManager que está como dependência em 
 *  AuthorizationServerConfig. Por isso precisamos definir como um Bean em nossa classe de configuração. 
 *  
 *  Também teremos problema com o PasswordEncoder, que precisamos definir nessa nova versão. 
 *  
 *  PS.: NoOpPasswordEncoder está como deprecated por não ser recomendado sua utilização (pois não realiza nenhum tipo 
 *   de encriptação), porém para manter a compatibilidade com a aula, iremos usá-lo. Em aulas posteriores vamos alterar 
 *   essa parte para algo que possa ser usado em produção.
*/
//@SuppressWarnings("deprecation")
@Profile("oauth-security") // Aula 7.6: Adicionando condição p/ só instanciar obj desta cls se um dos perfis ativos do spring for oauth-security 
@Configuration
@EnableWebSecurity
public class OAuthSecurityConfig extends WebSecurityConfigurerAdapter {

	/* Com as atualizações do Spring Security, não há um Bean para AuthenticationManager que é fornecido por padrão pelo Spring, 
	 * 	para isso precisamos definir esse Bean por conta própria. Para isso, podemos sobrescrever o método authenticationManager() 
	 * 	da classe WebSecurityConfigurerAdapter */
	@Bean
	@Override
	public /* protected */ AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	/* Aula 6.11: Definiremos também aqui, o Bean do nosso passwordEncoder (BCryptPasswordEncoder), que será usado para fazer o decode da 
	 * 	senha do usuário e da secret do cliente, no lugar do NoOpPasswordEncoder */
	@Bean
	public PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
		return new BCryptPasswordEncoder();
	}
}
