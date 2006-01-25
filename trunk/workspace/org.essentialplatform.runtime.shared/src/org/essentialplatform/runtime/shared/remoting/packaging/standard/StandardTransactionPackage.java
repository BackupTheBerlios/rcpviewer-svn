package org.essentialplatform.runtime.shared.remoting.packaging.standard;

import java.util.ArrayList;
import java.util.List;

import org.essentialplatform.runtime.shared.remoting.packaging.IPojoPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;

public final class StandardTransactionPackage implements ITransactionPackage {

	private List<IPojoPackage> _enlistedPojos = new ArrayList<IPojoPackage>();
	/**
	 * Adds a pojo (as a package) to <i>this</i> package.
	 * 
	 * @param pojoPackage
	 */
	public void addEnlistedPojo(IPojoPackage pojoPackage) {
		_enlistedPojos.add(pojoPackage);
	}

	/*
	 * @see org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage#enlistedPojos()
	 */
	public List<IPojoPackage> enlistedPojos() {
		return _enlistedPojos;
	}
	
	
	
}
