package com.aprendizado.spring_jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendizado.spring_jpa.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
