package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.gui.GuiPlugin;

/**
 * Provides the user with a GUI means of finding a specific instance of the 
 * passed domain class and opening the default editor on this.
 * TODO - incoporate with Ted's serach stuff?
 * @author Mike
 *
 */
public class SearchJob extends UIJob {

	private final IRuntimeDomainClass _clazz;
	
	/**
	 * Constructor requires the class to open.
	 * @param clazz
	 */
	public SearchJob( IRuntimeDomainClass clazz ) {
		super( GuiPlugin.getResourceString( "SearchJob.Name" ) ); //$NON-NLS-1$
		if ( clazz == null ) throw new IllegalArgumentException();
		this._clazz = clazz;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		new NotImplementedJob( getName() ).schedule();
		return Status.OK_STATUS;
	}

}
