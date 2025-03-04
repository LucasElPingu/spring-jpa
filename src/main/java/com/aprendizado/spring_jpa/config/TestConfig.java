package com.aprendizado.spring_jpa.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.aprendizado.spring_jpa.entities.User;
import com.aprendizado.spring_jpa.repositories.UserRepository;

@Configuration //fala para o Spring que essa e uma classe de configuração
@Profile("test") //especifica o perfil que essa aplicação rodara, apenas quando vc estiver no perfil de teste
public class TestConfig implements CommandLineRunner{
	
	//Para fazer a injeção de independencia automaticamentet associando ao UserRepository sem precisar de construtor
	@Autowired
	private UserRepository repository;

	//A interface CommandLineRunner implementa esse metodo que sera exetuado toda vez que a aplicação for iniciada
	@Override
	public void run(String... args) throws Exception {
		User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");
		User u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", "123456"); 
		repository.saveAll(Arrays.asList(u1, u2));
	}
}
