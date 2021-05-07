package com.example.algamoney.api.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.algamoney.api.model.Usuario;

public class UsuarioSistema extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Usuario usr;

	public UsuarioSistema(Usuario usr, Collection<? extends GrantedAuthority> auths) {
		super(usr.getEmail(), usr.getSenha(), auths);
		this.usr = usr;
	}

	public Usuario getUsuario() {
		return usr;
	}
	
}
