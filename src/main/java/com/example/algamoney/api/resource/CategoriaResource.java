package com.example.algamoney.api.resource;

//import java.net.URI;
import java.util.List;
//import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

/*@SuppressWarnings("unused")*/
/* 
 * Permissão de CORS no nível da classe: Serão permitidas requisições CORS p/ todos os métodos 
 * 	de categoria, p/ a origem http://localhost:8000. O cache de preflight (período em q uma nova 
 * 	req preflight ñ será enviada) é de 10 segs.   
 * 
 *  OBS: Infelizmente, conforme explicado na aula 6.9, as funcionalidades de CORS embutidas no Spring ainda
 *  	não trabalham bem com autenticação do Spring security oauth 2. Por isso elas não poderão ser usadas,
 *  	preferindo-se a solução alternativa mostrada na aula 6.10. Elas foram apenas mostradas por curiosidade,
 *  	para conhecimento, mas depois removidas (comentadas).  
 * */
//@CrossOrigin(maxAge = 10, origins = {"http://localhost:8000"})
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categRep;
	
	@Autowired
	private ApplicationEventPublisher pub;
	
	/* 
	 * Permissão de CORS no nível do método: Serão permitidas requisições CORS p/ listagem de
	 * 	categoria (GET http://localhost:8080/categorias), p/ a origem http://localhost:8000.
	 * 	O cache de preflight (período em q uma nova req preflight ñ será enviada) é de 10 segs.    
	 * 
	 *  OBS: Infelizmente, conforme explicado na aula 6.9, as funcionalidades de CORS embutidas no Spring ainda
	 *  	não trabalham bem com autenticação do Spring security oauth 2. Por isso elas não poderão ser usadas,
	 *  	preferindo-se a solução alternativa mostrada na aula 6.10. Elas foram apenas mostradas por curiosidade,
	 *  	para conhecimento, mas depois removidas (comentadas). 
	 * */
//	@CrossOrigin(maxAge = 10, origins = {"http://localhost:8000"})
	@GetMapping
	/*
	 * Aula 6.12: hasAuthority() verifica a permissão do usuário (user), enquanto que #oauth2.hasScope() verifica a 
	 *  permissão (escopo) do cliente (client). Ex: um usuário que tenha permissão de criar categoria, 
	 *  mas esteja usando um cliente que tem permissão (escopo) apenas de leitura, não conseguirá criar 
	 *  uma categoria neste contexto.
	 */
	@PreAuthorize(value = "hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
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
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	/* @ResponseStatus(HttpStatus.CREATED) */
	public /*void*/ ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categ, HttpServletResponse resp) {
		Categoria categSalva = categRep.save(categ);
		
		/*
		 * URI uri =
		 * ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
		 * .buildAndExpand(categSalva.getCodigo()).toUri(); resp.setHeader("Location",
		 * uri.toASCIIString());
		 */
		
		pub.publishEvent(new RecursoCriadoEvent(this, resp, categSalva.getCodigo()));
		
		/* É considerada boa prática de prog retornar o objeto dps de salvo, pois ele pode ter sido 
		 *  atualizado c/ outros atributos calculados no serv (neste caso, o ID), p/ que o usuário 
		 *  visualize. 
		 *  
		 *  Como abaixo já será criado uma resposta c/ status 201 (CREATED), a anotação de método
		 *   @ResponseStatus(HttpStatus.CREATED) pode ser comentada. 
		 */
		return ResponseEntity.status(HttpStatus.CREATED).body(categSalva);
		
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
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
