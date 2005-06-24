package mikespike4;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

public abstract class StartupJob extends UIJob {
	
	public static final String PLATFORM_ARG = "-pauseStartupJobs";

	private static IJobChangeListener SLEEPER = null;
	
	/**
	 * Interogates the passed arguments to see whether all subsequent
	 * <code>StartupJob</code>s are put to sleep.  The effects of this 
	 * are reversed by the <code>wake()</code>.
	 */
	public static void conditionalSleep( String[] args ) {
		if ( args == null ) throw new IllegalArgumentException();
		if ( SLEEPER != null ) return;
		for ( int i=0, num = args.length ; i< num ; i++ ) {
			if ( PLATFORM_ARG.equals( args[i] )  ) {
				SLEEPER = new JobChangeAdapter(){
					public void aboutToRun(IJobChangeEvent event) {
						assert event != null;
						if ( event.getJob().belongsTo( StartupJob.class ) ) {
							event.getJob().sleep();
						}
					}
					
				};
				Platform.getJobManager().addJobChangeListener( SLEEPER );
			}
		}
	}
	
	/**
	 * Runs all waiting jobs and stops future jobs frombeen put to sleep.
	 * <br>This must be called in the UI thread.
	 * <br>Any error causes an exceptin to be thrown and all subsequent jobs
	 * not to be run.
	 * <br>Irrespective of erros, this method clears all waiting jobs. 
	 */
	public static void wake() throws Exception {
		assert Display.getCurrent() != null;
		if ( SLEEPER != null ) {
			Platform.getJobManager().removeJobChangeListener( SLEEPER );
			SLEEPER = null;
			Job[] jobs = Platform.getJobManager().find( StartupJob.class );
			try {
				for ( int i=0 ; i< jobs.length ; i++ ) {
					assert jobs[i] instanceof UIJob;
					// jobs are explicitly run rather than run through the 
					// JobManager as this uses all sorts of aysncExec trickery
					// that cannot be join()ed or otherwise wait()ed for
					IStatus status = ((UIJob)jobs[i]).runInUIThread( null );
					if ( !status.isOK() ) {
						Throwable th = status.getException();
						if ( th instanceof Exception ) {
							throw (Exception)th;
						}
						else {
							throw new Exception( th );
						}
					}
				}
			}
			finally {
				Platform.getJobManager().cancel( StartupJob.class );
			}
		}
	}
	

	/**
	 * No-arg constructor for simplicity
	 *
	 */
	public StartupJob() {
		super( StartupJob.class.getName() );
	}


	/**
	 * All start-up jobs belong to a family identified by <code>StartupJob.class</code>.
	 * @see org.eclipse.core.internal.jobs.InternalJob#belongsTo(java.lang.Object)
	 */
	@Override
	public boolean belongsTo(Object family) {
		return ( StartupJob.class == family );
	}

}
