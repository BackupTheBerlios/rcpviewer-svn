package org.essentialplatform.core.fixture.progmodel.essential.standard.operation;

import org.essentialplatform.progmodel.essential.app.InDomain;

/**
 * Returned by {@link CustomerOperationReturningDomainObject#placeOrder()}.
 * 
 * @author Dan Haywood
 *
 */
@InDomain
public class Order {

	public Order(final int num) {
		this.num = num;
	}
	private final int num;
	public int getNum() {
		return num;
	}
}
