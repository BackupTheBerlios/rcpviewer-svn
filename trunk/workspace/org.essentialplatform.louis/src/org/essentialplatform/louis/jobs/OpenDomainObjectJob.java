package org.essentialplatform.louis.jobs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.editors.DefaultEditor;
import org.essentialplatform.louis.editors.DefaultEditorInput;
import org.essentialplatform.louis.util.PlatformUtil;
import org.essentialplatform.louis.views.ops.OpsView;
import org.essentialplatform.louis.views.sessiontree.SessionTreeView;

import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * Opens the passed domain object.
 * @author Mike
 *
 */
public class OpenDomainObjectJob extends AbstractDomainObjectJob  {

	/**
	 * Constructor requires the class to open.
	 * @param clazz
	 */
	public OpenDomainObjectJob( IDomainObject<?> object ) {
		super( LouisPlugin.getResourceString( "OpenDomainObjectJob.Name" ), //$NON-NLS-1$
			   object );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		IStatus status;
		try {
			// ensure instance-related views are open
			PlatformUtil.getActivePage().showView( SessionTreeView.ID );
			PlatformUtil.getActivePage().showView( OpsView.ID );
			
			// open editor
			DefaultEditorInput input
				= new DefaultEditorInput( getDomainObject() );
			PlatformUtil.getActivePage().openEditor(  input, DefaultEditor.ID );
			status = Status.OK_STATUS;
		}
		catch ( CoreException ce ) {
			LouisPlugin.getDefault().getLog().log( ce.getStatus() );
			MessageDialog.openError( 
					null, 
					LouisPlugin.getResourceString( "OpenDomainObjectJob.Error"), //$NON-NLS-1$
					ce.getMessage() );
			status = ce.getStatus();
		}
		ReportJob report;
		if ( Status.OK_STATUS == status ) {
			report = new ReportJob( 
					LouisPlugin.getResourceString( "OpenDomainObjectJob.Ok"),  //$NON-NLS-1$
					ReportJob.INFO,
					LouisPlugin.getText( getDomainObject() ) );
		}
		else {
			report = new ReportJob( status.getMessage(), ReportJob.ERROR );
		}
		report.schedule();
		return status;
	}

}
