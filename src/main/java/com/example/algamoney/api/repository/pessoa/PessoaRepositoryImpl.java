package com.example.algamoney.api.repository.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.model.Pessoa_;
import com.example.algamoney.api.repository.filter.PessoaFilter;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Pessoa> filtrar(PessoaFilter pessoaFiltro, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);
		
		//Criar as restrições
		Predicate[] predicates = criarRestricoes(pessoaFiltro, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Pessoa> query = manager.createQuery(criteria);
		adicRestricoesPaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(pessoaFiltro));
	}

	private Long total(PessoaFilter pessoaFiltro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);
		
		Predicate[] predicates = criarRestricoes(pessoaFiltro, builder, root);
		criteria.where(predicates);
		
		criteria.select( builder.count(root) );
		
		return manager.createQuery(criteria).getSingleResult();
	}

	private void adicRestricoesPaginacao(TypedQuery<Pessoa> query, Pageable pageable) {
		int pagAtual = pageable.getPageNumber();
		int totalRegsPag = pageable.getPageSize(); 
		int primeiroRegPag = pagAtual * totalRegsPag;
		
		query.setFirstResult(primeiroRegPag);
		query.setMaxResults(totalRegsPag);
	}

	private Predicate[] criarRestricoes(PessoaFilter pessoaFiltro, CriteriaBuilder builder, Root<Pessoa> root) {
		List<Predicate> predicates = new ArrayList<>();
		
		// WHERE TOLOWER(pessoa.nome) LIKE '%nome%'
		if( !StringUtils.isEmpty(pessoaFiltro.getNome()) ) {
			predicates.add( 
					builder.like( builder.lower( root.get(Pessoa_.nome) ), "%" + pessoaFiltro.getNome().toLowerCase() + "%" )
					);
		}
		
		return predicates.toArray( new Predicate[predicates.size()] );
	}

}
