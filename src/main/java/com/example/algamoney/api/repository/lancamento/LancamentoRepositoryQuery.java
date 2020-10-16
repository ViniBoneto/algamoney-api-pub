package com.example.algamoney.api.repository.lancamento;

import java.util.List;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.filter.LancamentoFilter;


/**
 *	LancamentoRepositoryQuery - Interface cujos implementadores farão o filtro da query de lançamentos 
 *								usando criterias JPA. A interface precisa seguir este padrão de nome, 
 *								p/ o Spring-data-jpa reconhecê-la e associá-la à interface LancamentoRepository. 
 *
 * @author vineto
 */
public interface LancamentoRepositoryQuery {

	public List<Lancamento> filtrar(LancamentoFilter lancaFiltro);
}
