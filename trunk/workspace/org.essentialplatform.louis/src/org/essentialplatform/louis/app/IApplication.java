package org.essentialplatform.louis.app;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.essentialplatform.louis.domain.ILouisDefinition;
import org.essentialplatform.runtime.shared.IRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;
import org.essentialplatform.runtime.shared.domain.IDomainRegistrar;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public interface IApplication extends IPlatformRunnable {

	/**
	 * As a simplification (for now), only support a single domain and a
	 * single object store.
	 * 
	 * <p>
	 * From the domain definition's name and the object store's name the
	 * application can build an {@link SessionBinding}.
	 */
	void init(ILouisDefinition louisDefinition, String objectStoreName);
	
	IRuntimeBinding getBinding();
	void setBinding(IRuntimeBinding binding);
	
	
	



}
