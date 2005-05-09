package mikespike2_runtime;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView( 
				ClassExplorerView.class.getName(),  
				false, 
				IPageLayout.LEFT, 
				1.0f, 
				editorArea);
	}
}
