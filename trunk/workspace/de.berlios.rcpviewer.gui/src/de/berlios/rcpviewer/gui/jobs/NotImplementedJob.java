package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.progress.UIJob;

import de.berlios.rcpviewer.gui.GuiPlugin;

/**
 * Puts up a 'not implemented' dialog.
 * @author Mike
 *
 */
public class NotImplementedJob extends UIJob {
	
	private final String title;

	/**
	 * No-arg constructor - title set to <code>null</code>.
	 */
	public NotImplementedJob() {
		this( null );
	}
	
	/**
	 * Constructor passed title to display - can be <code>null</code>.
	 * @param title
	 */
	public NotImplementedJob( String title ) {
		super( NotImplementedJob.class.getSimpleName() );
		this.title = title;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		MessageDialog.openWarning( 
				null, 
				title,
				GuiPlugin.getResourceString( "NotImplementedJob.Msg") );
		return Status.OK_STATUS;
	}
	
	

}
