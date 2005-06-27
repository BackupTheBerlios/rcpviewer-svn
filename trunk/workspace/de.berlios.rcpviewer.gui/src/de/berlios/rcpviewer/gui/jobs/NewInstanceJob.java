package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.gui.GuiPlugin;

/**
 * Creates a new instance of the passed domain class and opens the default editor.
 * @author Mike
 *
 */
public class NewInstanceJob extends UIJob {

	private final IDomainClass clazz;
	
	/**
	 * Constrcutor requires the class to open.
	 * @param clazz
	 */
	public NewInstanceJob( IDomainClass clazz ) {
		super( GuiPlugin.getResourceString( "NewInstanceJob.Name" ) );
		if ( clazz == null ) throw new IllegalArgumentException();
		this.clazz = clazz;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		new NotImplementedJob( getName() ).schedule();
		return Status.OK_STATUS;
	}

}
