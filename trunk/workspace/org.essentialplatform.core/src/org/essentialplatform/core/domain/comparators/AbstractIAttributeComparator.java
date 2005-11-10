package org.essentialplatform.core.domain.comparators;

import java.util.Comparator;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;


/**
 * Compare attributes according to an annotation. 
 * 
 * @author dkhaywood
 *
 */
public abstract class AbstractIAttributeComparator implements Comparator<IDomainClass.IAttribute> {

	
	/**
	 * Compare according to {@link #getPosition(IAttribute)}.
	 * 
	 * @param attribute0
	 * @param attribute1
	 * @return
	 */
	public final int compare(IAttribute attribute0, IAttribute attribute1) {
		Integer position0 = getPosition(attribute0); 
		Integer position1 = getPosition(attribute1); 
		
		if (position0 != null && position1 == null) {
			// if have positioned #0 but not #1, then return #0 first.
			return -1; 
		} else if (position0 == null && position1 != null) {
			// if have positioned #1 but not #0, then return #1 first.
			return 1; 
		} else if (position0 == null || position1 == null) {
			// neither have been positioned
			return compareByName(attribute0, attribute1);
		} else {
			// both have been positioned
			if (position0.intValue() == position1.intValue()) {
				// positioned at same place
				return compareByName(attribute0, attribute1);
			} else
			if (position0.intValue() < position1.intValue()) {
				return -1;
			} else {
				// position0 > position1
				return +1;
			}
		}
	}
	
	protected abstract Integer getPosition(IAttribute attribute);

	private int compareByName(IDomainClass.IAttribute attribute0, IDomainClass.IAttribute attribute1) {
		return attribute0.getName().compareTo(attribute1.getName());
	}
	

}
