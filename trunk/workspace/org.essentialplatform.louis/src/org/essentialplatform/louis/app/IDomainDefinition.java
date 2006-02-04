package org.essentialplatform.louis.app;

import org.eclipse.jface.viewers.ILabelProvider;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.louis.dnd.IDndTransferProvider;
import org.essentialplatform.louis.factory.IGuiFactories;
import org.essentialplatform.louis.labelproviders.ILouisLabelProvider;
import org.essentialplatform.runtime.shared.domain.IDomainBootstrap;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.osgi.framework.Bundle;

/**
 * Encapsulates the definition of an domain to Essential.
 */
public interface IDomainDefinition {
	
	/**
	 * The name of this domain.
	 * 
	 * <p>
	 * All classes registered by this domain (by the {@link IDomainBootstrap} 
	 * returned by {@link #getDomainBootstrap()}) should indicate this domain.
	 * 
	 * @return
	 */
	String getDomainName();


	/**
	 * Provided by Essential platform itself, is the {@link Bundle} that 
	 * represents the domain plugin.
	 * 
	 * @param domainBundle
	 */
	void setBundle(Bundle domainBundle);

	/**
	 * The (runtime) implementation of a {@link IDomainBuilder} which is
	 * capable of building a domain out of the classes registed by the 
	 * {@link IDomainBootstrap} returned by {@link #getDomainBootstrap()}).
	 *   
	 * @return
	 */
	IDomainBuilder getDomainBuilder();
	
	/**
	 * Bootstraps the domain.
	 * 
	 * @return
	 */
	IDomainBootstrap getDomainBootstrap();

	/**
	 * Gui-specifics.
	 * 
	 * @return
	 */
	IGuiFactories getGuiFactories();
	
	/**
	 * Gui-specifics.
	 * 
	 * @return
	 */
	ILouisLabelProvider getGlobalLabelProvider();
	
	/**
	 * Gui-specifics.
	 * 
	 * @return
	 */
	IDndTransferProvider getGlobalDndTransferProvider();
}
