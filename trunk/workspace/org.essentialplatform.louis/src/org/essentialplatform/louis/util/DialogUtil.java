package org.essentialplatform.louis.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.essentialplatform.louis.LouisPlugin;


/**
 * Static methods for mucking around with dialogs.
 * 
 * @author Dan Haywood
 */
public final class DialogUtil {
	
	// prevent instantiation
	private DialogUtil() {
		super();
	}

	public static void showNotImplementedDialog() {
		try {
			MessageDialog.openWarning(
					null,
					null,
					LouisPlugin.getResourceString( "NotImplementedJob.Msg" ) ); //$NON-NLS-1$
			return;
		}
		finally {
		}
	}

}
