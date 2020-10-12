package com.example.algamoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	LancamentoRepository lancaRep;
	
	@Autowired
	private ApplicationEventPublisher pub;
	
	@GetMapping
	public List<Lancamento> listar() {
		return lancaRep.findAll();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPorCodigo(@PathVariable Long codigo) {
//		Implementando retorno 404 usando java.util.Optional<T>.map()
		return this.lancaRep.findById(codigo).map( lancamento -> ResponseEntity.ok(lancamento) )
				.orElse( ResponseEntity.notFound().build() );
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lanca, HttpServletResponse resp) {
		Lancamento lancaSalvo = this.lancaRep.save(lanca);
		
		this.pub.publishEvent( new RecursoCriadoEvent( this, resp, lancaSalvo.getCodigo() ) );
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancaSalvo);
	}
}
