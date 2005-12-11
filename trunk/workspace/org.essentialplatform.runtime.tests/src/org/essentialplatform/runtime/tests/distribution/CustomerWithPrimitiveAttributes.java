package org.essentialplatform.runtime.tests.distribution;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.Lifecycle;

@InDomain
@Lifecycle(instantiable=true)
public class CustomerWithPrimitiveAttributes {

	private int integer;
	public int getInteger() {
		return integer;
	}
	public void setInteger(int number) {
		this.integer = number;
	}

	
}
