package com.example.algamoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
