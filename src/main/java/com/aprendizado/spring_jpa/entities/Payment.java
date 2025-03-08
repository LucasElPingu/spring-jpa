package com.aprendizado.spring_jpa.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_payment")
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //difinição de estrategia de auto incremento para o Id.
	private Long id;
	private Instant moments;
	
	@JsonIgnore
	@OneToOne
	/*
	 Diz ao JPA que a chave primária da entidade atual deve ser a mesma que a chave primária da entidade Order.
	  Isso é útil quando você deseja que a entidade atual compartilhe a mesma chave primária que a entidade Order.
	*/
	@MapsId
	private Order order;
	
	public Payment() {
	}

	public Payment(Long id, Instant moments, Order order) {
		this.id = id;
		this.moments = moments;
		this.order = order;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getMoments() {
		return moments;
	}

	public void setMoments(Instant moments) {
		this.moments = moments;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
