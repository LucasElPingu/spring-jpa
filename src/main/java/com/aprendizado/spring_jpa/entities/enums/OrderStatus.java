package com.aprendizado.spring_jpa.entities.enums;

public enum OrderStatus { //definindo um enum chamado OrderStatus, que representa os status de um pedido

	WAITING_PAYMENT(1),
	PAID(2),
	SHIPPED(3),
	DELIVERED(4),
	CANCELED(5);
	//Cada constante do enum tem um número associado
	//Criamos um atributo privado code, que armazenará o valor do estado atual
	private int code;
	
	//O construtor do enum é privado, pois um enum não pode ser instanciado fora dele mesmo
	private OrderStatus(int code) {
		this.code=code;
	}
	//Método getter para obter o número (code) correspondente ao status
	public int getCode() {
		return code;
	}
	/*
	O método valueOf percorre todos os valores do OrderStatus usando values() (que retorna um array com todas as constantes do enum).
	Se encontrar um OrderStatus com o mesmo code, retorna esse status.Se o código for inválido, lança uma exceção (IllegalArgumentException).
	*/
	public static OrderStatus valueOf(int code) {
		for(OrderStatus value : OrderStatus.values()) {
			if(value.getCode() == code) {
				return value;
			}
		} throw new IllegalArgumentException("Valor Inválido");
	}
}