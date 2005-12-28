package org.essentialplatform.louis;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.essentialplatform.core.domain.DomainConstants;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.RuntimePlugin;
import org.essentialplatform.runtime.domain.adapters.IDomainRegistry;
import org.essentialplatform.runtime.persistence.PersistenceConstants;
import org.essentialplatform.runtime.session.ISessionManager;
import org.essentialplatform.runtime.session.SessionBinding;

/**
 * Starts a new session on a domain
 * 
 * @author Mike
 */
class SessionBootstrapJob extends AbstractBootstrapJob {

	private final SessionBinding _sessionBinding;

	/**
	 * Bootstraps a session on the default domain using the passed object store
	 */
	SessionBootstrapJob() {
		this(new SessionBinding(DomainConstants.DEFAULT_NAME, PersistenceConstants.DEFAULT_OBJECT_STORE_ID));
	}

	/**
	 * Bootstraps a session on the domain name and (id of the) objectstore
	 * represented by the supplied {@link SessionBinding}.
	 * 
	 * @param sessionBinding
	 */
	SessionBootstrapJob(SessionBinding sessionBinding) {
		super(SessionBootstrapJob.class.getSimpleName());
		if (sessionBinding == null)
			throw new IllegalArgumentException();
		_sessionBinding = sessionBinding; 
	}

	/**
	 * Creates a session for the passed domain and object store.
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			// FIXME In the future there will be a better way to set sessions
			ISessionManager sessionManager = RuntimePlugin.getDefault().getSessionManager();
			IDomainRegistry domainRegistry = RuntimePlugin.getDefault().getDomainRegistry();
			IDomain domain = domainRegistry.getDomain(_sessionBinding.getDomainName());
			sessionManager.defineSession(domain, _sessionBinding.getObjectStoreId());
		} catch (CoreException ce) {
			return ce.getStatus();
		}
		return Status.OK_STATUS;
	}
}
