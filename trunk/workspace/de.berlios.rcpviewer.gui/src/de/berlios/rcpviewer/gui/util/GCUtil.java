package de.berlios.rcpviewer.gui.util;

import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;

/**
 * Static methods for handling <code>GC</code>-related activity.
 * <br>Note this stores static state to prevent innecessary <code>GC</code>
 * instantiations.
 * @author Mike
 */
public class GCUtil {
	
	private static int _avCharWidth = Integer.MIN_VALUE;
	private static int _maxCharWidth = Integer.MIN_VALUE;
	private static int _safeCharWidth = Integer.MIN_VALUE;
	
	/**
	 * As it says.
	 * <br>Note the argument is only actually used the first time this is 
	 * called, however it should always be passed.
	 * @param drawable
	 * @return
	 */
	public static final int getAverageCharWidth( Drawable drawable ){
		if ( _avCharWidth == Integer.MIN_VALUE ) {
			initCachedValues( drawable );
		}
		assert _avCharWidth != Integer.MIN_VALUE;
		return _avCharWidth;
	}
	
	/**
	 * As it says.
	 * <br>Note the argument is only actually used the first time this is 
	 * called, however it should always be passed.
	 * @param drawable
	 * @return
	 */
	public static final int getMaxCharWidth( Drawable drawable ){
		if ( _maxCharWidth == Integer.MIN_VALUE ) {
			initCachedValues( drawable );
		}
		assert _maxCharWidth != Integer.MIN_VALUE;
		return _maxCharWidth;
	}
	
	/**
	 * Using the average character width for sizing labels etc. often leads to
	 * clipping, whilst using the max character width results in excess 
	 * whitespace.  The value returned by this method is an experience-based
	 * compromise between these tow values.
	 * <br>Note the argument is only actually used the first time this is 
	 * called, however it should always be passed.
	 * @param drawable
	 * @return
	 */
	public static final int getSafeCharWidth( Drawable drawable ){
		if ( _safeCharWidth == Integer.MIN_VALUE ) {
			initCachedValues( drawable );
		}
		assert _safeCharWidth != Integer.MIN_VALUE;
		return _safeCharWidth;
	}
	
	// as it says
	private static void initCachedValues( Drawable drawable ){
		if( drawable == null ) throw new IllegalArgumentException();
		GC gc = null;
		try {
			gc = new GC( drawable );
			_avCharWidth = gc.getFontMetrics().getAverageCharWidth();
			_maxCharWidth = gc.getCharWidth( 'W' );
			_safeCharWidth = (int)( (2*_avCharWidth + _maxCharWidth)/3 );
		}
		finally {
			gc.dispose();
		}
	}
	

	private GCUtil() {
		super();
	}

}
