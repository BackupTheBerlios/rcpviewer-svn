package org.essentialplatform.louis.app;

import org.essentialplatform.core.domain.DomainConstants;
import org.essentialplatform.louis.dnd.IDndTransferProvider;
import org.essentialplatform.louis.factory.IGuiFactories;
import org.essentialplatform.louis.labelproviders.ILouisLabelProvider;
import org.essentialplatform.runtime.shared.domain.IDomainBootstrap;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Implementation of {@link IDomainDefinition} designed for configuration
 * by Spring, or manual instantiation.
 * 
 * @author Dan Haywood
 */
public class DomainDefinition implements IDomainDefinition {


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
	// SessionBinding
	////////////////////////////////////////////////////////////////////
	
	private SessionBinding _sessionBinding = 
		new SessionBinding(DomainConstants.DEFAULT_NAME, PersistenceConstants.DEFAULT_OBJECT_STORE_ID);
	public SessionBinding getSessionBinding() {
		return _sessionBinding;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional.  If not specified, defaults to a binding for 
	 * {@link DomainConstants#DEFAULT_NAME} and 
	 * {@link PersistenceConstants#DEFAULT_OBJECT_STORE_ID)}
	 * 
	 * @param sessionBinding
	 */
	public void setSessionBinding(SessionBinding sessionBinding) {
		_sessionBinding = sessionBinding;
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
	
	private IDndTransferProvider _globalDnDTransferProvider; 
	public IDndTransferProvider getGlobalDndTransferProvider() {
		return _globalDnDTransferProvider;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param globalDnDTransferProvider
	 */
	public void setGlobalDnDTransferProvider(IDndTransferProvider globalDnDTransferProvider) {
		_globalDnDTransferProvider = globalDnDTransferProvider;
	}

}
