/**
 * 
 */
package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

/**
 * Super class for all GUI jobs fired by the user.
 * @author Mike
 */
public abstract class AbstractUserJob extends UIJob {

	// once login functionality added we will point to the user representation
	// generated there.
	public static final Object USER = new Object();
	

	/**
	 * @param jobDisplay
	 * @param name
	 */
	public AbstractUserJob(Display jobDisplay, String name) {
		super(jobDisplay, name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public AbstractUserJob(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Job family linked by the user object.
	 * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
	 */
	@Override
	public boolean belongsTo(Object family) {
		return USER.equals( family );
	}
	
	


}
