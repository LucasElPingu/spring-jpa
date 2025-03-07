package com.aprendizado.spring_jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendizado.spring_jpa.entities.OrderItem;
import com.aprendizado.spring_jpa.entities.pk.OrderItemPK;
/*
O JpaRepo ele ja e um componente do spring então não precisa colocar a anotação no UserRepo
O JpaRepo e uma interface que permite vc instancia um obj repository qque possui operações para trabalhar
O spring data JPA ja tem uma implementatção padrão para essa interface então ela não precisa ser implementada
*/
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK>{ 

}
