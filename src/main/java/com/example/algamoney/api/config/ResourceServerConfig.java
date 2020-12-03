package com.example.algamoney.api.config;

import org.springframework.context.annotation.Bean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;


/* Teoricamente desnecessário colocar a anotação @Configuration abaixo, pois a anotação @EnableWebSecurity já 
 * 	contém uma anotação @Configuration embutida em si. No entanto, o autor do tutorial optou por essa redundância, 
 *  para deixar frisado que esta é uma classe de configuração. 
 *
 *
 * Aula 6.3: Renomear cls de SecurityConfig p/ ResourceServerConfig e fazê-la estender cls ResourceServerConfigurerAdapter,
 *  em vez de WebSecurityConfigurerAdapter (c/ alterações em métodos necessárias). Isso tudo para que esta cls passe a
 *  ter o papel do Resource Server no protocolo OAuth2. Tb adicionar a anotação @EnableResourceServer.
 *  
 * Aula 6.11: Nesta classe sobrescrevemos um novo método configure para referenciar o nosso UserDetailsService, não será mais necessário. 
 *  Também iremos remover a anotação @EnableWebSecurity. 
 *  
 * Aula 6.12: Adiciona a anotação @EnableGlobalMethodSecurity p/ habilitar o Spring Security global method security. 
 */
@Configuration  
//@EnableWebSecurity
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class /* SecurityConfig */ ResourceServerConfig extends /* WebSecurityConfigurerAdapter */ ResourceServerConfigurerAdapter {

	/* @Override */
//	@Autowired
//	public /* protected */ void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
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
		 * 
		 * OBS: Na aula 6.3 vamos mudar a estratégia e usar o método OAuthSecurityConfig.passwordEncoder(), para obter o encoder,  
		 * 	em vez de passar entre chaves o ID do Encoder que desejamos utilizar. 
		 */
//				.withUser("admin")/* .password("{noop}admin") */.password("admin").roles("ROLE");
//	}
	
	@Override
	public /* protected */ void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				/*
				 * Aula 6.12: Com a adição dos controles de acesso à métodos (c/ @PreAuthorize), a expressão de permissão 
				 * 	às categorias, abaixo, até perdeu sentido e poderia ser removida, pois o acesso será controlado diretamente
				 * 	nos métodos da classe CategoriaResource. 
				 */
				.antMatchers("/categorias").permitAll() //Indica que requisições à URL de categorias será permitida a todos
				.anyRequest().authenticated() //Indica que d+ requisições a URLs serão permitidas só a usuários autenticados
				.and()
//			.httpBasic().and() //Usa autenticação HTTP Basic
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() //Não usa Sessão HTTP (requisições não terão estado)
			.csrf().disable(); //Desabilita suporte a CSRF (Cross-site request forgery)
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(true);
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
	
	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}
}
