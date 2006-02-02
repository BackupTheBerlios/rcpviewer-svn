package org.essentialplatform.runtime.shared.domain;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.runtime.client.transaction.ITransactionManager;
import org.essentialplatform.runtime.client.transaction.TransactionManager;


public abstract class AbstractDomainBootstrap implements IDomainBootstrap {

	protected abstract Logger getLogger(); 

	/**
	 * Delegates to subclass to do the registration (in {@link #doRegisterClasses()}), 
	 * but suspending the {@link TransactionManager} before it does so.
	 * 
	 * <p>
	 * The suspending of the <tt>TransactionManager</tt> is done so that 
	 * subclasses can validate their configuration, eg by checking to see if 
	 * the classes they are registering can be instantiated.
	 */
	public final void registerClasses() throws DomainBootstrapException {
		ITransactionManager transactionManager = TransactionManager.instance();

		transactionManager.suspend();
		doRegisterClasses();
		transactionManager.resume();
	}
	
	/**
	 * Subclasses should implement by simplying calling
	 * {@link #registerClass(Class)} for each class that needs to be registered
	 * (one is often enough).
	 *  
	 * <p>
	 * Called by {@link #registerClasses}, with the transaction manager 
	 * suspended.  
	 *  
	 */
	protected abstract void doRegisterClasses() throws DomainBootstrapException ;

	/**
	 * For subclasses to call.
	 * 
	 * @param javaClass
	 * @throws DomainBootstrapException
	 */
	protected final void registerClass(Class<?> javaClass) throws DomainBootstrapException {
		getLogger().info("Registering " + javaClass.getName()); //$NON-NLS-1$
		if (!(Domain.lookupAny(javaClass) != null)) {
			String msg= "Failed to register class in domain:"+javaClass.getName();
			throw new DomainBootstrapException(msg);
		}
	}
	
}
