package mikespike4.rcp;

import mikespike4.StartupJob;
import mikespike4.util.PlatformUtil;
import mikespike4.views.classbar.ClassBarView;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;


/**
 * Default perspective opened on startup.
 * @author Mike
 *
 */
public class DefaultPerspective implements IPerspectiveFactory {
	
	public static final String ID = DefaultPerspective.class.getName();
	
	private static final String RIGHT = "right";

	/**
	 * Creates:
	 * <ul>
	 * <li>an uncloseable ClassBarView on the left hand side
	 * <li>placeholder on the right-hand side for other views 
	 * </ul>
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 * @see de.berlios.rcpviewer.gui.views.classbar.ClassBarView;
	 */
	public void createInitialLayout( final IPageLayout layout) {
		if ( layout == null ) throw new IllegalArgumentException();
		
		// creation of classbar conditional on startup arguments so
		// wrapped in a startup job
		StartupJob job = new StartupJob(){
			public IStatus runInUIThread( IProgressMonitor monitor ) {
				layout.addStandaloneView(
						ClassBarView.ID,
						false,
						IPageLayout.LEFT,
						0.05f,
						layout.getEditorArea() );
				layout.getViewLayout( ClassBarView.ID ).setCloseable( false );
				// ensure that action bars etc are updated
				PlatformUtil.refreshCurrentPage();
				return Status.OK_STATUS;
			}
		};
		job.schedule();
				
		// editor area
		layout.setEditorAreaVisible( true );
		
		// right hand-side placeholders
        IPlaceholderFolderLayout right = layout.createPlaceholderFolder( 
                RIGHT, 
                IPageLayout.RIGHT, 
                0.90f,
				layout.getEditorArea() );

	}
}
