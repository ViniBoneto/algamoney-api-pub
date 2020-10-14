package com.example.algamoney.api.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRep;
	
	@Autowired
	private LancamentoRepository lancaRep;
	
	public Lancamento salvar(@Valid Lancamento lanca) {
		/*
		 * Até cogitou-se, a princípio, usar-se o método PessoaService.buscaPessoaPorCod(codigo) p/
		 * 	se localizar a pessoa relacionada ao lançamento, porém, em caso de pessoa inválida, este método 
		 * 	retorna a exceção EmptyResultDataAccessException, que será mapeada no exception handler p/ o retorno
		 * 	404 (não encontrado). Isto poderia causar confusão e o cliente pensar que o lançamento em si é inválido,
		 * 	por isso, preferiu-se usar a interface PessoaRepository direito, p/ se o obter a pessoa e, em caso de
		 * 	pessoa inexistente ou inativa, lançar-se uma exceção própria, que informaria mais acuradamente a situação.
		 */
		@SuppressWarnings("unused")
		Pessoa pessoaValida = this.pessoaRep.findById( lanca.getPessoa().getCodigo() ).filter( (pessoa) -> !pessoa.isInativo() ).orElseThrow( PessoaInexistenteOuInativaException::new );
		
		return lancaRep.save(lanca);
	}

}
