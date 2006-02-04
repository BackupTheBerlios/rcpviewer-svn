package org.essentialplatform.louis;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.essentialplatform.runtime.client.session.ClientSessionManager;
import org.essentialplatform.runtime.client.session.IClientSessionManager;
import org.essentialplatform.runtime.shared.session.SessionBinding;

/**
 * Starts a new session on a domain
 * 
 * @author Mike
 */
public class SessionBootstrapJob extends AbstractBootstrapJob {

	private final SessionBinding _sessionBinding;


	/**
	 * Bootstraps a session on the domain name and (id of the) objectstore
	 * represented by the supplied {@link SessionBinding}.
	 * 
	 * @param sessionBinding
	 */
	public SessionBootstrapJob(SessionBinding sessionBinding) {
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
		IClientSessionManager sessionManager = ClientSessionManager.instance();
		sessionManager.defineSession(_sessionBinding);
		return Status.OK_STATUS;
	}
}
