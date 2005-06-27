package de.berlios.rcpviewer.gui.util;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * Static methods for handling fonts.
 * @author Mike
 */
public class FontUtil {
	
	private static final String LABEL_FONT_KEY
		= FontUtil.class.getSimpleName() + ".LabelFontKey" ;  //$NON-NLS-1$
	
	/**
	 * Defines a standard label font - the default font but in bold.
	 * @return
	 */
    public static Font getLabelFont() {
		assert Display.getCurrent() != null;
		// lazy instantiator
		if ( !JFaceResources.getFontRegistry().hasValueFor( LABEL_FONT_KEY ) ) {
			Font original = JFaceResources.getDefaultFont();
			FontData data = new FontData(
					original.getFontData()[0].getName(),
					original.getFontData()[0].getHeight(),
					SWT.BOLD );
			JFaceResources.getFontRegistry().put( 
					LABEL_FONT_KEY, new FontData[]{ data } );
		}
		return JFaceResources.getFontRegistry().get( LABEL_FONT_KEY );
    }
	
	

	// prevent instantiation
	private FontUtil() {
		super();
	}

}
