package com.example.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authMngr;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient("angular") //Client ID
			.secret("@ngul@r0") //Client secret
			.scopes("read", "write") //Escopo da permissão
			.authorizedGrantTypes("password") //Grant Type: Resource Owner Password Credentials
			.accessTokenValiditySeconds(1800); //Tempo p/ expiração do token: 1800 / 60 = 30 mins 
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore())
			.accessTokenConverter(accessTokenConverter())
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
}
