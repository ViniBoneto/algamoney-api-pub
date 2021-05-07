package com.example.algamoney.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.UsuarioRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usrRep;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> usrOpt = usrRep.findByEmail(email);
		Usuario usr = usrOpt.orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha inválido "
				+ "( Login usuário passado : " + email + " ).") );
		
		/* Aula 7.5: substitui retorno de classe User p/ sua extensão UsuarioSistema, q armazenará tb    
		 	o obj do tipo Usuario (entidade) */
//		return new User(email, usr.getSenha(), getPermissoes(usr));
		return new UsuarioSistema(usr, getPermissoes(usr));
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usr) {
		Set<GrantedAuthority> permissoes = new HashSet<>();
		usr.getPermissoes().forEach( p -> permissoes.add( new SimpleGrantedAuthority( p.getDescricao().toUpperCase() ) ) );
		
		return permissoes;
	}

}
