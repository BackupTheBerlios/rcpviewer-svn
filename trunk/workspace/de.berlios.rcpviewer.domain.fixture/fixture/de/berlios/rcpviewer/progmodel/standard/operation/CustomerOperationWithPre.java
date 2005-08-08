package de.berlios.rcpviewer.progmodel.standard.operation;

import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.standard.InDomain;

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
