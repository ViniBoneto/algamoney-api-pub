package com.example.algamoney.api.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categRep;
	
	@GetMapping
	public /*ResponseEntity<?>*/ List<Categoria> listar() {
		return categRep.findAll();
		
//		List<Categoria> categs = categRep.findAll();

		/*	O código abaixo foi apenas p/ efeito de demonstração, caso se quisesse retornar o código
		 * 	 404 (Not Found) p/ o caso da lista estar vazia. Foi comentado, no entando, por estar  
		 * 	 conceitualmente errado: O erro 404 é mais aplicável para quando o recurso (ex: a página 
		 *   web em si) não estiver mais disponível ou não puder ser encontrado.
		 * 
		 * return !categs.isEmpty() ? ResponseEntity.ok(categs) : ResponseEntity.notFound().build(); 
		 */

		/*	O código abaixo foi apenas p/ efeito de demonstração, caso se quisesse retornar o código
		 * 	 204 (No content) p/ o caso da lista estar vazia. Foi comentado, no entando, por estar  
		 * 	 conceitualmente errado: O erro 204 é mais aplicável para quando uma atualização ou operação 
		 *   é feita no servidor, com sucesso, mas não há nada a ser passado no corpor da mensagem, pois 
		 *   não haverá qq alteração nos dados da página (Ex: Quando se atualiza um cadastro com PUT).
		 * 
		 *	return !categs.isEmpty() ? ResponseEntity.ok(categs) : ResponseEntity.noContent().build();
		 */
	}
	
	@PostMapping
	/* @ResponseStatus(HttpStatus.CREATED) */
	public /*void*/ ResponseEntity<Categoria> criar(@RequestBody Categoria categ, HttpServletResponse resp) {
		Categoria categSalva = categRep.save(categ);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(categSalva.getCodigo()).toUri();
		resp.setHeader("Location", uri.toASCIIString());
		
		/* É considerada boa prática de prog retornar o objeto dps de salvo, pois ele pode ter sido 
		 *  atualizado c/ outros atributos calculados no serv (neste caso, o ID), p/ que o usuário 
		 *  visualize. 
		 *  
		 *  Como abaixo já será criado uma resposta c/ status 201 (CREATED), a anotação de método
		 *   @ResponseStatus(HttpStatus.CREATED) pode ser comentada. 
		 */
		return ResponseEntity.created(uri).body(categSalva);
		
	}
	
	@GetMapping("/{codigo}")
	public /*Categoria*/ResponseEntity<?> buscarPorCodigo(@PathVariable Long codigo) {
		/*
		 * Categoria categEx = new Categoria(); categEx.setCodigo(codigo);
		 * 
		 * Example<Categoria> ex = Example.of(categEx);
		 * 
		 * return categRep.findOne(ex).orElse(null);
		 */
		
		/*
		* Apesar de ainda podermos utilizar o método findOne, ele se tornou trabalhoso para este caso, 
		* já que tudo que queremos, é obter o recurso através de seu código. Para isso, temos o método 
		* findById, que faz exatamente o que queremos!
		*/  
//		  return this.categRep.findById(codigo).orElse(null);

		//Implementando retorno 404 (NOT FOUND) se a categ ñ for achada
		/*
		 * Optional<Categoria> optCateg = this.categRep.findById(codigo); return
		 * optCateg.isPresent() ? ResponseEntity.ok(optCateg.get()) :
		 * ResponseEntity.notFound().build();
		 */
		
		//Implementando retorno 404 usando java.util.Optional<T>.map()
		return this.categRep.findById(codigo).map( categ -> ResponseEntity.ok(categ) )
				.orElse( ResponseEntity.notFound().build() );
	}
}
