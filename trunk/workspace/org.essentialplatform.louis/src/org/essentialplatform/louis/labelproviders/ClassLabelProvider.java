package org.essentialplatform.louis.labelproviders;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.ImageUtil;

/**
 * Descriptions and icons for primitive classes.
 * 
 * @author Mike
 */
class ClassLabelProvider extends LabelProvider implements ILouisLabelProvider{
	
	/**
	 * Package-private constructor.
	 */
	ClassLabelProvider() {
		super();
	}

	/*
	 * @see org.essentialplatform.louis.labelproviders.ILouisLabelProvider#init()
	 */
	public void init() {
		// does nothing
	}
	
	/**
	 * Returns descriptions for primitive classes.
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if ( element instanceof Class && ((Class)element).isPrimitive() ) {
			String key = null;
			if ( Byte.class.equals( element ) || byte.class.equals( element ) ) {
				key = "ByteToolTip"; //$NON-NLS-1$
			}
			else if ( Character.class.equals( element ) || char.class.equals( element ) ) {
				key = "CharToolTip"; //$NON-NLS-1$
			}
			else if ( Short.class.equals( element ) || short.class.equals( element ) ) {
				key = "ShortToolTip"; //$NON-NLS-1$
			}
			else if ( Integer.class.equals( element ) || int.class.equals( element ) ) {
				key = "IntToolTip"; //$NON-NLS-1$
			}
			else if ( Long.class.equals( element ) || long.class.equals( element ) ) {
				key = "IntToolTip"; //$NON-NLS-1$
			}
			else if ( Float.class.equals( element ) || float.class.equals( element ) ) {
				key = "IntToolTip"; //$NON-NLS-1$
			}
			else if ( Double.class.equals( element ) || double.class.equals( element ) ) {
				key = "IntToolTip"; //$NON-NLS-1$
			}
			if ( key != null ) {
				return LouisPlugin.getResourceString( 
						"PrimitiveClassLabelProvider." + key ); //$NON-NLS-1$
			}
		}
		return null;
	}
	


	/**
	 * Returns icons for primitive classes
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if ( element instanceof Class && ((Class)element).isPrimitive() ) {
			String icon = null;
			if ( Byte.class.equals( element ) || byte.class.equals( element ) ) {
				icon = "byte.png"; //$NON-NLS-1$
			}
			else if ( Character.class.equals( element ) || char.class.equals( element ) ) {
				icon = "char.png"; //$NON-NLS-1$
			}
			else if ( Short.class.equals( element ) || short.class.equals( element ) ) {
				icon = "short.png"; //$NON-NLS-1$
			}
			else if ( Integer.class.equals( element ) || int.class.equals( element ) ) {
				icon = "int.png"; //$NON-NLS-1$
			}
			else if ( Long.class.equals( element ) || long.class.equals( element ) ) {
				icon = "long.png"; //$NON-NLS-1$
			}
			else if ( Float.class.equals( element ) || float.class.equals( element ) ) {
				icon = "float.png"; //$NON-NLS-1$
			}
			else if ( Double.class.equals( element ) || double.class.equals( element ) ) {
				icon = "double.png"; //$NON-NLS-1$
			}
			if ( icon != null ) {
				return ImageUtil.getImage(
						LouisPlugin.getDefault(),
						"icons/" + icon ); //$NON-NLS-1$
			}
		}
		return null;
	}

}