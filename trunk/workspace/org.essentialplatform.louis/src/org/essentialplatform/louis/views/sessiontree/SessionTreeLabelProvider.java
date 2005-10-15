package org.essentialplatform.louis.views.sessiontree;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.ImageUtil;
import org.essentialplatform.louis.widgets.ErrorInput;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Label provider for session tree.
 * @author Mike
 */
class SessionTreeLabelProvider extends LabelProvider 
		implements ITableLabelProvider {
	
	private static final Point IMAGE_SIZE = new Point( 12, 12 );
	
	/**
	 * This method called if tree has single column.
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return getColumnImage( element, 0 );
	}

	/**
	 * This method called if tree has single column.
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		return getColumnText( element, 0 ) ;
	}

	/**
	 * Class icon if class and first column, else nothing.
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		if ( columnIndex == 0
				&& element instanceof IDomainClass ) {
			return ImageUtil.resize( 
						ImageUtil.getImage( (IDomainClass)element ), 
						IMAGE_SIZE ) ;
		}
		return null;
	}

	/**
	 * Class or instance name
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		if ( element instanceof IDomainClass<?> ) {
			return ((IDomainClass<?>)element).getName();
		}
		else if ( element instanceof IDomainObject<?> ) {
			return LouisPlugin.getText( element );
		}
		else if ( element instanceof ErrorInput ) {
			return ((ErrorInput)element).getMessage();
		}
		else {
			throw new IllegalArgumentException();
		}
	}

}
