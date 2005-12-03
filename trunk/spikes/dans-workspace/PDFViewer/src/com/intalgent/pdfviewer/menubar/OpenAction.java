package com.intalgent.pdfviewer.menubar;

import com.intalgent.pdfviewer.PDFViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;

import org.eclipse.swt.widgets.FileDialog;


public class OpenAction extends Action
{
    ApplicationWindow window;

    public OpenAction(ApplicationWindow w)
    {
        window = w;
        this.setText("&Open...");

        this.setToolTipText("Open a PDF document.");
    }

    public void run()
    {
        FileDialog dialog = new FileDialog(window.getShell());
        dialog.open();

        String file = dialog.getFilterPath() + "\\" + dialog.getFileName();

        PDFViewer viewer = ((PDFViewer) window);

        viewer.loadFile(file);
    }
}
