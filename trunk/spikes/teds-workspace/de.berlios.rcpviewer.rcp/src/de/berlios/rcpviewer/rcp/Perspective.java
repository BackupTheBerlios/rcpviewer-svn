package de.berlios.rcpviewer.rcp;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.IViewLayout;

import de.berlios.rcpviewer.views.RcpViewerActionsView;
import de.berlios.rcpviewer.views.RcpViewerShortcutsView;

public class Perspective implements IPerspectiveFactory {
	
	public static final String ID = Perspective.class.getName();

	public void createInitialLayout(IPageLayout layout) {
		
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		IFolderLayout shortcutsFolder=  layout.createFolder(
				"de.berlios.rcpviewer.shortcuts",
				IPageLayout.LEFT, 
				0.3f, 
				editorArea);
		shortcutsFolder.addView(RcpViewerShortcutsView.class.getName());
		IViewLayout shortcutsLayout= layout.getViewLayout("de.berlios.rcpviewer.shortcuts");
		shortcutsLayout.setCloseable(false);
		
		IFolderLayout actionsFolder=  layout.createFolder(
				"de.berlios.rcpviewer.actions",
				IPageLayout.BOTTOM, 
				0.5f, 
				"de.berlios.rcpviewer.shortcuts");
		actionsFolder.addView(RcpViewerActionsView.class.getName());
		IViewLayout actionsLayout= layout.getViewLayout("de.berlios.rcpviewer.actions");
		actionsLayout.setCloseable(false);

		IPlaceholderFolderLayout resultsFolder=  layout.createPlaceholderFolder(
				"de.berlios.rcpviewer.results",
				IPageLayout.BOTTOM, 
				0.7f, 
				editorArea);
		resultsFolder.addPlaceholder("org.eclipse.search.ui.views.SearchView");
		resultsFolder.addPlaceholder("org.eclipse.search.SearchResultView");
	}
}
