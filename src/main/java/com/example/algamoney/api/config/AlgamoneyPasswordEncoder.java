package com.example.algamoney.api.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/* Necessário criar uma classe de password encoder (ñ previsto no tutorial) e incluir no SecurityConfig, 
	p/ corrigir o erro aqui apontado: https://www.programmersought.com/article/5945623551/ 
	
	OBS: A correção do erro não será mais feita usando esta classe, mas sim da maneira indicada na aba 
		"Conteúdo de apoio", da aula 6.1 */
public class AlgamoneyPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(rawPassword.toString());
	}

}
