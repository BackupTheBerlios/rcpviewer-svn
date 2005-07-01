package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.progress.UIJob;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.util.PlatformUtil;
import de.berlios.rcpviewer.gui.views.sessiontree.SessionTreeView;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionManager;

/**
 * Creates a new instance of the passed domain class and opens the default editor.
 * @author Mike
 *
 */
public class NewDomainObjectJob extends UIJob {

	private final IRuntimeDomainClass _clazz;
	
	/**
	 * Constructor requires the class to open.
	 * @param clazz
	 */
	public NewDomainObjectJob( IRuntimeDomainClass clazz ) {
		super( GuiPlugin.getResourceString( "NewDomainObjectJob.Name" ) );
		if ( clazz == null ) throw new IllegalArgumentException();
		this._clazz = clazz;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		try {
			ISessionManager sessionManager= RuntimePlugin.getDefault().getSessionManager();
			ISession session= sessionManager.get(sessionManager.getCurrentSessionId());
			
			IDomainObject domainObject = session.createTransient( _clazz );
			PlatformUtil.getActivePage().showView( SessionTreeView.ID );

			new OpenDomainObjectJob( domainObject ).schedule();
			return Status.OK_STATUS;
		}
		catch ( CoreException ce ) {
			GuiPlugin.getDefault().getLog().log( ce.getStatus() );
			MessageDialog.openError( 
					null, 
					GuiPlugin.getResourceString( "NewDomainObjectJob.Error"), 
					ce.getMessage() );
			return ce.getStatus();
		}
	}

}
