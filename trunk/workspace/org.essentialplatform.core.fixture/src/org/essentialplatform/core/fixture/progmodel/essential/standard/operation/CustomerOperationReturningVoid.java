package org.essentialplatform.core.fixture.progmodel.essential.standard.operation;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Prerequisites;

@InDomain
public class CustomerOperationReturningVoid {

	public boolean orderPlaced = false;
	public void placeOrder() {
		orderPlaced = true;
	}
}
