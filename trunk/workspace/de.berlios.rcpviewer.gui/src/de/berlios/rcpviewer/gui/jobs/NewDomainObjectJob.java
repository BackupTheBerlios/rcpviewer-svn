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
import de.berlios.rcpviewer.gui.editors.DefaultEditorContentBuilder;
import de.berlios.rcpviewer.gui.editors.DefaultEditor;
import de.berlios.rcpviewer.gui.editors.DefaultEditorInput;
import de.berlios.rcpviewer.gui.util.PlatformUtil;
import de.berlios.rcpviewer.progmodel.standard.DomainObject;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionManager;

/**
 * Creates a new instance of the passed domain class and opens the default editor.
 * @author Mike
 *
 */
public class NewDomainObjectJob extends UIJob {

	private final IRuntimeDomainClass clazz;
	
	/**
	 * Constructor requires the class to open.
	 * @param clazz
	 */
	public NewDomainObjectJob( IRuntimeDomainClass clazz ) {
		super( GuiPlugin.getResourceString( "NewInstanceJob.Name" ) );
		if ( clazz == null ) throw new IllegalArgumentException();
		this.clazz = clazz;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		try {
			ISessionManager sessionManager= RuntimePlugin.getDefault().getSessionManager();
			ISession session= sessionManager.get(sessionManager.getCurrentSessionId());
			IDomainObject domainObject= session.createTransient( clazz );

			DefaultEditorInput input = new DefaultEditorInput( 
						domainObject, 
						new DefaultEditorContentBuilder() );
			PlatformUtil.getActivePage().openEditor(  input, DefaultEditor.ID );
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
