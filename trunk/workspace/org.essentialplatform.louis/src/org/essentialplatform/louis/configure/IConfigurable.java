/**
 * 
 */
package org.essentialplatform.louis.configure;



/**
 * Offers methods that allow the implementor to be configured
 * @author Mike
 */
public interface IConfigurable extends Runnable {
	
	/**
	 * Method must:
	 * <ol>
	 * <li>allow the user to configure the gui
	 * <li>applies this configuration to the parent gui part
	 * </ol>
	 */
	public void run();
	
	/**
	 * Adds a listener
	 * @param listener
	 * @return whether added ok
	 */
	public boolean addConfigurableListener( IConfigurableListener listener );
	
	/**
	 * Removes a listener
	 * @param listener
	 * @return whether removed ok
	 */
	public boolean removeConfigurableListener( IConfigurableListener listener );
	
	/**
	 * Listens whenever a change made to the configuration
	 * @author Mike
	 *
	 */
	public interface IConfigurableListener {
		
		/**
		 * Event fired whenever the reported configuration is changed.
		 * @param config
		 */
		public void configurationChanged( IConfigurable config );
	}
}
