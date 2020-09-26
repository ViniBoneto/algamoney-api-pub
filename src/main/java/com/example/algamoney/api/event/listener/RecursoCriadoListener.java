package com.example.algamoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.event.RecursoCriadoEvent;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {

	@Override
	public void onApplicationEvent(RecursoCriadoEvent recCriadoEvt) {
		HttpServletResponse resp = recCriadoEvt.getResp();
		Long cod = recCriadoEvt.getCodigo();
		
		addHeaderLocation(resp, cod);
	}

	private void addHeaderLocation(HttpServletResponse resp, Long cod) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(cod).toUri();
		resp.setHeader("Location", uri.toASCIIString());
	}

}
