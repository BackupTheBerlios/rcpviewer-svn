package org.essentialplatform.louis.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;

import de.berlios.rcpviewer.domain.EmfAnnotations;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.progmodel.extended.ExtendedProgModelConstants;
import de.berlios.rcpviewer.progmodel.extended.RelativeOrder;

/**
 * Static methods for helping with EMF constructs
 * @author Mike
 */
public class EmfUtil {
	
	public static enum SortType { ALPHABETICAL, ANNOTATION }
	
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
	 * As its says.
	 * @param ref
	 * @return
	 */
	public static final IRuntimeDomainClass<?> getCollectionDomainType( 
			EReference ref ) {
		if( ref == null ) throw new IllegalArgumentException();
		return (IRuntimeDomainClass<?>)RuntimeDomain.instance().lookupNoRegister( 
					(Class<?>)ref.getEType().getInstanceClass() );		
	}
	
	/**
	 * As its says.
	 * @param ref
	 * @return
	 */
	public static final IRuntimeDomainClass<?> getContainerDomainType( 
			EReference ref ) {
		if( ref == null ) throw new IllegalArgumentException();
		Class<?> containerPojoType = ((EClassifier)ref.eContainer()).getInstanceClass();
		return (IRuntimeDomainClass<?>)RuntimeDomain.instance().lookupNoRegister( 
					containerPojoType );
	}
	
	/**
	 * Whether elements can be added to the passed reference
	 * @param ref
	 * @return
	 */
	public static final boolean canAddTo( EReference ref ) {
		return( getContainerDomainType( ref ).getAssociatorFor( ref ) != null );
	}	
	
	/**
	 * Whether elements can be removed from the passed reference
	 * @param ref
	 * @return
	 */
	public static final boolean canRemoveFrom( EReference ref ) {
		return( getContainerDomainType( ref ).getDissociatorFor( ref ) != null );
	}	
	
	/**
	 * Sorts the passed list of structural elements using one of the following 
	 * algorithms:
	 * <ul>
	 * <li><code>ALPHABETICAL</code> - alphabetically by name
	 * <li><code>ANNOTATION</code> - uses the {@link Order} annotation, then 
	 * falls back on alphabetical sorting.
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
			case ANNOTATION:
				comparator = new AttributeComparator<T>();
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
	
	/* private classes */
	
	private static class AttributeComparator<T extends EStructuralFeature> 
			implements Comparator<T> {
		
		private final EmfAnnotations emfAnnotations = new EmfAnnotations();
		
		/**
		 * Compares according to the {@link Order} attribute, if known, or
		 * the name if not known.
		 * 
		 * @param arg0
		 * @param arg1
		 * @return
		 */
		public int compare(T feature0, T feature1) {
			
			Map<String,String> attributeDetails0 = 
				emfAnnotations.getAnnotationDetails(feature0, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
			Map<String,String> attributeDetails1 = 
				emfAnnotations.getAnnotationDetails(feature1, ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE);
			
			String positionedAtStr0 = 
				attributeDetails0.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_RELATIVE_ORDER_KEY);
			String positionedAtStr1 = 
				attributeDetails1.get(ExtendedProgModelConstants.ANNOTATION_ATTRIBUTE_RELATIVE_ORDER_KEY);
			
			
			if (positionedAtStr0 != null && positionedAtStr1 == null) {
				// if have positioned #0 but not #1, then return #0 first.
				return -1; 
			} else if (positionedAtStr0 == null && positionedAtStr1 != null) {
				// if have positioned #1 but not #0, then return #1 first.
				return 1; 
			} else if (positionedAtStr0 == null || positionedAtStr1 == null) {
				// neither have been positioned
				return compareByName(feature0, feature1);
			} else {
				// both have been positioned
				int positionedAt0 = Integer.parseInt(positionedAtStr0);
				int positionedAt1 = Integer.parseInt(positionedAtStr1);
				if (positionedAt0 == positionedAt1) {
					// positioned at same place
					return compareByName(feature0, feature1);
				}
				// compare according to their position.
				return positionedAt0 - positionedAt1;
			}
		}
		
		private int compareByName(T feature0, T feature1) {
			return feature0.getName().compareTo(feature1.getName());
		}
		
	}

}
