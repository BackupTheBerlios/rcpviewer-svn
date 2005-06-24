package mikespike4_test;

import junit.framework.TestCase;
import mikespike4.StartupJob;
import mikespike4.test.TestSession;

import org.eclipse.core.runtime.Platform;


/**
 * Parent for all RCP test cases.
 * @author Mike
 *
 */
abstract class AbstractRcpTestCase extends TestCase {
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		checkPlatformArguments();
		TestSession.start();
		StartupJob.wake();
	}

	@Override
	protected void tearDown() throws Exception {
		if ( TestSession.isActive() ) TestSession.stop();
		super.tearDown();
	}
	
	private void checkPlatformArguments() throws Exception {
		// check that argument has been sent to control startup jobs
		String[] args = Platform.getApplicationArgs();
		for ( int i=0, num = args.length ; i< num ; i++ ) {
			if ( StartupJob.PLATFORM_ARG.equals( args[i] ) ) return;
		}
		fail( "The following application argument must be set : "
				+ StartupJob.PLATFORM_ARG );
	}

}
