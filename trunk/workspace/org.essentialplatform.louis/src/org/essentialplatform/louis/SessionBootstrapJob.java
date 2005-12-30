package org.essentialplatform.louis;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.essentialplatform.core.domain.DomainConstants;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry;
import org.essentialplatform.runtime.shared.persistence.PersistenceConstants;
import org.essentialplatform.runtime.shared.session.IClientSessionManager;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.RuntimePlugin;

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
			IClientSessionManager sessionManager = RuntimePlugin.getDefault().getSessionManager();
			IDomainRegistry domainRegistry = RuntimePlugin.getDefault().getDomainRegistry();
			sessionManager.defineSession(_sessionBinding);
		} catch (CoreException ce) {
			return ce.getStatus();
		}
		return Status.OK_STATUS;
	}
}
