package com.aprendizado.spring_jpa.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendizado.spring_jpa.entities.Category;
import com.aprendizado.spring_jpa.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService service;
	
	@GetMapping({ "", "/"}) //use expressões regulares de ou um dos dois
	public ResponseEntity<List<Category>> findAll(){
		List<Category>list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	//Responde requisições do tipo get do http
	/*@GetMapping("/") //O meu spring ta tratando o "/" no final como uma rota diferente, testei adicionando a linha
	//spring.web.mvc.pathmatch.matching-strategy=path_pattern_parser no application.properties, n deu certo
	public ResponseEntity<List<Category>> findAllWhithSlash(){
		List<Category>list = service.findAll();
		return ResponseEntity.ok().body(list);
	}*/
	
	@GetMapping(value = "/{id}") //indica que a requisição vai aceitar um id na url 
	public ResponseEntity<Category> findById(@PathVariable Long id){
		//o @PathVariable indica que o parametro vai vir pela url
		Category u = service.findById(id);
		return ResponseEntity.ok().body(u);
	}
	
}
