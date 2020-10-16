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

import org.springframework.util.StringUtils;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Lancamento_;
import com.example.algamoney.api.repository.filter.LancamentoFilter;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Lancamento> filtrar(LancamentoFilter lancaFiltro) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		//Criar as restrições
		Predicate[] predicates = criarRestricoes(lancaFiltro, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		
		return query.getResultList();
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

}