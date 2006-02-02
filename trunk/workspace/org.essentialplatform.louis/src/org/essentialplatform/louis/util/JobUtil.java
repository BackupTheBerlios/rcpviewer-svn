package org.essentialplatform.louis.util;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;

public final class JobUtil {

	private JobUtil() {}

	/**
	 * Wait for job to finish.
	 * 
	 * @param job
	 * @param logger TODO
	 * @throws InterruptedException
	 * @throws CoreException
	 */
	public static void waitForJob(Job job, Logger logger) throws InterruptedException, CoreException {
		assert job != null;
		job.join();
		if ( !job.getResult().isOK() ) {
			logger.error( job.getResult() );
			throw new CoreException( job.getResult() );
		}
	}

}
