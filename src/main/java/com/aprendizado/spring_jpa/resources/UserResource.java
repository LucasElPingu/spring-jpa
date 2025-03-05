package com.aprendizado.spring_jpa.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendizado.spring_jpa.entities.User;
import com.aprendizado.spring_jpa.services.UserService;
//OBS SEMPRE LEMBRAR DE FINALIZAR E EXECUTAR DNV NO MEU O RESTART FALHOU UMA VEZ
//Indica que e um rest controlle no caso o que faz a comunicação entre interface e bd
import org.springframework.web.bind.annotation.RequestParam;

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
	/*@GetMapping("/") //O meu spring ta trando o "/" no final como uma rota diferente, testei adicionando a linha
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
	
}
