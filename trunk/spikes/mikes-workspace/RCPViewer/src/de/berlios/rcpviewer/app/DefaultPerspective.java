package de.berlios.rcpviewer.app;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.berlios.rcpviewer.views.ClassBar;

/**
 * Auto-generated
 * @author Mike
 */
public class DefaultPerspective implements IPerspectiveFactory {
	
	static final String ID = DefaultPerspective.class.getName();

	public void createInitialLayout(IPageLayout layout) {
		if (layout == null)
			throw new RuntimeException("layout may not be null");
		
        // editor area
        layout.setEditorAreaVisible( true );
        String editorArea = layout.getEditorArea();
		
        // left - classbar
        IFolderLayout left = layout.createFolder( 
                "classbar", 
                IPageLayout.LEFT, 
                0.3f,
                editorArea);
        left.addView( ClassBar.ID );
		layout.getViewLayout( ClassBar.ID ).setCloseable( false );  // for now
		
		
	}
}
