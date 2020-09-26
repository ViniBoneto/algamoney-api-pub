package com.example.algamoney.api.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

public class RecursoCriadoEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpServletResponse resp;
	private Long codigo;
	
	public RecursoCriadoEvent(Object source, HttpServletResponse resp, Long codigo) {
		super(source);
		this.codigo = codigo;
		this.resp = resp;
	}

	public HttpServletResponse getResp() {
		return resp;
	}

	public Long getCodigo() {
		return codigo;
	}

}
