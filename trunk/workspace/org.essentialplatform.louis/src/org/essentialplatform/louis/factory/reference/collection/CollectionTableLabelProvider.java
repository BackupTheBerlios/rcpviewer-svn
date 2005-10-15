/**
 * 
 */
package org.essentialplatform.louis.factory.reference.collection;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.essentialplatform.louis.LouisPlugin;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * @author Mike
 *
 */
class CollectionTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {
	
	private final  List<EAttribute> _attributes;
	
	CollectionTableLabelProvider( List<EAttribute> attributes ) {
		assert attributes != null;
		_attributes = attributes;
	}

	/**
	 * Always <code>null</code>
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		if ( element == null ) throw new IllegalArgumentException();
		if ( !(element instanceof IDomainObject ) ) throw new IllegalArgumentException();
		if ( columnIndex == 0 ) {
			return LouisPlugin.getText( element );
		}
		else {
			return LouisPlugin.getText(
					((IDomainObject<?>)element).getAttribute( 
							_attributes.get( --columnIndex ) ).get() );
		}
	}

}
