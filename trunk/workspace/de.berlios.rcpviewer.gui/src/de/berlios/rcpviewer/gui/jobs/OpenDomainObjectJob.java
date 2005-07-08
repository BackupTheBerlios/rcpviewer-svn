package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.progress.UIJob;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.editors.DefaultEditor;
import de.berlios.rcpviewer.gui.editors.DefaultEditorInput;
import de.berlios.rcpviewer.gui.util.PlatformUtil;
import de.berlios.rcpviewer.gui.views.actions.ActionsView;
import de.berlios.rcpviewer.gui.views.sessiontree.SessionTreeView;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Opens the passed domain object.
 * @author Mike
 *
 */
public class OpenDomainObjectJob extends UIJob {

	private final IDomainObject _domainObject;
	
	/**
	 * Constructor requires the class to open.
	 * @param clazz
	 */
	public OpenDomainObjectJob( IDomainObject object ) {
		super( GuiPlugin.getResourceString( "OpenDomainObjectJob.Name" ) ); //$NON-NLS-1$
		if ( object == null ) throw new IllegalArgumentException();
		_domainObject = object;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		try {
			// ensure instance-related views are open
			PlatformUtil.getActivePage().showView( SessionTreeView.ID );
			PlatformUtil.getActivePage().showView( ActionsView.ID );
			
			// open editor
			DefaultEditorInput input = new DefaultEditorInput( _domainObject );
			PlatformUtil.getActivePage().openEditor(  input, DefaultEditor.ID );
			return Status.OK_STATUS;
		}
		catch ( CoreException ce ) {
			GuiPlugin.getDefault().getLog().log( ce.getStatus() );
			MessageDialog.openError( 
					null, 
					GuiPlugin.getResourceString( "OpenDomainObjectJob.Error"), //$NON-NLS-1$
					ce.getMessage() );
			return ce.getStatus();
		}
	}

}
