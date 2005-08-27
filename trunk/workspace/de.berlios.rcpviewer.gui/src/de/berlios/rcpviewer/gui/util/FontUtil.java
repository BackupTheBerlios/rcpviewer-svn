package de.berlios.rcpviewer.gui.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Static methods for handling fonts.
 * <br>Note this stores static state to prevent innecessary <code>GC</code>
 * instantiations.
 * @author Mike
 */
public class FontUtil {
	
	public enum CharWidthType { AVERAGE, MAX, SAFE }
	
	private static final Map<Font,int[]> CACHED_WIDTHS
		= new HashMap<Font,int[]>();
	
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
    
    /**
     * Returns character width for sizing / placing labels.
     * <br>The second argument can be one of the following values:
     * <ul>
     * <li><code>AVERAGE</code> - as it says
     * <li><code>MAX</code> - as it says
     * <li><code>SAFE</code> - 	using the average character width often leads 
     * to clipping, whilst using the max character width results in excess 
	 * whitespace.  The value returned by this method is an experience-based
	 * compromise between these two values.
     * </ul>
     * @param drawable
     * @param type
     * @return
     */
    public static int getCharWidth( Composite composite, CharWidthType type ) {
    	if( composite == null ) throw new IllegalArgumentException();
    	if( type == null ) throw new IllegalArgumentException();
    	int[] widths = CACHED_WIDTHS.get( composite.getFont() );
    	if ( widths == null ) {
    		widths = new int[3];
    		GC gc = null;
    		try {
    			gc = new GC( composite );
    			widths[0] = gc.getFontMetrics().getAverageCharWidth();
    			widths[1] = gc.getCharWidth( 'W' );
    			widths[2] = (int)( (2*widths[0] + widths[1])/3 );
    		}
    		finally {
    			gc.dispose();
    		}
    		CACHED_WIDTHS.put( composite.getFont(), widths );
    	}
    	assert widths != null;
    	assert widths.length == 3;
    	switch( type ) {
    		case AVERAGE:
    			return widths[0];
    		case MAX :
    			return widths[1];
    		case SAFE:
    			return widths[2];
    		default:
    			assert false;
    			return Integer.MIN_VALUE;
    	}
    }
	
	

	// prevent instantiation
	private FontUtil() {
		super();
	}

}
