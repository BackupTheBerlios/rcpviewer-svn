package org.essentialplatform.louis.app;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.console.IConsoleConstants;
import org.essentialplatform.louis.views.classbar.ClassBarView;
import org.essentialplatform.louis.views.ops.OpsView;
import org.essentialplatform.louis.views.sessiontree.SessionTreeView;


/**
 * Default perspective opened on startup.
 * @author Mike
 *
 */
public class DefaultPerspective implements IPerspectiveFactory {
	
	/**
	 * ID used in plugin.xml.
	 */
	public static final String ID = DefaultPerspective.class.getName();
	
	private static final String TOP_RIGHT = "topright"; //$NON-NLS-1$
	private static final String BOTTOM_RIGHT = "bottomright"; //$NON-NLS-1$
	private static final String BOTTOM = "bottom"; //$NON-NLS-1$
	
	/**
	 * Creates:
	 * <ul>
	 * <li>an uncloseable ClassBarView on the left hand side
	 * <li>placeholder on the top right-hand side for views:
	 *   <ul>
	 *   <li>session tree
	 *   </ul>
	 * <li>placeholder on the bottom right-hand side for views:
	 *   <ul>
	 *   <li>actions view
	 *   </ul>
	 * <li>placeholder on bottom for views:
	 *   <ul>
	 *   <li>platform search views
	 *   <li>console view
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
		
		// top right hand-side placeholder
        IPlaceholderFolderLayout topRight = layout.createPlaceholderFolder( 
                TOP_RIGHT, 
                IPageLayout.RIGHT, 
                0.8f,
				layout.getEditorArea() );
        topRight.addPlaceholder( SessionTreeView.ID );
        
        // bottom right hand-side placeholder
        IPlaceholderFolderLayout bottomRight = layout.createPlaceholderFolder( 
                BOTTOM_RIGHT, 
                IPageLayout.BOTTOM, 
                0.7f,
				TOP_RIGHT );        
        bottomRight.addPlaceholder( OpsView.ID );
		
		// bottom placeholder
		IPlaceholderFolderLayout bottom =  layout.createPlaceholderFolder(
				BOTTOM,
				IPageLayout.BOTTOM, 
				0.7f, 
				layout.getEditorArea() );
		bottom.addPlaceholder( "org.eclipse.search.ui.views.SearchView" ); //$NON-NLS-1$
		bottom.addPlaceholder( "org.eclipse.search.SearchResultView" ); //$NON-NLS-1$
		bottom.addPlaceholder( IConsoleConstants.ID_CONSOLE_VIEW );
		
		// view shortcuts
        layout.addShowViewShortcut( SessionTreeView.ID );
        layout.addShowViewShortcut( OpsView.ID );
        layout.addShowViewShortcut( IConsoleConstants.ID_CONSOLE_VIEW );

	}
}
