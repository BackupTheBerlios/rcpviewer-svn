package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;

/**
 * Action that runs the <code>Job</code> passed on construction
 * @author Mike
 * @see org.eclipse.core.runtime.jobs.Job
 */
public class JobAction extends Action {

	private final Job job;

	
	/**
	 * Constructor passed the job to use.  
	 * <br>Note that the job's name is used as the action text.
	 * @param job
	 */
	public JobAction( Job job ) {
		if ( job == null ) throw new IllegalArgumentException();
		this.job = job;
		setText( job.getName() );
	}

	@Override
	public void run() {
		job.schedule();
	}
	
	

}
