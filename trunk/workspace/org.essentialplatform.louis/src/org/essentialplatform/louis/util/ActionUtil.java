package org.essentialplatform.louis.util;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.essentialplatform.louis.LouisPlugin;


/**
 * Static methods for mucking around with actions.
 * 
 * @author Dan Haywood
 */
public final class ActionUtil {
	
	// prevent instantiation
	private ActionUtil() {
		super();
	}

	public static void setupLabelAndImage(IAction action, String text, String image) {
		action.setImageDescriptor(
				ImageUtil.getImageDescriptor( 
						LouisPlugin.getDefault(), 
						image ) );
		action.setToolTipText( LouisPlugin.getResourceString( text ) );
	}


}
