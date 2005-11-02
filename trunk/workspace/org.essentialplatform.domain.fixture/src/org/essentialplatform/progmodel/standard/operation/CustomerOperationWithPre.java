package org.essentialplatform.progmodel.standard.operation;

import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.standard.InDomain;

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
