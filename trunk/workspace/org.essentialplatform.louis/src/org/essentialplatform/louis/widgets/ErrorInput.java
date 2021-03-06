/**
 * 
 */
package org.essentialplatform.louis.widgets;

import org.essentialplatform.louis.LouisPlugin;

/**
 * Dummy input for viewers when an error occurs.
 * @author Mike
 */
public class ErrorInput {
	
	private final String _msg;

	/**
	 * No-arg constructor uses default message.
	 */
	public ErrorInput() {
		this ( LouisPlugin.getResourceString( "ErrorInput.DefaultMsg" ) ); //$NON-NLS-1$
	}
	
	/**
	 * Constructor takes message to display.
	 * @param msg
	 */
	public ErrorInput( String msg ) {
		if ( msg == null ) throw new IllegalArgumentException();
		_msg = msg;
	}
	
	/**
	 * Return the message to display.
	 * @return
	 */
	public String getMessage() {
		return _msg;
	}

}
