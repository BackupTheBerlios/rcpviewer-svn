package org.essentialplatform.louis.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.runtime.client.session.ClientSessionManager;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.client.session.IClientSessionManager;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * Creates a new instance of the passed domain class and opens the default editor.
 * @author Mike
 *
 */
public class NewDomainObjectJob extends AbstractUserJob {

	private final IDomainClass _clazz;
	
	/**
	 * Constructor requires the class to open.
	 * @param clazz
	 */
	public NewDomainObjectJob( IDomainClass clazz ) {
		super( LouisPlugin.getResourceString( "NewDomainObjectJob.Name" ) ); //$NON-NLS-1$
		if ( clazz == null ) throw new IllegalArgumentException();
		this._clazz = clazz;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		IStatus status;
		String name = null;
		IClientSessionManager sessionManager = ClientSessionManager.instance();
		// TODO: hardcoded for the default domain, needs to be switchable.
		IClientSession session= sessionManager.getCurrentSession(Domain.instance());
		
		IDomainObject<?> domainObject = session.create( _clazz );
//			IDomainObject<?> domainObject = session.recreate( _clazz );
		
		new OpenDomainObjectJob( domainObject ).schedule();
		name = LouisPlugin.getText( domainObject );
		status = Status.OK_STATUS;
		
		ReportJob report;
		if ( Status.OK_STATUS == status ) {
			assert name != null;
			report = new ReportJob( 
					LouisPlugin.getResourceString( "NewDomainObjectJob.Ok"),  //$NON-NLS-1$
					ReportJob.INFO,
					name );
		}
		else {
			report = new ReportJob( status.getMessage(), ReportJob.ERROR );
		}
		report.schedule();
		return status;
	}

}
