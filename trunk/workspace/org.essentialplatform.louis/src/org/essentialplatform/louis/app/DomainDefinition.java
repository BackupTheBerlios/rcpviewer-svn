package org.essentialplatform.louis.app;

import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.louis.dnd.IDndTransferProvider;
import org.essentialplatform.louis.factory.IGuiFactories;
import org.essentialplatform.louis.labelproviders.ILouisLabelProvider;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.shared.domain.IDomainBootstrap;
import org.osgi.framework.Bundle;

/**
 * Implementation of {@link IDomainDefinition} designed for configuration
 * by Spring, or manual instantiation.
 * 
 * @author Dan Haywood
 */
public class DomainDefinition implements IDomainDefinition {


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
	 *  
	 * @param domainBootstrap
	 */
	public void setName(String name) {
		_name = name;
	}

	
	////////////////////////////////////////////////////////////////////
	// DomainBuilder
	////////////////////////////////////////////////////////////////////

	private IDomainBuilder _domainBuilder = new EssentialProgModelRuntimeBuilder();
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
	 * Optional; if not specified then defaults to {@link EssentialProgModelRuntimeBuilder}.
	 * 
	 * @param domainBuilder
	 */
	public void setDomainBuilder(IDomainBuilder domainBuilder) {
		_domainBuilder = domainBuilder;
	}


	////////////////////////////////////////////////////////////////////
	// DomainBootstrap
	////////////////////////////////////////////////////////////////////

	private IDomainBootstrap _domainBootstrap;
	public IDomainBootstrap getDomainBootstrap() {
		return _domainBootstrap;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param domainBootstrap
	 */
	public void setDomainBootstrap(IDomainBootstrap domainBootstrap) {
		_domainBootstrap = domainBootstrap;
	}
	
	
	////////////////////////////////////////////////////////////////////
	// GuiFactories
	////////////////////////////////////////////////////////////////////
	

	private IGuiFactories _guiFactories;
	public IGuiFactories getGuiFactories() {
		return _guiFactories;
	}
	/**
	 * For dependency injection.
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
	 * For dependency injection.
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
	 * For dependency injection.
	 * 
	 * @param globalDnDTransferProvider
	 */
	public void setGlobalDndTransferProvider(IDndTransferProvider globalDnDTransferProvider) {
		_globalDndTransferProvider = globalDnDTransferProvider;
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
	 * to assist the {@link IDomainBootstrap} in the verification of 
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
		_domainBootstrap.setBundle(domainBundle);
	}
	
	
}
