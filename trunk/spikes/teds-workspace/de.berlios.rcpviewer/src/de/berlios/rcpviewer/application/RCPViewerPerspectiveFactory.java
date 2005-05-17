package de.berlios.rcpviewer.application;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class RCPViewerPerspectiveFactory 
implements IPerspectiveFactory 
{

    public void createInitialLayout(IPageLayout layout) {
        defineActions(layout);
        defineLayout(layout);
    }
    /**
     * Defines the initial actions for a page.  
     */
    public void defineActions(IPageLayout layout) {

        //  Add "new wizards".
        layout.addNewWizardShortcut("de.berlios.rcpviewer.examples.wizards.PurchaseOrderNewWizard");

        // Add "show views".
        //  layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
        //  layout.addShowViewShortcut(IPageLayout.ID_BOOKMARKS);
        //  layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        //  layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
        //  layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
    }

	/**
     * Defines the initial layout for a page.  
     */
    public void defineLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        
        // Bottom left
        //IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, (float)0.50,//$NON-NLS-1$
        //  "topLeft");//$NON-NLS-1$
        //left.addView(IPageLayout.ID_OUTLINE);

        // Bottom right.
        //layout.addView(IPageLayout.ID_TASK_LIST, IPageLayout.RIGHT, (float)0.66, editorArea);
    }

}
