package com.aprendizado.spring_jpa.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aprendizado.spring_jpa.entities.User;
import com.aprendizado.spring_jpa.services.UserService;


@RestController
@RequestMapping(value = "/users")
public class UserResource {

	@Autowired
	private UserService service;
	
	@GetMapping({ "", "/"}) //use expressões regulares de ou um dos dois
	public ResponseEntity<List<User>> findAll(){
		List<User>list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	//Responde requisições do tipo get do http
	/*@GetMapping("/") //O meu spring ta tratando o "/" no final como uma rota diferente, testei adicionando a linha
	//spring.web.mvc.pathmatch.matching-strategy=path_pattern_parser no application.properties, n deu certo
	public ResponseEntity<List<User>> findAllWhithSlash(){
		List<User>list = service.findAll();
		return ResponseEntity.ok().body(list);
	}*/
	
	@GetMapping(value = "/{id}") //indica que a requisição vai aceitar um id na url 
	public ResponseEntity<User> findById(@PathVariable Long id){
		//o @PathVariable indica que o parametro vai vir pela url
		User u = service.findById(id);
		return ResponseEntity.ok().body(u);
	}
	
	/*
	Indica que este método será chamado quando uma requisição POST for feita para o endpoint correspondente (exemplo: POST /users).
	Geralmente, POST é usado para criar novos recursos no servidor.
	*/
	@PostMapping
	/*
	 O método recebe um objeto JSON no corpo da requisição (@RequestBody User obj).Esse JSON é convertido 
	 automaticamente para um objeto User pelo Spring Boot.
	*/
	public ResponseEntity<User> insert(@RequestBody User obj){
		obj = service.insert(obj);
		/*
		Gera a URL do novo usuário criado
		fromCurrentRequest() → Pega a URL base da requisição atual (/users).
		path("/{id}") → Adiciona /{id} na URL.
		buildAndExpand(obj.getId()) → Substitui {id} pelo ID real do usuário recém-criado.
		.toUri() → Converte para um objeto URI.
		*/
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		/*
		Retorna uma resposta HTTP 201 (Created), indicando que o recurso foi criado com sucesso.
		Adiciona um cabeçalho Location com a URL do novo usuário (uri).
		Retorna o próprio objeto salvo no corpo da resposta (body(obj)). 
		*/
		return ResponseEntity.created(uri).body(obj);
	}
	/*
	Define que este método será chamado quando uma requisição DELETE for feita para o endpoint /users/{id}.
	O {id} na URL representa um parâmetro dinâmico, ou seja, o ID do usuário a ser deletado.
	*/
	@DeleteMapping (value = "/{id}")
	/*
 	O **@PathVariable** Long id faz com que o ID recebido na URL seja passado como argumento para o método.
	*/
	public ResponseEntity<Void> delete(@PathVariable Long id){
			service.delete(id);
			/*
			Código HTTP 204 (No Content), indicando que a requisição foi processada com sucesso, mas não há conteúdo na resposta.
			Isso é comum para requisições DELETE, pois, uma vez excluído, não há mais dados para retornar.
			*/
			return ResponseEntity.noContent().build();
	}
	
	//@PutMapping é usada no Spring Boot para mapear requisições HTTP do tipo PUT para um método específico no controller.
	@PutMapping(value = "/{id}")
	/*
	@PathVariable Long id extrai o valor do {id} da URL e o passa como argumento para o método.
	@RequestBody User obj o corpo da requisição HTTP (JSON enviado pelo cliente) é convertido automaticamente para um objeto User. 
	*/
	public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User obj) {
		obj = service.update(id, obj);
		return ResponseEntity.ok().body(obj);
	}
}
