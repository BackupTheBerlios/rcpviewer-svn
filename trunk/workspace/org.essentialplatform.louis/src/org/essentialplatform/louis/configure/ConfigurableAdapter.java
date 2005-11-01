/**
 * 
 */
package org.essentialplatform.louis.configure;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract base class for <code>IConfigurable</code> implementations.
 * @author Mike
 */
public abstract class ConfigurableAdapter implements IConfigurable {
	
	private static final IConfigurableListener[] EMPTY_ARRAY
		= new IConfigurableListener[0];
	
	private List<IConfigurableListener> _listeners = null;

	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.IGuiConfigurator#addListener(org.essentialplatform.gui.factory.IGuiConfigurator.IConfigurableListener)
	 */
	public boolean addConfigurableListener(IConfigurableListener listener) {
		if ( listener == null ) throw new IllegalArgumentException();
		if ( _listeners == null ) {
			_listeners = new ArrayList<IConfigurableListener>();
		}
		return _listeners.add( listener );
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.IGuiConfigurator#removeListener(org.essentialplatform.gui.factory.IGuiConfigurator.IConfigurableListener)
	 */
	public boolean removeConfigurableListener(IConfigurableListener listener) {
		if ( listener == null ) throw new IllegalArgumentException();
		boolean ok =  _listeners.remove( listener );
		if ( _listeners.isEmpty() ) _listeners = null;
		return ok;
	}
	
	/**
	 * Allows access to list of listeners for subclasses.
	 * @return
	 */
	protected IConfigurableListener[] getListeners() {
		if ( _listeners == null ) return EMPTY_ARRAY;
		return _listeners.toArray( EMPTY_ARRAY );
	}

}
