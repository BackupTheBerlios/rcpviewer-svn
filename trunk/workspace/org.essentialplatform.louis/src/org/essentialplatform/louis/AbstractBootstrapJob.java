/**
 * 
 */
package org.essentialplatform.louis;

import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

/**
 * Superclass for all bootstrap jobs - ensures they do not run
 * in parallel
 * @author Mike
 */
abstract class AbstractBootstrapJob extends Job {
	
	// straight out of help file!
	private static final ISchedulingRule MUTEX = new ISchedulingRule(){
      public boolean isConflicting(ISchedulingRule rule) {
          return rule == this;
       }
       public boolean contains(ISchedulingRule rule) {
          return rule == this;
       }
	};

	/**
	 * @param name
	 */
	protected AbstractBootstrapJob(String name) {
		super(name);
		setRule( MUTEX );
	}

	/**
	 * Job family linked by class object.
	 * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
	 */
	@Override
	public boolean belongsTo(Object family) {
		return AbstractBootstrapJob.class == family;
	}
	
	


}
