package org.essentialplatform.louis.labelproviders;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.essentialplatform.louis.util.ImageUtil;

import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * Can handle any <code>IDomainObject</code>
 * @author Mike
 */
class DefaultDomainObjectLabelProvider
		extends LabelProvider implements ILouisLabelProvider{
	
	/**
	 * Package-private constructor.
	 */
	DefaultDomainObjectLabelProvider() {
		super();
	}


	/**
	 * Uses <code>title()</code> method if <code>IDomainObject</code>
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if ( element instanceof IDomainObject<?> ) {
			return ((IDomainObject<?>)element).title();
		}
		return null;
	}
	


	/**
	 * Uses whatever image the domain class has defined.
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if ( element instanceof IDomainObject<?> ) {
			return ImageUtil.getImage( 
					((IDomainObject<?>)element).getDomainClass() );
		}
		return null;
	}

}