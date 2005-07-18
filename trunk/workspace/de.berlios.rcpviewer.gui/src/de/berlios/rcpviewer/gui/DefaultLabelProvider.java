package de.berlios.rcpviewer.gui;

import org.eclipse.jface.viewers.LabelProvider;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.session.IDomainObject;


/**
 * Used when no other available <code>IDomainObjectLabelProvider</code> 
 * is applicable.
 * @author Mike

 */
class DefaultLabelProvider extends LabelProvider
						   implements IDomainObjectLabelProvider {

	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.gui.fields.IDomainObjectLabelProvider#isApplicable(de.berlios.rcpviewer.domain.IDomainClass)
	 */
	public boolean isApplicable( IDomainClass clazz) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( !( element instanceof IDomainObject ) ) {
			throw new IllegalArgumentException();
		}
		return ((IDomainObject<?>)element).title();
	}
	
	

	
	
}
