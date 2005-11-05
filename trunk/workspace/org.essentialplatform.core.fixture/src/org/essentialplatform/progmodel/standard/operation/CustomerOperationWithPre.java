package org.essentialplatform.progmodel.standard.operation;

import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Prerequisites;

@InDomain
public class CustomerOperationWithPre {

	public boolean orderPlaced = false;
	public void placeOrder() {
		orderPlaced = true;
	}
	
	public boolean placeOrderVeto = false;
	public IPrerequisites placeOrderPre() {
		if (placeOrderVeto) {
			return Prerequisites.unusable();
		} else {
			return Prerequisites.none();
		}
	}

}
