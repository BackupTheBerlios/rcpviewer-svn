package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.editors.DefaultEditor;
import de.berlios.rcpviewer.gui.editors.DefaultEditorInput;
import de.berlios.rcpviewer.gui.util.PlatformUtil;
import de.berlios.rcpviewer.gui.views.ops.OpsView;
import de.berlios.rcpviewer.gui.views.sessiontree.SessionTreeView;
import de.berlios.rcpviewer.session.IDomainObject;

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
	public OpenDomainObjectJob( IDomainObject object ) {
		super( GuiPlugin.getResourceString( "OpenDomainObjectJob.Name" ), //$NON-NLS-1$
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
			GuiPlugin.getDefault().getLog().log( ce.getStatus() );
			MessageDialog.openError( 
					null, 
					GuiPlugin.getResourceString( "OpenDomainObjectJob.Error"), //$NON-NLS-1$
					ce.getMessage() );
			status = ce.getStatus();
		}
		ReportJob report;
		if ( Status.OK_STATUS == status ) {
			report = new ReportJob( 
					GuiPlugin.getResourceString( "OpenDomainObjectJob.Ok"),  //$NON-NLS-1$
					ReportJob.INFO,
					GuiPlugin.getDefault()
							 .getLabelProvider( getDomainObject() )
							 .getText( getDomainObject() ) );
		}
		else {
			report = new ReportJob( status.getMessage(), ReportJob.ERROR );
		}
		report.schedule();
		return status;
	}

}
