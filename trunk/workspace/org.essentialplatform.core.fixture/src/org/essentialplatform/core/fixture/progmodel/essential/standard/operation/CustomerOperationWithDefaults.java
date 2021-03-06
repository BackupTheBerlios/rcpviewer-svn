package org.essentialplatform.core.fixture.progmodel.essential.standard.operation;

import org.essentialplatform.progmodel.essential.app.InDomain;

@InDomain
public class CustomerOperationWithDefaults {

	public Product _productDefaulted;
	public int _quantityDefaulted;

	public Product _productPlacedByOrder;
	public int _quantityPlacedByOrder;

	
	public boolean placeOrder(Product product, int quantity) {
		_productPlacedByOrder = product;
		_quantityPlacedByOrder = quantity;
		return true;
	}
	
	public void placeOrderDefaults(Product[] product, int[] quantity) {
		_productDefaulted = new Product();
		_quantityDefaulted = 25;
		product[0] = _productDefaulted;
		quantity[0] = _quantityDefaulted;
	}
	

}
