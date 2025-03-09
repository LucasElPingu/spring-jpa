package com.aprendizado.spring_jpa.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.aprendizado.spring_jpa.entities.User;
import com.aprendizado.spring_jpa.repositories.UserRepository;
import com.aprendizado.spring_jpa.services.exceptions.DatabaseException;
import com.aprendizado.spring_jpa.services.exceptions.ResourceNotFoundException;

//classe de serviços para o User, não e obrigado da para colocar tudo resource layer, mas ai sobrecarregaria com regras de negocios.
@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	// repassara a chamada para o repository.findAll
	public List<User> findAll() {
		return repository.findAll();
	}

	public User findById(Long id) {
		/*
		 * O método findById(id) do JpaRepository retorna um Optional<User>. O Optional
		 * é uma classe do Java 8 criada para evitar NullPointerException ao lidar com
		 * valores que podem ser nulos. O método get() do Optional retorna o objeto
		 * contido dentro dele. Problema: Se o Optional estiver vazio (ou seja, se não
		 * encontrar um usuário com o ID fornecido), get() lança uma exceção
		 * NoSuchElementException pode-se tratar esse erro usando
		 * ".orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));"
		 */
		Optional<User> op = repository.findById(id);
		/*
		 * Retorna o get, caso não tenha usuário ele lança uma excessão
		 */
		return op.orElseThrow(() -> new ResourceNotFoundException(id));

	}

	public User insert(User obj) {
		return repository.save(obj); // salva o objeto no bd
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			//Trata o erro de não achar o id, no caso dele não existir
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			//trata o erro ao tentar deletar um usuario que possui pedidos
			throw new DatabaseException(e.getMessage());
		}
	}

	public User update(Long id, User obj) {
		/*
		 * getReferenceById(id): Obtém uma referência do usuário no banco sem carregar
		 * os dados imediatamente (carregamento preguiçoso, ou lazy loading)
		 */
		User entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return repository.save(entity);
	}

	// Atualizar os campos do usuário com os novos valores vindos no obj
	private void updateData(User entity, User obj) {
		entity.setName(obj.getName());
		entity.setEmail(obj.getEmail());
		entity.setPhone(obj.getPhone());
	}
}
