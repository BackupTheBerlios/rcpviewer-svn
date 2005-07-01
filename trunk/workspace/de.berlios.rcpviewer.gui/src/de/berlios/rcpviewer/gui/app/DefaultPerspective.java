package de.berlios.rcpviewer.gui.app;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;

import de.berlios.rcpviewer.gui.views.classbar.ClassBarView;
import de.berlios.rcpviewer.gui.views.sessiontree.SessionTreeView;

/**
 * Default perspective opened on startup.
 * @author Mike
 *
 */
public class DefaultPerspective implements IPerspectiveFactory {
	
	public static final String ID = DefaultPerspective.class.getName();
	
	private static final String RIGHT = "right";
	private static final String BOTTOM = "bottom";
	
	/**
	 * Creates:
	 * <ul>
	 * <li>an uncloseable ClassBarView on the left hand side
	 * <li>placeholder on the right-hand side for views 
	 * <li>placeholder on bottom for views:
	 *   <ul>
	 *   <li>platform search views
	 *   </ul>
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
		
		// right hand-side placeholder
        IPlaceholderFolderLayout right = layout.createPlaceholderFolder( 
                RIGHT, 
                IPageLayout.RIGHT, 
                0.8f,
				layout.getEditorArea() );
		right.addPlaceholder( SessionTreeView.ID );
		
		// bottom placholder
		IPlaceholderFolderLayout bottom =  layout.createPlaceholderFolder(
				BOTTOM,
				IPageLayout.BOTTOM, 
				0.7f, 
				layout.getEditorArea() );
		bottom.addPlaceholder( "org.eclipse.search.ui.views.SearchView" );
		bottom.addPlaceholder( "org.eclipse.search.SearchResultView" );

	}
}
