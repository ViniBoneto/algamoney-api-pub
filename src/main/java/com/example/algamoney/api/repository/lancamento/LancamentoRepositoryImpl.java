package com.example.algamoney.api.repository.lancamento;

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

import com.example.algamoney.api.model.Categoria_;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Lancamento_;
import com.example.algamoney.api.model.Pessoa_;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public /*List*/ Page<Lancamento> filtrar(LancamentoFilter lancaFiltro, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		//Criar as restrições
		Predicate[] predicates = criarRestricoes(lancaFiltro, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		adicRestricoesPaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancaFiltro));
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancaFiltro, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		//Cria seleção a partir da projeção
		criteria.select(builder.construct(ResumoLancamento.class, root.get(Lancamento_.codigo), root.get(Lancamento_.descricao),
					root.get(Lancamento_.dataVencimento), root.get(Lancamento_.dataPagamento), root.get(Lancamento_.valor),
					root.get(Lancamento_.tipo), root.get(Lancamento_.categoria).get(Categoria_.nome), root.get(Lancamento_.pessoa)
					.get(Pessoa_.nome)));
		
		//Criar as restrições
		Predicate[] predicates = criarRestricoes(lancaFiltro, builder, root);
		criteria.where(predicates);
		
		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		adicRestricoesPaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancaFiltro));
	}
	
	private Predicate[] criarRestricoes(LancamentoFilter lancaFiltro, CriteriaBuilder builder, Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<>();
		
		// WHERE TOLOWER(lancamento.descricao) LIKE '%descricao%'
		if( !StringUtils.isEmpty(lancaFiltro.getDescricao()) ) {
			predicates.add( 
					builder.like( builder.lower( root.get(Lancamento_.descricao) ), "%" + lancaFiltro.getDescricao().toLowerCase() + "%" )
					);
		}
		
		// [WHERE | AND] lancamento.data_vencimento >= dataVencimentoDe 
		if(lancaFiltro.getDataVencimentoDe() != null) {
			predicates.add( 
					builder.greaterThanOrEqualTo( root.get(Lancamento_.dataVencimento), lancaFiltro.getDataVencimentoDe() )
					);
		}
		
		// [WHERE | AND] lancamento.data_vencimento <= dataVencimentoAte 
		if(lancaFiltro.getDataVencimentoAte() != null) {
			predicates.add( 
					builder.lessThanOrEqualTo( root.get(Lancamento_.dataVencimento), lancaFiltro.getDataVencimentoAte() )
					);
		}		
		
		return predicates.toArray( new Predicate[predicates.size()] );
	}

	private void adicRestricoesPaginacao(TypedQuery<?> query, Pageable pageable) {
		int pagAtual = pageable.getPageNumber();
		int totalRegsPag = pageable.getPageSize(); 
		int primeiroRegPag = pagAtual * totalRegsPag;
		
		query.setFirstResult(primeiroRegPag);
		query.setMaxResults(totalRegsPag);
	}
	
	private Long total(LancamentoFilter lancaFiltro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancaFiltro, builder, root);
		criteria.where(predicates);
		
		criteria.select( builder.count(root) );
		
		return manager.createQuery(criteria).getSingleResult();
	}

	
}
