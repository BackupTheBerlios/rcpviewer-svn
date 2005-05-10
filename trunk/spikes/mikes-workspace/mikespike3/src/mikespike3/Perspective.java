package mikespike3;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {
	
	public static final String ID = Perspective.class.getName();

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.addStandaloneView( 
				ObjectListView.class.getName(),  
				false, 
				IPageLayout.LEFT, 
				0.3f, 
				editorArea);
	}
}
