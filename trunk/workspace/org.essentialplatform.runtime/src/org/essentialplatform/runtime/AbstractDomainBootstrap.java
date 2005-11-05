package org.essentialplatform.runtime;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import org.essentialplatform.core.domain.Domain;


public abstract class AbstractDomainBootstrap 
implements IDomainBootstrap {

	private String pluginId;

	/**
	 * 
	 * @param pluginId to use for Status if fail to register a class. 
	 */
	protected AbstractDomainBootstrap(final String pluginId) {
		this.pluginId = pluginId;
	}
	
	/**
	 * Subclasses should implement by simplying calling
	 * {@link #registerClass(Class)} for each class that needs to be registered
	 * (one is often enough).
	 *  
	 */
	public abstract void registerClasses() throws CoreException;

	protected final void registerClass(Class<?> javaClass) 
			throws CoreException {
		if (!register(javaClass)) {
			String msg= "Failed to register class in domain:"+javaClass.getName();
			Status status= new Status(Status.ERROR, pluginId, 0, msg, null);
			throw new CoreException(status);
		}
	}
	
	private boolean register(Class<?> javaClass) {
		return Domain.lookupAny(javaClass) != null;
	}



}
