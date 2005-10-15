package org.essentialplatform.louis.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
	
	/**
	 * Allows printf-style substitution.
	 * @param s
	 * @param args
	 * @return
	 * @see java.io.PrintStream#printf(java.lang.String, java.lang.Object[])
	 */
	public static String printf( String s, Object...args ) {
		if( s == null ) throw new IllegalArgumentException();
		if ( args == null ) return s;
		 ByteArrayOutputStream out = new ByteArrayOutputStream( s.length() );
		 PrintStream ps = null;
		 try {
			 ps = new PrintStream( out );
			 ps.printf( s, args );
			 ps.close();
			 ps = null;
			 return new String( out.toByteArray() );
		 }
		 finally {
			 if ( ps != null ) ps.close();
		 }
	}

	// prevent instantiation
	private StringUtil() {
		super();
	}

}
