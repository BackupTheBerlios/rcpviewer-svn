package org.essentialplatform.progmodel.standard.operation;

import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class CustomerOperationReturningDomainObject {

	private int orderNum;
	public Order placeOrder() {
		++orderNum;
		return new Order(orderNum);
	}
	
	public int numberOfOrdersPlaced() {
		return orderNum;
	}
}
