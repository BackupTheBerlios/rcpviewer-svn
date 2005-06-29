package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.gui.GuiPlugin;

/**
 * Provides the user with a GUI means of finding a specific instance of the 
 * passed domain class and opening the default editor on this.
 * @author Mike
 *
 */
public class SearchJob extends UIJob {

	private final IDomainClass clazz;
	
	/**
	 * Constrcutor requires the class to open.
	 * @param clazz
	 */
	public SearchJob( IDomainClass clazz ) {
		super( GuiPlugin.getResourceString( "SearchJob.Name" ) );
		if ( clazz == null ) throw new IllegalArgumentException();
		this.clazz = clazz;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		new NotImplementedJob( getName() ).schedule();
		return Status.OK_STATUS;
	}

}
