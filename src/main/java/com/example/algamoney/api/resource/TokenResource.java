package com.example.algamoney.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

/* Aula 6.14: Estamos usando o JWT access token, que não é armazenado em arquivo ou banco de dados, no servidor. 
 *	Logo, a maneira p/ podermos implementar o logout é a aplicação cliente (ex: Angular) apagando o access token
 *	do browser e o servidor removendo o refresh token do cookie. Isto será feito com o controlador abaixo.  
 * */ 
@RestController
@RequestMapping("/tokens")
public class TokenResource {
	
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProp; //Aula 7.2: Usa props externas de perfil spring

	@DeleteMapping("/revoke")
	public void revogar(HttpServletRequest req, HttpServletResponse resp) {
		Cookie rTokenCookie = new Cookie("refreshToken", null);
		rTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		rTokenCookie.setHttpOnly(true);
		rTokenCookie.setSecure(algamoneyApiProp.getSecurity().isEnableHttps());
		rTokenCookie.setMaxAge(0); //Valor zero remove o cookie
		
		resp.addCookie(rTokenCookie);
//		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
		resp.setStatus(HttpStatus.NO_CONTENT.value()); //Tanto faz desta forma ou como comentado acima
	}
	
}
