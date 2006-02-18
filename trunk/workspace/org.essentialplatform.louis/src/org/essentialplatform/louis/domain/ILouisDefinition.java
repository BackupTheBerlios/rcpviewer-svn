package org.essentialplatform.louis.domain;

import java.util.List;

import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.louis.dnd.IDndTransferProvider;
import org.essentialplatform.louis.factory.IGuiFactories;
import org.essentialplatform.louis.labelproviders.ILouisLabelProvider;
import org.essentialplatform.runtime.shared.domain.DomainBootstrapException;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;
import org.osgi.framework.Bundle;

/**
 * Encapsulates the definition of an domain to Essential.
 */
public interface ILouisDefinition extends IDomainDefinition {
	

	/**
	 * The (definition of the) domain for which this UI-specific definition
	 * applies.
	 */
	IDomainDefinition getDomainDefinition();

	
	/**
	 * As per inherited interface, but also  
	 * post-registers any secondary {@link IDomainBuilder}s.
	 * 
	 * @see IDomainClassRegistrar
	 */
	void registerClasses() throws DomainBootstrapException;

	/**
	 * For dependency injection.
	 * 
	 * @return
	 */
	List<IDomainBuilder> getSecondaryBuilders();
	/**
	 * Adds the {@link IDomainBuilder} to those returned from {@link #getSecondaryBuilders()}.
	 * 
	 * <p>
	 * If using an implementation that is designed for dependency injection, 
	 * then this method would <i>not</i> be used.  Instead, the implementation
	 * would provide a setter for the entire <tt>List</tt>.
	 * 
	 * @param domainBuilder
	 */
	void addSecondaryBuilder(IDomainBuilder domainBuilder);

	
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
