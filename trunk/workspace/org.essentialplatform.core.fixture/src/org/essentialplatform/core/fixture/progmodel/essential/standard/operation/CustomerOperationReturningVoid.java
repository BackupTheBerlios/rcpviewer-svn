package org.essentialplatform.core.fixture.progmodel.essential.standard.operation;

import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerOperationReturningVoid {

	public boolean orderPlaced = false;
	public void placeOrder() {
		orderPlaced = true;
	}
}
