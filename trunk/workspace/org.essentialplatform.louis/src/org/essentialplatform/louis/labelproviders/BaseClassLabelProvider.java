package org.essentialplatform.louis.labelproviders;

import java.math.BigDecimal;
import java.util.Date;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.ImageUtil;
import org.essentialplatform.louis.util.PrimitiveUtil;

/**
 * Descriptions and icons for primitive and some other base classes.
 * @author Mike
 */
class BaseClassLabelProvider extends LabelProvider implements ILouisLabelProvider{
	
	/**
	 * Package-private constructor.
	 */
	BaseClassLabelProvider() {
		super();
	}


	/**
	 * Returns descriptions.
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if ( element instanceof Class ) {
			String key = null;
			if ( ((Class)element).isPrimitive() 
					|| PrimitiveUtil.isWrapperClass( (Class)element )) {
				if ( Boolean.class.equals( element ) || boolean.class.equals( element ) ) {
					key = "BooleanToolTip"; //$NON-NLS-1$
				}
				else if ( Byte.class.equals( element ) || byte.class.equals( element ) ) {
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
			}
			else if ( String.class.equals( element ) ) {
				key = "StringToolTip"; //$NON-NLS-1$
			}
			else if ( Date.class.equals( element ) ) {
				key = "DateToolTip"; //$NON-NLS-1$
			}
			else if ( BigDecimal.class.equals( element ) ) {
				key = "BigDecimalToolTip"; //$NON-NLS-1$
			}
			if ( key != null ) {
				return LouisPlugin.getResourceString( 
						"BaseClassLabelProvider." + key ); //$NON-NLS-1$
			}
		}
		return null;
	}
	


	/**
	 * Returns icons for the classes.
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if ( element instanceof Class ) {
			String icon = null;
			if ( ((Class)element).isPrimitive()
					|| PrimitiveUtil.isWrapperClass( (Class)element )) {
				if ( Boolean.class.equals( element ) || boolean.class.equals( element ) ) {
					icon = "boolean.png"; //$NON-NLS-1$
				}
				else if ( Byte.class.equals( element ) || byte.class.equals( element ) ) {
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
			}
			else if ( String.class.equals( element ) ) {
				icon = "string.png"; //$NON-NLS-1$
			}
			else if ( Date.class.equals( element ) ) {
				icon = "date.png"; //$NON-NLS-1$
			}
			else if ( BigDecimal.class.equals( element ) ) {
				icon = "bigdecimal.png"; //$NON-NLS-1$
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