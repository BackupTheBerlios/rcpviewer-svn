/**
 * 
 */
package org.essentialplatform.louis.factory.reference;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Alerted whenever the display value for the part is changed.
 * @author Mike
 */
public interface IReferencePartDisplayListener {

	/**
	 * Event when the display value is changed - could be <code>null</code>
	 * @param value
	 */
	public void displayValueChanged( IDomainObject<?> value  );
}
