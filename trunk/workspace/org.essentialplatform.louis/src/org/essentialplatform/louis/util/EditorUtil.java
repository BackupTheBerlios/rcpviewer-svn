package org.essentialplatform.louis.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.essentialplatform.louis.LouisPlugin;


/**
 * Static methods for mucking around with dialogs.
 * 
 * @author Dan Haywood
 */
public final class EditorUtil {
	
	// prevent instantiation
	private EditorUtil() {
		super();
	}

	public static IEditorPart getActiveEditor() {
		IWorkbenchWindow window= LouisPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page= window.getActivePage();
			if (page != null) {
				return page.getActiveEditor();
			}
		}
		return null;
	}

}
