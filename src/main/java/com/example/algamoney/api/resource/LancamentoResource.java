package com.example.algamoney.api.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.service.LancamentoService;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancaRep;
	
	@Autowired
	private LancamentoService lancaServ;
	
	@Autowired
	private ApplicationEventPublisher pub;
	
	@Autowired
	private MessageSource msgSrc;
	
/*	@GetMapping
	public List<Lancamento> listar() {
		return lancaRep.findAll();
	}
*/
	//Mudando o handler do método GET de listar() p/ pesquisar(), p/ usar filtro de query c/ metamodel
	@GetMapping
	public List<Lancamento> pesquisar(LancamentoFilter lancaFiltro) {
		return lancaRep.filtrar(lancaFiltro);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPorCodigo(@PathVariable Long codigo) {
//		Implementando retorno 404 usando java.util.Optional<T>.map()
		return this.lancaRep.findById(codigo).map( lancamento -> ResponseEntity.ok(lancamento) )
				.orElse( ResponseEntity.notFound().build() );
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lanca, HttpServletResponse resp) {
		Lancamento lancaSalvo = this.lancaServ.salvar(lanca);
		
		this.pub.publishEvent( new RecursoCriadoEvent( this, resp, lancaSalvo.getCodigo() ) );
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancaSalvo);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		lancaRep.deleteById(codigo);
	}	
	
	/*
	 * Não devemos tratar a exceção PessoaInexistenteOuInativaException no handler geral AlgamoneyExceptionHandler,
	 * 	pois lá devemos tratar apenas as exceções relativas à aplicação como um todo. Como esta exceção é específica 
	 * 	de lançamento (na inclusão/atualização do mesmo), ela deve ser tratada por um método handler no próprio 
	 * 	controlador de lançamentos. 
	 */ 
	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String msgUsr = msgSrc.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String msgDev = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(msgUsr, msgDev));
		
		return ResponseEntity.badRequest().body(erros);
	}
}
