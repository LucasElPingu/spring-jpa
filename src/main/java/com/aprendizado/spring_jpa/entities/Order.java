package com.aprendizado.spring_jpa.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.aprendizado.spring_jpa.entities.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_order") // A palavra Order e reservado para o DB, mudar o nome na tabela para tb_order
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // difinição de estrategia de auto incremento para o Id.
	private Long id;
	private Instant moment;
	private Integer orderStatus;

	@ManyToOne
	@JoinColumn(name = "client_id") // Essa vai ser a chave estrangeira a ser usada, vai ser criado uma coluna no DB
	// com esse nome
	private User client; // Sempre seguir o diagrama
	
	//Esta mapeando as duas entidades para ter o mesmo "id" e no caso de mapear relação 1 para 1 com msm Id e obrigatorio colocar o cascade
	/*
	 A opção cascade na anotação @OneToOne define como as operações de persistência (como salvar, atualizar, deletar) realizadas na 
	 entidade principal (Order) serão propagadas para a entidade associada (Payment).
	No seu exemplo, cascade = CascadeType.ALL significa que todas as operações de persistência realizadas em Order serão automaticamente 
	aplicadas a Payment.
	 */
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL) 
	private Payment payment;
	
	/*
	 O pedido tem vários itens. Ele e mapeado pelo id.order, pois no OrdemItem temos o Id e no Id que tem o Order
	 */
	@OneToMany(mappedBy = "id.order")
	private Set<OrderItem>items = new HashSet<>();

	public Order() {
	}

	public Order(Long id, Instant moment, OrderStatus orderStatus, User client) {
		this.id = id;
		this.moment = moment;
		this.client = client;
		setOrderStatus(orderStatus);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public User getClient() {
		return client;
	}

	public void setClient(User client) {
		this.client = client;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public OrderStatus getOrderStatus() {
		return OrderStatus.valueOf(orderStatus);
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		if (orderStatus != null) {
			this.orderStatus = orderStatus.getCode();
		}
	}

	public Set<OrderItem> getItems() {
		return items;
	}
	
	public Double getTotal() {
		double total = 0;
		for(OrderItem item : items) {
			total += item.getSubTotal();
		}
		return total;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", moment=" + moment + ", client=" + client + "]";
	}

}
