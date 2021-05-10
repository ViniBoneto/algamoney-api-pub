package com.example.algamoney.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.example.algamoney.api.config.token.CustomTokenEnhancer;

@Profile("oauth-security") // Aula 7.6: Adicionando condição p/ só instanciar obj desta cls se um dos perfis ativos do spring for oauth-security
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authMngr;

	/* Aula 6.11: Anteriormente fazíamos referência à UserDetailsService em nosso ResourceServerConfig. Agora iremos trazer essa referência 
	 * 	para o AuthorizationServerConfig */
	@Autowired
	private UserDetailsService usrDetailsServ;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("angular") //Client ID
	//			.secret("@ngul@r0") //Client secret
				/* Aula 6.11: Iremos alterar a secret do nosso cliente angular para que ela seja encodada com BCrypt, para isso podemos usar a classe utilitária 
				 * 	GeradorSenha, inserindo o valor @ngul@r0 para ser encodado. */
				.secret("$2a$10$3/UlliW2Yc5bO7vknevx6eNPu0qpI.Z57oRuL2wJP0FDHtq9h2Gbe") //Client secret.
				.scopes("read", "write") //Escopo da permissão
				.authorizedGrantTypes("password", "refresh_token") /* Grant Type: Resource Owner Password Credentials.
																	Aula 6.6: Adicionado grant type p/ implementação refresh token */
				//Aula 6.12: Retorno do tempo expiração access token p/ 30 mins, p/ se poder testar permissões c/ calma
				.accessTokenValiditySeconds(1800) //Tempo p/ expiração do token: 1800 / 60 = 30 mins 
	//			.accessTokenValiditySeconds(20) //Aula 6.6: alteração tempo expiração access token p/ 20 s, p/ testar refresh token 
				.refreshTokenValiditySeconds(3600 * 24) //Aula 6.6: Refresh token expira em 24 hrs
			.and() //Aula 6.12: Além de "angular", cria um novo cliente "mobile", porém este só com escopo de leitura
				.withClient("mobile")
				.secret("$2a$10$Myhx2T/EV9gE5arz73ogseWqecxUO2ggHEKRZ7yGjMtID.ALlZxAS") //Client secret: m0b1l30
				.scopes("read")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(1800) 
				.refreshTokenValiditySeconds(3600 * 24);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		/* Aula 7.5: Adiciona um TokenEnhancerChain com um CunstomTokenEnhancer ao AuthorizationServerEndpointsConfigurer, p/ poder adicionar info c/ 
		 * 	nome usuário ao JWT Token */
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain(); 
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), (JwtAccessTokenConverter) accessTokenConverter()));
		
		endpoints.tokenStore(tokenStore())
			// Aula 7.5: Ñ há mais necessidade de se passar o AccessTokenConverter diretamente, pois ele será passado no TokenEnhancerChain  
//			.accessTokenConverter(accessTokenConverter())
			.tokenEnhancer(tokenEnhancerChain)
			.reuseRefreshTokens(false) //Qdo novo access token for requisitado p/ refresh token, um novo deste será gerado (uma app logada ñ expirará)
			/* Aula 6.11: Anteriormente fazíamos referência à UserDetailsService em nosso ResourceServerConfig. Agora iremos trazer essa referência 
			 * 	para o AuthorizationServerConfig */			
			.userDetailsService(usrDetailsServ) 
			.authenticationManager(authMngr);
	}

	@Bean
	public AccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter(); 
		accessTokenConverter.setSigningKey("algaworks");
		
		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new /*InMemoryTokenStore()*/ JwtTokenStore((JwtAccessTokenConverter) accessTokenConverter());
	}

	// Aula 7.5: Adiciona um TokenEnhancer
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}
}
