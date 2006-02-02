/**
 * 
 */
package org.essentialplatform.louis.log;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SocketAppender;
import org.apache.log4j.varia.NullAppender;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.essentialplatform.louis.LouisPlugin;

/**
 * Sets up and controls logging.
 * 
 * <p>
 * Instantiate once and throw away.  It registers itself with the 
 * {@link IPreferenceStore} which will hold a 
 * strong reference to it.  (Does this constitute a memory leak?)
 * 
 * <p>
 * TODO: factor out the common stuff here into IAppenderController (currently empty).
 * 
 * @author Mike
 */
public class LogController implements IPropertyChangeListener {
	
	/**
	 * Constructor starts listening on preference store and sets up initial
	 * logging state
	 */
	public LogController() {
		IPreferenceStore store = LouisPlugin.getDefault().getPreferenceStore();
		store.setDefault(SOCKET_APPENDER_HOST_NAME_KEY, SOCKET_APPENDER_DEFAULT_HOST_NAME);
		store.setDefault(SOCKET_APPENDER_PORT_KEY, SOCKET_APPENDER_DEFAULT_PORT);
		
		store.addPropertyChangeListener( this );
		boolean appenderAdded = false;
		
		if (store.getBoolean(CONSOLE_APPENDER_KEY)) {
			addConsoleAppender();
			appenderAdded = true;
		}
		if (store.getBoolean(SOCKET_APPENDER_KEY)){
			addSocketAppender();
			appenderAdded = true;
		}
		if (!appenderAdded) {
			// prevent 'no appender' error messages by log4j
			Logger.getRootLogger().addAppender( new NullAppender() );
		}
	}

	
	/**
	 * Adds/removes appenders based on changes to preferences
	 * @see org.eclipse.core.runtime.Preferences$IPropertyChangeListener#propertyChange(org.eclipse.core.runtime.Preferences.PropertyChangeEvent)
	 */
	public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
		assert event != null;
		
		if( CONSOLE_APPENDER_KEY.equals( event.getProperty() ) ) {
			boolean appendToConsole = ((Boolean)event.getNewValue()).booleanValue();
			assert event.getNewValue() instanceof Boolean;
			if ( appendToConsole ) {
				if ( _consoleAppender == null ) {
					addConsoleAppender();
				}
			}
			else {
				if ( _consoleAppender != null ) {
					removeConsoleAppender();
				}
			}
		}

		if( SOCKET_APPENDER_KEY.equals( event.getProperty() ) ) {
			boolean appendToSocket = ((Boolean)event.getNewValue()).booleanValue();
			assert event.getNewValue() instanceof Boolean;
			if ( appendToSocket ) {
				if ( _socketAppender == null ) {
					addSocketAppender();
				}
			}
			else {
				if ( _socketAppender != null ) {
					removeSocketAppender();
				}
			}
		}
	}

	///////////////////////////////////////////////////////////////////
	// ConsoleAppender
	///////////////////////////////////////////////////////////////////

	public static final String CONSOLE_APPENDER_KEY = "LogController.ShowConsole";

	private MessageConsoleAppender _consoleAppender = null;

	private void addConsoleAppender() {
		assert _consoleAppender == null;
		_consoleAppender = new MessageConsoleAppender();
		Logger.getRootLogger().addAppender( _consoleAppender );
	}
	
	private void removeConsoleAppender() {
		assert _consoleAppender != null;
		_consoleAppender.close();
		Logger.getRootLogger().removeAppender( _consoleAppender );
		_consoleAppender = null;
	}

	///////////////////////////////////////////////////////////////////
	// SocketAppender
	///////////////////////////////////////////////////////////////////

	public static final String SOCKET_APPENDER_KEY = "LogController.SocketAppender";
	public static final String SOCKET_APPENDER_HOST_NAME_KEY = "LogController.SocketAppenderHostName";
	public static final String SOCKET_APPENDER_PORT_KEY = "LogController.SocketAppenderPort";
	
	public static final String SOCKET_APPENDER_DEFAULT_HOST_NAME = "localhost";
	public static final int SOCKET_APPENDER_DEFAULT_PORT = 4445;

	private SocketAppender _socketAppender = null;

	private void addSocketAppender() {
		assert _socketAppender == null;

		IPreferenceStore store = LouisPlugin.getDefault().getPreferenceStore();
		String hostname = store.getString(SOCKET_APPENDER_HOST_NAME_KEY); 
		int port = store.getInt(SOCKET_APPENDER_PORT_KEY); 
		Logger.getRootLogger().addAppender( new SocketAppender(hostname, port));

		_socketAppender = new SocketAppender(hostname, port);
		Logger.getRootLogger().addAppender( _socketAppender );
	}
	
	private void removeSocketAppender() {
		assert _socketAppender != null;
		_socketAppender.close();
		Logger.getRootLogger().removeAppender( _socketAppender );
		_consoleAppender = null;
	}
	
}
