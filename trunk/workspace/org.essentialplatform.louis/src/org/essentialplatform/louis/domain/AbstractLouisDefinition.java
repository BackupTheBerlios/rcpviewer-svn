package org.essentialplatform.louis.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.louis.dnd.IDndTransferProvider;
import org.essentialplatform.louis.factory.IGuiFactories;
import org.essentialplatform.louis.labelproviders.ILouisLabelProvider;
import org.essentialplatform.runtime.client.transaction.ITransactionManager;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.shared.domain.DomainBootstrapException;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;
import org.osgi.framework.Bundle;

/**
 * Implementation of {@link IDomainDefinition} designed for configuration
 * by Spring, or manual instantiation.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractLouisDefinition implements ILouisDefinition {

	protected abstract Logger getLogger(); 


	
	////////////////////////////////////////////////////////////////////
	// Registration
	////////////////////////////////////////////////////////////////////
	
	/**
	 * Delegates to {@link #getDomainDefinition()} to register the classes, 
	 * then installs any secondary {@link IDomainBuilder}s. 
	 * 
	 */
	public final void registerClasses() throws DomainBootstrapException {
		getDomainDefinition().registerClasses();

		for(IDomainBuilder secondaryBuilder: _secondaryBuilders) {
			Domain.instance(getDomainDefinition().getName()).addBuilder(secondaryBuilder); 
		}
		Domain.instance(getDomainDefinition().getName()).done();	

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
	


	////////////////////////////////////////////////////////////////////
	// DomainDefinition
	////////////////////////////////////////////////////////////////////

	private IDomainDefinition _domainDefinition;
	/**
	 * The domain (definition) to which this Louis (UI) definition relates.
	 */
	public IDomainDefinition getDomainDefinition() {
		return _domainDefinition;
	}
	/**
	 * For dependency injection (or set by subclass).
	 * 
	 * @param domainDefinition
	 */
	public void setDomainDefinition(IDomainDefinition domainDefinition) {
		_domainDefinition = domainDefinition;
	}
	
	
	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainDefinition#getName()
	 */
	public final String getName() {
		return getDomainDefinition().getName();
	}
	/*
	 * @see org.essentialplatform.runtime.shared.domain.IDomainDefinition#getDomainBuilder()
	 */
	public final IDomainBuilder getDomainBuilder() {
		return getDomainDefinition().getDomainBuilder();
	}
	

	////////////////////////////////////////////////////////////////////
	// GuiFactories
	////////////////////////////////////////////////////////////////////
	

	private IGuiFactories _guiFactories;
	public IGuiFactories getGuiFactories() {
		return _guiFactories;
	}
	/**
	 * For dependency injection (or set by subclass).
	 * 
	 * @param guiFactories
	 */
	public void setGuiFactories(IGuiFactories guiFactories) {
		_guiFactories = guiFactories;
	}
	

	////////////////////////////////////////////////////////////////////
	// GlobalLabelProvider
	////////////////////////////////////////////////////////////////////

	private ILouisLabelProvider _globalLabelProvider;
	public ILouisLabelProvider getGlobalLabelProvider() {
		return _globalLabelProvider;
	}
	/**
	 * For dependency injection (or set by subclass).
	 * 
	 * @param globalLabelProvider
	 */
	public void setGlobalLabelProvider(ILouisLabelProvider globalLabelProvider) {
		_globalLabelProvider = globalLabelProvider;
	}
	

	////////////////////////////////////////////////////////////////////
	// GlobalDndTransferProvider
	////////////////////////////////////////////////////////////////////
	
	private IDndTransferProvider _globalDndTransferProvider; 
	public IDndTransferProvider getGlobalDndTransferProvider() {
		return _globalDndTransferProvider;
	}
	/**
	 * For dependency injection (or set by subclass).
	 * 
	 * @param globalDnDTransferProvider
	 */
	public void setGlobalDndTransferProvider(IDndTransferProvider globalDnDTransferProvider) {
		_globalDndTransferProvider = globalDnDTransferProvider;
	}

}
