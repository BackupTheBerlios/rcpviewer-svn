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
	// Name
	////////////////////////////////////////////////////////////////////

	private String _name;
	/*
	 * @see org.essentialplatform.louis.app.IDomainDefinition#getName()
	 */
	public String getDomainName() {
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
	// DomainBuilder
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
	 * dependencies.  However, the LouisDefinition may well define transitively.
	 * 
	 * @param domainBuilder
	 */
	public void setDomainBuilder(IDomainBuilder domainBuilder) {
		_domainBuilder = domainBuilder;
	}


	////////////////////////////////////////////////////////////////////
	// DomainRegistrar
	////////////////////////////////////////////////////////////////////

	private IDomainRegistrar _domainRegistrar;
	public IDomainRegistrar getDomainRegistrar() {
		return _domainRegistrar;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param domainRegistrar
	 */
	public void setDomainRegistrar(IDomainRegistrar domainRegistrar) {
		_domainRegistrar = domainRegistrar;
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
		_domainRegistrar.registerClass(javaClass);
	}


	
	////////////////////////////////////////////////////////////////////
	// Bundle
	////////////////////////////////////////////////////////////////////
	
	private Bundle _bundle;
	/**
	 * The (Eclipse) bundle representing the domain plugin.
	 * 
	 * <p>
	 * Set by Essential itself (rather than through Spring, say), primarily
	 * to assist the {@link IDomainRegistrar} in the verification of 
	 * domain classes (so that it can use the appropriate <tt>ClassLoader</tt>).
	 */
	public Bundle getBundle() {
		return _bundle;
	}
	/*
	 * Set by Essential itself (rather than through Spring, say).
	 */
	public void setBundle(Bundle domainBundle) {
		_bundle = domainBundle;
		_domainRegistrar.setBundle(domainBundle);
	}
	
	
}
