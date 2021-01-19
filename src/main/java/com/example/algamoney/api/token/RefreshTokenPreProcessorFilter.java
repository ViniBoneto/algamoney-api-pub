package com.example.algamoney.api.token;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) /* Filtro a ser executado na mais alta ordem de prioridade (antes dos d+) 
	p/ já inserir o refresh token no request trabalhado pelos outros filtros e servlet */ 
public class RefreshTokenPreProcessorFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		String refreshToken = null;
		
		if( "/oauth/token".equalsIgnoreCase(req.getRequestURI()) && "refresh_token".equals(
				req.getParameter("grant_type")) && req.getCookies() != null )
			
/*
			for (Cookie cookie : req.getCookies()) {
				if(cookie.getName().equals("refreshToken")) {
					refreshToken = cookie.getValue();
					req = new MyServletRequestWrapper(req, refreshToken);
				}
			}
*/
		//Podemos utilizar a API de stream do Java 8 ao invés do for tradicional (é opcional, porém torna o código mais fácil de se ler).
		refreshToken = Stream.of( req.getCookies() ).filter( cookie -> "refreshToken".equals(cookie.getName()) ).findFirst()
			.map( cookie -> cookie.getValue() ).orElse( null );	
			
		req = new MyServletRequestWrapper(req, refreshToken);
		chain.doFilter(req, response);
	}

	static class MyServletRequestWrapper extends HttpServletRequestWrapper {

		private String refreshToken;
		
		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken });
			map.setLocked(true);
			
			return map;
		}
		
	}
}
