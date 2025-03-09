package com.aprendizado.spring_jpa.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.aprendizado.spring_jpa.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

//Permite que a classe intercepte exceções lançadas em qualquer controller da aplicação.
@ControllerAdvice
//captura exceções lançadas na aplicação e retorna respostas personalizadas.
public class ResourceExceptionHandler {

	//Indica que esse método trata especificamente exceções do tipo ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)
	//Esse método captura exceções **ResourceNotFoundException** e retorna um objeto **StandardError** no corpo da resposta HTTP.
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
		String error = "Resource Not Found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
}
