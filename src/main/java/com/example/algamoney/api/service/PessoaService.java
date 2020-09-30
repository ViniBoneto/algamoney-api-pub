package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRep;

	public Pessoa atualizar(Long codigo, Pessoa pessoa){
		Pessoa pessoaSalva = buscaPessoaPorCod(codigo);
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
	
		return this.pessoaRep.save(pessoaSalva);	
	}

	public void atualizaPropAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = buscaPessoaPorCod(codigo);
		pessoaSalva.setAtivo(ativo);		
		this.pessoaRep.save(pessoaSalva);
	}
	
	private Pessoa buscaPessoaPorCod(Long codigo) {
		Pessoa pessoaSalva = this.pessoaRep.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1) );
		return pessoaSalva;
	}
}
