package com.ibm.developerworks.google.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class BrowserView extends ViewPart
{
    public static final String ID = "com.ibm.developerworks.google.views.BrowserView";

    public static Browser browser;

    public void createPartControl(Composite parent)
    {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.marginHeight = 5;
        gridLayout.marginWidth = 5;
        parent.setLayout(gridLayout);

        browser = new Browser(parent, SWT.NONE);

        browser.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.GRAB_VERTICAL | GridData.FILL_HORIZONTAL
                | GridData.FILL_VERTICAL));
        browser.setUrl("about:");

    }

    public void setFocus()
    {
        browser.setFocus();

    }
}