package de.berlios.rcpviewer.gui.app;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

import de.berlios.rcpviewer.gui.views.classbar.ClassBarView;

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
		
		layout.addStandaloneView(
				ClassBarView.ID,
				false,
				IPageLayout.LEFT,
				0.1f,
				layout.getEditorArea() );
		layout.getViewLayout( ClassBarView.ID ).setCloseable( false );
				
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
