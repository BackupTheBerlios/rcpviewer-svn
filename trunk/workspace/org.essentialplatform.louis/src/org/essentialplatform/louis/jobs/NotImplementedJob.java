package org.essentialplatform.louis.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.essentialplatform.louis.LouisPlugin;


/**
 * Puts up a 'not implemented' dialog.
 * @author Mike
 *
 */
public class NotImplementedJob extends AbstractUserJob {
	
	private final String _title;

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
		this._title = title;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		MessageDialog.openWarning( 
				null, 
				_title,
				LouisPlugin.getResourceString( "NotImplementedJob.Msg") ); //$NON-NLS-1$
		return Status.OK_STATUS;
	}
	
	

}
