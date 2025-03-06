package com.aprendizado.spring_jpa.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aprendizado.spring_jpa.entities.Category;
import com.aprendizado.spring_jpa.repositories.CategoryRepository;

//classe de serviços para o Category, não e obrigado da para colocar tudo resource layer, mas ai sobrecarregaria com regras de negocios.
@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	//repassara a chamada para o repository.findAll
	public List<Category> findAll(){
		return repository.findAll();
	}
	
	public Category findById(Long id) {
		/*O método findById(id) do JpaRepository retorna um Optional<Category>.
		O Optional é uma classe do Java 8 criada para evitar NullPointerException ao lidar com valores que podem ser nulos.
		O método get() do Optional retorna o objeto contido dentro dele.
		Problema: Se o Optional estiver vazio (ou seja, se não encontrar um usuário com o ID fornecido), get()
		 lança uma exceção NoSuchElementException
		 pode-se tratar esse erro usando ".orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));"
		*/
		Optional<Category>op = repository.findById(id);
		return op.get();
		
	}
}
