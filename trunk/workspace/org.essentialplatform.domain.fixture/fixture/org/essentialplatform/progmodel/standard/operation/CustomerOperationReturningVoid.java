package org.essentialplatform.progmodel.standard.operation;

import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.standard.InDomain;

@InDomain
public class CustomerOperationReturningVoid {

	public boolean orderPlaced = false;
	public void placeOrder() {
		orderPlaced = true;
	}
}
