package com.example.algamoney.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

	@Autowired
	private AlgamoneyApiProperty algamoneyApiProp; //Aula 7.2: Usa props externas de perfil spring
	
//	private String origemPermitida = algamoneyApiProp.getOrigemPermitida(); <= Deu erro de null pointer!!!
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String origemPermitida = algamoneyApiProp.getOrigemPermitida(); //Mudei p/ aqui e correu ok!
		
		//Estes headers ficam fora da condição pois deverão constar em todas respostas
		resp.setHeader("Access-Control-Allow-Origin", origemPermitida);
		resp.setHeader("Access-Control-Allow-Credentials", "true"); //Necessário p/ enviar cookie c/ refresh token
		
		if( "OPTIONS".equals( req.getMethod() ) && origemPermitida.equals( req.getHeader("Origin") ) ) {
			resp.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
			resp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			resp.setHeader("Access-Control-Max-Age", "3600"); //Duração cache preflight request: 1 hr 
			
			resp.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			chain.doFilter(request, response);
		}
	}

}
