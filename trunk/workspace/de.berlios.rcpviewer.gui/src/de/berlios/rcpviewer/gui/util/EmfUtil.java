package de.berlios.rcpviewer.gui.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;

/**
 * Static methods for helping with EMF constructs
 * @author Mike
 */
public class EmfUtil {
	
	public static enum SortType { ALPHABETICAL }
	
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
	
	/**
	 * Sorts the passed list of structural elements using one of the following 
	 * algorithms:
	 * <ul>
	 * <li><code>ALPHABETICAL</code> - alphabetically by name
	 * </ul>
	 * TODO - extend this to all EStructuralElements and use annotations -
	 * order / importance etc.
	 * @param attributes
	 * @return
	 */
	public static <T extends EStructuralFeature> List<T> sort( 
			List<T> features, SortType sortType ) {
		if ( features == null ) throw new IllegalArgumentException();
		if ( sortType == null ) throw new IllegalArgumentException();
		if ( features.size() < 2 ) return features;
		
		Comparator<T> comparator;
		switch( sortType ) {
			case ALPHABETICAL:
				comparator = new Comparator<T>(){
					public int compare(T o1, T o2) {
						return o1.getName().compareTo( o2.getName() );
					}
				};
				break;
			default:
				throw new IllegalArgumentException();
		}
		Collections.sort( features, comparator );
		return features;
	}
	

	// prevent instantiation
	private EmfUtil() {
		super();
	}

}
