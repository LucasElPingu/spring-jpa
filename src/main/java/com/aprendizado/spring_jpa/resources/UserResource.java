package com.aprendizado.spring_jpa.resources;

import java.net.URI;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
}
