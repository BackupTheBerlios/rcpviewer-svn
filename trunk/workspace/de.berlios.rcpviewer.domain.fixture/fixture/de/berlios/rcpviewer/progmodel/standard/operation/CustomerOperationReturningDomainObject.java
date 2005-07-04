package de.berlios.rcpviewer.progmodel.standard.operation;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerOperationReturningDomainObject {

	private int orderNum;
	public Order placeOrder() {
		return new Order(++orderNum);
	}
	
	public int numberOfOrdersPlaced() {
		return orderNum;
	}
}
