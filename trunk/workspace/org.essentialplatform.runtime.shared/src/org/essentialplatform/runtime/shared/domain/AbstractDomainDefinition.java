package org.essentialplatform.runtime.shared.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.runtime.client.transaction.ITransactionManager;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.osgi.framework.Bundle;

/**
 * Implementation of {@link IDomainDefinition} designed for configuration
 * by Spring, or manual instantiation.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractDomainDefinition implements IDomainDefinition {

	protected abstract Logger getLogger(); 


	////////////////////////////////////////////////////////////////////
	// Name (injected)
	////////////////////////////////////////////////////////////////////

	private String _name;
	/*
	 * @see org.essentialplatform.louis.app.IDomainDefinition#getDomainName()
	 */
	public String getName() {
		return _name;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Mandatory.  Should be the same name as those of the domain classes being
	 * registered.  For example, if using the 
	 * {@link EssentialProgModelRuntimeBuilder}, then the domain classes should
	 * be annotated using <tt>@InDomain("xxx")</tt> where <tt>"xxx"</tt> is the
	 * name provided here.
	 */
	public void setName(String name) {
		_name = name;
	}

	
	////////////////////////////////////////////////////////////////////
	// DomainBuilder (injected)
	////////////////////////////////////////////////////////////////////

	private IDomainBuilder _domainBuilder;
	/*
	 * @see org.essentialplatform.louis.app.IDomainDefinition#getDomainBuilder()
	 */
	public IDomainBuilder getDomainBuilder() {
		return _domainBuilder;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Mandatory; there is no default 
	 * (eg <tt>EssentialProgModelRuntimeBuilder</tt>) to prevent circular 
	 * dependencies between plugins.
	 * 
	 * @param domainBuilder
	 */
	public void setDomainBuilder(IDomainBuilder domainBuilder) {
		_domainBuilder = domainBuilder;
	}


	////////////////////////////////////////////////////////////////////
	// Registration
	////////////////////////////////////////////////////////////////////
	
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
	 * For client-side subclasses to call.
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
