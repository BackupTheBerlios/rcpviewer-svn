package com.intalgent.pdfviewer.menubar;

import com.intalgent.pdfviewer.PDFViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;

import org.eclipse.swt.widgets.Display;


public class AboutAction extends Action
{
    ApplicationWindow window;

    public AboutAction(ApplicationWindow w)
    {
        window = w;
        this.setText("&About PDF Viewer");

        this.setToolTipText("About the PDF Viewer");
    }

    public void run()
    {
        MessageDialog.openInformation(Display.getCurrent().getActiveShell(),
            "About PDF Viewer", "IBM developerWorks");
        ((PDFViewer) window).showPdfControl();
    }
}
