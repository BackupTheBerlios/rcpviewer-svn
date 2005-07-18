package de.berlios.rcpviewer.gui.util;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.ETypedElement;

/**
 * Static methods for helping with EMF constructs
 * @author Mike
 */
public class EmfUtil {
	
	/**
	 * Whether the passed element represents a modifiable entity.
	 * @param element
	 * @return
	 */
	public static boolean isModifiable( ETypedElement element ) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( element instanceof EAttribute ) {
			return ((EAttribute)element).isChangeable();
		}
		if ( element instanceof EParameter ) {
			return true;
		}
		return false;
	}
	
	

	// prevent instantiation
	private EmfUtil() {
		super();
	}

}
