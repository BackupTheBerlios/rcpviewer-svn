package org.essentialplatform.runtime.shared.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.runtime.client.transaction.ITransactionManager;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.osgi.framework.Bundle;


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
	public final void registerClasses(String domainName) throws DomainBootstrapException {
		ITransactionManager transactionManager = TransactionManager.instance();

		transactionManager.suspend();
		doRegisterClasses();
		transactionManager.resume();

		// TODO: the Domain instance should be provided.
		for(IDomainBuilder secondaryBuilder: _secondaryBuilders) {
			Domain.instance(domainName).addBuilder(secondaryBuilder); 
		}
		Domain.instance(domainName).done();	

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



	//////////////////////////////////////////////////////////////////////
	// SecondaryBuilders
	//////////////////////////////////////////////////////////////////////
	
	private List<IDomainBuilder> _secondaryBuilders = new ArrayList<IDomainBuilder>();
	public List<IDomainBuilder> getSecondaryBuilders() {
		return _secondaryBuilders;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified then no additional builders will be defined.
	 * @param secondaryBuilders
	 */
	public void setSecondaryBuilders(List<IDomainBuilder> secondaryBuilders) {
		_secondaryBuilders = secondaryBuilders;
	}
	/**
	 * For programmatic configuration.
	 * 
	 * <p>
	 * Either this method should be called (eg programmatically) to add to the
	 * existing <tt>List</tt> of {@link IDomainBuilder}s, or the
	 * {@link #setSecondaryBuilders(List)} should be called to provide a 
	 * complete new <tt>List</tt> of {@link IDomainBuilder}s.
	 * 
	 */
	public void addSecondaryBuilder(IDomainBuilder domainBuilder) {
		_secondaryBuilders.add(domainBuilder);
	}
	

	
	//////////////////////////////////////////////////////////////////////
	// Bundle
	//////////////////////////////////////////////////////////////////////

	private Bundle _bundle;
	/**
	 * Populated by owning DomainDefinition.
	 */
	public void setBundle(Bundle bundle) {
		_bundle = bundle;
	}
	public Bundle getBundle() {
		return _bundle;
	}
	

}
