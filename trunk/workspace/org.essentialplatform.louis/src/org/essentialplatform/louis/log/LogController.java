/**
 * 
 */
package org.essentialplatform.louis.log;

import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.essentialplatform.louis.LouisPlugin;

/**
 * Sets up and controls logging.
 * @author Mike
 */
public class LogController implements IPropertyChangeListener {
	
	public static final String SHOW_CONSOLE_KEY = "LogController.ShowConsole";
	
	private MessageConsoleAppender _currentAppender = null;
	
	/**
	 * Constructor starts listening on preference store and sets up initial
	 * logging state
	 */
	public LogController() {
		IPreferenceStore store = LouisPlugin.getDefault().getPreferenceStore();
		store.addPropertyChangeListener( this );
		if ( store.getBoolean( SHOW_CONSOLE_KEY ) ) {
			addAppender();
		}
		else {
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
		if( SHOW_CONSOLE_KEY.equals( event.getProperty() ) ) {
			assert event.getNewValue() instanceof Boolean;
			if ( ((Boolean)event.getNewValue()).booleanValue() ) {
				if ( _currentAppender == null ) addAppender();
			}
			else {
				if ( _currentAppender != null ) removeAppender();
			}
		}
		
	}
	
	// as it says
	private void addAppender() {
		assert _currentAppender == null;
		_currentAppender = new MessageConsoleAppender();
		Logger.getRootLogger().addAppender( _currentAppender );
	}
	
	// as it says
	private void removeAppender() {
		assert _currentAppender != null;
		_currentAppender.close();
		Logger.getRootLogger().removeAppender( _currentAppender );
		_currentAppender = null;
	}

}
