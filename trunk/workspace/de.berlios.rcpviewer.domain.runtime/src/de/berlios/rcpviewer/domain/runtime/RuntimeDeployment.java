package de.berlios.rcpviewer.domain.runtime;

import de.berlios.rcpviewer.domain.Deployment;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.Deployment.IAttributeBinding;
import de.berlios.rcpviewer.domain.IDomainClass.IAttribute;

/**
 * A binding of {@link IDomainClass} for the runtime environment.
 * 
 * @author Dan Haywood
 */
public final class RuntimeDeployment extends Deployment {

	/**
	 * Sets the current binding to be RUNTIME.
	 * 
	 * @throws RuntimeException if a binding has already been set.
	 */
	public RuntimeDeployment() {}

	@Override
	public <D extends Deployment> IAttributeBinding<D> getBinding(IAttribute attribute) {
		// TODO Auto-generated method stub
		return null;
	}


	// I really want it to be following (perhaps I need <Deployment super ?>  ???
//	public IAttributeBinding<RuntimeDeployment> getBinding(IAttribute attribute) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
