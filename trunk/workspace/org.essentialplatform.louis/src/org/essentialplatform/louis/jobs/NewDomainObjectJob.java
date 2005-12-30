package org.essentialplatform.louis.jobs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.essentialplatform.louis.LouisPlugin;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.client.session.IClientSessionManager;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.RuntimePlugin;

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
		try {
			IClientSessionManager sessionManager= RuntimePlugin.getDefault().getSessionManager();
			// TODO: hardcoded for the default domain, needs to be switchable.
			IClientSession session= sessionManager.getCurrentSession(Domain.instance());
			
			IDomainObject<?> domainObject = session.create( _clazz );
//			IDomainObject<?> domainObject = session.recreate( _clazz );
			
			new OpenDomainObjectJob( domainObject ).schedule();
			name = LouisPlugin.getText( domainObject );
			status = Status.OK_STATUS;
		}
		catch ( CoreException ce ) {
			LouisPlugin.getDefault().getLog().log( ce.getStatus() );
			MessageDialog.openError( 
					null, 
					LouisPlugin.getResourceString( "NewDomainObjectJob.Error"), //$NON-NLS-1$
					ce.getMessage() );
			status = ce.getStatus();
		}
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
