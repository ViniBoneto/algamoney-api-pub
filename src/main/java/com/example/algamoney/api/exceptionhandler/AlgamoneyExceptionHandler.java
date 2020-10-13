package com.example.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource msgSrc;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String msgUsr = msgSrc.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
//		String msgDev = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		
		//Usaremos o mesmo código utilizado na aula, porém fica uma dica para melhorar a legibilidade: Optional!
		String msgDev = Optional.ofNullable( ex.getCause() ).orElse( ex ).toString();
		List<Erro> erros = Arrays.asList(new Erro(msgUsr, msgDev));
		
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Erro> erros = criarListaErros(ex.getBindingResult());
		
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ EmptyResultDataAccessException.class })
	/* @ResponseStatus(code = HttpStatus.NOT_FOUND) */
	public /*void*/ ResponseEntity<Object> handleEmptyResultDataAccessException( EmptyResultDataAccessException ex, WebRequest req ) {
		String msgUsr = msgSrc.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
		String msgDev = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(msgUsr, msgDev));
		
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
	}
	
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleDataIntegrityViolationException( DataIntegrityViolationException ex, WebRequest req ) {
		String msgUsr = msgSrc.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale());
//		String msgDev = ex.toString();
		String msgDev = ExceptionUtils.getRootCauseMessage(ex);
		List<Erro> erros = Arrays.asList(new Erro(msgUsr, msgDev));
		
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, req);
	}
	
	private List<Erro> criarListaErros(BindingResult bindRes) {
		List<Erro> erros = new ArrayList<>();
	
		String msgUsr;
		String msgDev;
		
		for (FieldError fldError : bindRes.getFieldErrors()) {
			msgDev = fldError.toString();
			msgUsr = msgSrc.getMessage(fldError, LocaleContextHolder.getLocale());
			erros.add(new Erro(msgUsr, msgDev));
		}
		
		return erros;
	}

	public static class Erro {

		private String mensagemUsuario;
		private String mensagemDesenvolvedor;
		
		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}
		
		public String getMensagemUsuario() {
			return mensagemUsuario;
		}
		
		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}
		
	}
}
