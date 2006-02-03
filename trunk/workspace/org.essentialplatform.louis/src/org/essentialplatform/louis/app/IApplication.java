package org.essentialplatform.louis.app;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.essentialplatform.runtime.shared.session.SessionBinding;

public interface IApplication extends IPlatformRunnable {

	/**
	 * As a simplification (for now), only support a single domain and a
	 * single object store.
	 * 
	 * <p>
	 * From the domain definition's name and the object store's name the
	 * application can build an {@link SessionBinding}.
	 * 
	 * @param domainDefinition
	 * @param objectStoreName 
	 */
	void init(IDomainDefinition domainDefinition, String objectStoreName);


}
