package com.ibm.developerworks.google;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.ibm.developerworks.google.views.BrowserView;
import com.ibm.developerworks.google.views.SearchView;

public class GooglePerspective implements IPerspectiveFactory
{
    public static final String ID = "com.ibm.developerworks.google.GooglePerspective";

    public void createInitialLayout(IPageLayout layout)
    {
        layout.setEditorAreaVisible(false);
        layout.addView(SearchView.ID, IPageLayout.BOTTOM, new Float(0.60)
                .floatValue(), IPageLayout.ID_EDITOR_AREA);
        layout.addView(BrowserView.ID, IPageLayout.TOP, new Float(0.40)
                .floatValue(), IPageLayout.ID_EDITOR_AREA);
    }
}