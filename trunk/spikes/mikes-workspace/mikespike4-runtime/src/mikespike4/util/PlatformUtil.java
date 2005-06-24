package mikespike4.util;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Static methods providing platform-specific utiltities.  Essentially 
 * addtional functionality to <code>PlatformUI</code>
 * @author Mike
 * @see org.eclipse.ui.PlatformUI;
 */
public class PlatformUtil {
	
	/**
	 * Using async gui jobs can affect gui generation.  This causes the 
	 * current page to be re-initialised.
	 */
	public static final void refreshCurrentPage() {
		IWorkbenchWindow window
			= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		window.setActivePage( null );
		window.setActivePage( page );
	}
	
	

	// prevent instantiation
	private PlatformUtil() {
		super();
	}

}
