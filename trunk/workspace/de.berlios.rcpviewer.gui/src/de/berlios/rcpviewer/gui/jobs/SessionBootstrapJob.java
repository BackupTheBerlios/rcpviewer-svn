package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainRegistry;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.session.ISessionManager;

/**
 * Starts a new session on a domain
 * @author Mike
 */
public class SessionBootstrapJob extends Job {
	
	private static final String DEFAULT_DOMAIN_ID = "default";
	
	private final String _domainId;
	private final IObjectStore _store;
	
	/**
	 * Bootstraps a session on the default domain using an im-memory object store
	 */
	public SessionBootstrapJob() {
		this( DEFAULT_DOMAIN_ID, new InMemoryObjectStore()  );
	}
	
	/**
	 * Bootstraps a session on the domain identified by the passed id with the
	 * passed object store
	 * @param domainId
	 * @param store
	 */
	public SessionBootstrapJob( String domainId, IObjectStore store ) {
		super( SessionBootstrapJob.class.getSimpleName() );
		if ( domainId == null ) throw new IllegalArgumentException();
		if ( store == null ) throw new IllegalArgumentException();
		_domainId = domainId;
		_store = store;
	}


	/**
	 * Creates a session for the passed domain and object store.
	 * @see org.eclipse.core.internal.jobs.InternalJob#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {  
		try {
			//FIXME In the future there will be a better way to set sessions
			ISessionManager sessionManager= RuntimePlugin.getDefault().getSessionManager();
			IDomainRegistry domainRegistry= RuntimePlugin.getDefault().getDomainRegistry();
			IDomain domain = domainRegistry.getDomain( _domainId );
			sessionManager.createSession( domain, _store );
		}
		catch ( CoreException ce ) {
			return ce.getStatus();	
		}	
		return Status.OK_STATUS;	
	}
}
