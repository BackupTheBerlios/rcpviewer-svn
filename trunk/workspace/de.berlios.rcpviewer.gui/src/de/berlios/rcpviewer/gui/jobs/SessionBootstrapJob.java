package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainRegistry;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.session.ISessionManager;

/**
 * Starts a new session on a domain
 * @author Mike
 */
public class SessionBootstrapJob extends Job {
	
	private static final String DEFAULT_DOMAIN_ID = "default";
	
	private final String domainId;
	
	/**
	 * Bootstraps a session on the default domain.
	 */
	public SessionBootstrapJob() {
		this( DEFAULT_DOMAIN_ID );
	}
	
	/**
	 * Bootstraps a session on the domian identified by the passed id.
	 * @param domainId
	 */
	public SessionBootstrapJob( String domainId ) {
		super( SessionBootstrapJob.class.getSimpleName() );
		if ( domainId == null ) throw new IllegalArgumentException();
		this.domainId = domainId;
	}


	/**
	 * Runs the wrapped <code>IDomainBootstrap</code>.
	 * @see org.eclipse.core.internal.jobs.InternalJob#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {  
		try {
			//FIXME In the future there will be a better way to set sessions
			ISessionManager sessionManager= RuntimePlugin.getDefault().getSessionManager();
			IDomainRegistry domainRegistry= RuntimePlugin.getDefault().getDomainRegistry();
			IDomain domain= domainRegistry.getDomain("default");
			sessionManager.createSession( domain, new InMemoryObjectStore());
		}
		catch ( CoreException ce ) {
			return ce.getStatus();	
		}	
		return Status.OK_STATUS;	
	}
}
