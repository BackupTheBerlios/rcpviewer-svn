package org.essentialplatform.louis.app;

import org.eclipse.jface.viewers.ILabelProvider;
import org.essentialplatform.louis.dnd.IDndTransferProvider;
import org.essentialplatform.louis.factory.IGuiFactories;
import org.essentialplatform.runtime.shared.domain.IDomainBootstrap;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Encapsulates the definition of an domain to Essential.
 */
public interface IDomainDefinition {
	
	IDomainBootstrap getDomainBootstrap();
	
	SessionBinding getSessionBinding();

	IGuiFactories getGuiFactories();
	
	ILabelProvider getGlobalLabelProvider();
	
	IDndTransferProvider getGlobalDndTransferProvider();
}
