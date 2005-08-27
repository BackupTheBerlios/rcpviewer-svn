package de.berlios.rcpviewer.gui.util;

import java.util.Arrays;


/**
 * Static methods for handling <code>Strings</code>
 * @author Mike
 */
public class StringUtil {
	
	/**
	 * Left-pads the passed <code>String</code> with whitespace up to the 
	 * passed length.  If the <code>String</code> is already that length, 
	 * or longer, simply return the <code>String</code>.
	 * @param s
	 * @param length
	 * @return
	 */
	public static String padLeft( String s, int length ) {
		if ( s == null ) throw new IllegalArgumentException();
		if ( length < 1 ) throw new IllegalArgumentException();
		int numPads = length - s.length();
		if ( numPads < 1 ) return s;
		char[] pad = new char[numPads];
		Arrays.fill( pad, ' ' );
		return new String( pad ) + s;
	}

	// prevent instantiation
	private StringUtil() {
		super();
	}

}
