package de.berlios.rcpviewer.progmodel.standard.operation;

import de.berlios.rcpviewer.progmodel.standard.InDomain;

@InDomain
public class CustomerOperationReturningVoid {

	public boolean orderPlaced = false;
	public void placeOrder() {
		orderPlaced = true;
	}
}
