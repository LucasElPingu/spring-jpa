package com.aprendizado.spring_jpa.config;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.aprendizado.spring_jpa.entities.Category;
import com.aprendizado.spring_jpa.entities.Order;
import com.aprendizado.spring_jpa.entities.OrderItem;
import com.aprendizado.spring_jpa.entities.Product;
import com.aprendizado.spring_jpa.entities.User;
import com.aprendizado.spring_jpa.entities.enums.OrderStatus;
import com.aprendizado.spring_jpa.repositories.CategoryRepository;
import com.aprendizado.spring_jpa.repositories.OrderItemRepository;
import com.aprendizado.spring_jpa.repositories.OrderRepository;
import com.aprendizado.spring_jpa.repositories.ProductRepository;
import com.aprendizado.spring_jpa.repositories.UserRepository;

@Configuration //fala para o Spring que essa e uma classe de configuração
@Profile("test") //especifica o perfil que essa aplicação rodara, apenas quando vc estiver no perfil de teste
public class TestConfig implements CommandLineRunner{
	
	//Para fazer a injeção de independencia automaticamentet associando ao UserRepository sem precisar de construtor
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	//A interface CommandLineRunner implementa esse metodo que sera exetuado toda vez que a aplicação for iniciada
	@Override
	public void run(String... args) throws Exception {
		
		Category cat1 = new Category(null, "Electronics");
		Category cat2 = new Category(null, "Books");
		Category cat3 = new Category(null, "Computers"); 
		
		Product p1 = new Product(null, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
		Product p2 = new Product(null, "Smart TV", "Nulla eu imperdiet purus. Maecenas ante.", 2190.0, "");
		Product p3 = new Product(null, "Macbook Pro", "Nam eleifend maximus tortor, at mollis.", 1250.0, "");
		Product p4 = new Product(null, "PC Gamer", "Donec aliquet odio ac rhoncus cursus.", 1200.0, "");
		Product p5 = new Product(null, "Rails for Dummies", "Cras fringilla convallis sem vel faucibus.", 100.99, ""); 

		
		User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");
		User u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", "123456"); 
		
		Order o1 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"),OrderStatus.WAITING_PAYMENT, u1); //passando o client ele mesmo faz a associação dos objetos
		Order o2 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"),OrderStatus.PAID, u2); //na classe pedido tem um atributo do tipo User
		Order o3 = new Order(null, Instant.parse("2019-07-22T15:21:22Z"),OrderStatus.CANCELED, u1); 
		
		categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3));
		productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));
		
		//Indicar a associação das entidades
		p1.getCategories().add(cat2);
		p2.getCategories().add(cat1);
		p2.getCategories().add(cat3);
		p3.getCategories().add(cat3);
		p4.getCategories().add(cat3);
		p5.getCategories().add(cat2);
		//E necessário salvar novamente a tabela product
		productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));
		repository.saveAll(Arrays.asList(u1, u2));
		orderRepository.saveAll(Arrays.asList(o1, o2, o3));
		
		//O oi1 e do pedido 1 com o produto 1 com 2 unidades e o preço pega no p1
		//O oi2 e do pedido 1 com o produto 3 com 1 unidade e o preço pega no p3
		OrderItem oi1 = new OrderItem(o1, p1, 2, p1.getPrice());
		OrderItem oi2 = new OrderItem(o1, p3, 1, p3.getPrice());
		OrderItem oi3 = new OrderItem(o2, p3, 2, p3.getPrice());
		OrderItem oi4 = new OrderItem(o3, p5, 2, p5.getPrice());
		
		orderItemRepository.saveAll(Arrays.asList(oi1, oi2, oi3, oi4));
		
	}
}
