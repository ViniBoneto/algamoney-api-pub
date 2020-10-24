package com.example.algamoney.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;

/* Teoricamente desnecessário colocar a anotação @Configuration abaixo, pois a anotação @EnableWebSecurity já 
 * 	contém uma anotação @Configuration embutida em si. No entanto, o autor do tutorial optou por essa redundância, 
 *  para deixar frisado que esta é uma classe de configuração. 
 */
@Configuration  
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		/* Necessário incluir um password encoder (ñ previsto no tutorial) p/ corrigir o erro aqui apontado:
		 	https://www.programmersought.com/article/5945623551/ 
		 	
		 	OBS: A correção do erro não será mais feita usando a classe AlgamoneyPasswordEncoder, mas sim da maneira indicada na aba 
				"Conteúdo de apoio", da aula 6.1 
		*/
//			.passwordEncoder(new AlgamoneyPasswordEncoder())
		/* Como estamos declarando a senha de usuário como texto puro (admin), não precisamos de decodificá-la, então precisamos 
		 * declarar o NoOpPasswordEncoder, e temos duas formas de fazer isso (1ª forma):
		 * 
		 * Para este caso, podemos passar entre chaves o ID do Encoder que desejamos utilizar, como a senha não está criptografada, 
		 * vamos utilizar o {noop}. Caso nossa senha estivesse criptografa com BCrypt (por exemplo), poderíamos utilizar {bcrypt}
		 */
			.withUser("admin").password("{noop}admin").roles("ROLE");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/categorias").permitAll() //Indica que requisições à URL de categorias será permitida a todos
				.anyRequest().authenticated() //Indica que d+ requisições a URLs serão permitidas só a usuários autenticados
				.and()
			.httpBasic().and() //Usa autenticação HTTP Basic
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() //Não usa Sessão HTTP (requisições não terão estado)
			.csrf().disable(); //Desabilita suporte a CSRF (Cross-site request forgery)
	}
	
	/*
	 * Como estamos declarando a senha de usuário como texto puro (admin), não precisamos de decodificá-la, então precisamos declarar o 
	 * NoOpPasswordEncoder, e temos duas formas de fazer isso (2ª forma):
	 * 
	 * Adicionando informações dos usuários, através de Bean Para este caso, criamos
	 * as informações de usuário, criando um Bean UserDetailsService, que já nos
	 * devolve uma instância com o PasswordEncoder padrão.
	 * 
	 * Nota-se que este método está depreciado, pois não é algo considerado seguro,
	 * apenas para demonstrações, e nas próximas aulas vamos fazer da forma segura!
	 */
/*	@Bean
	public UserDetailsService userDetailsService() {
		@SuppressWarnings("deprecation")
		User.UserBuilder builder = User.withDefaultPasswordEncoder();
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(builder.username("admin").password("admin").roles("ROLE").build());
		
		return manager;
	} */
}
