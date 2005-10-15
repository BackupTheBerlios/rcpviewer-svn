package org.essentialplatform.louis.dnd;


import org.essentialplatform.louis.util.PrimitiveUtil;

/**
 * Used for transferring an arbitary class within the application.
 * @author Mike
 */
public class PrimitiveTransfer extends GenericTransfer {

	/**
	 * Due to autoboxing must use wrapper class.
	 * @param clazz
	 */
	PrimitiveTransfer( Class clazz ) {
		super( PrimitiveUtil.getWrapperClass( clazz ) );
	}
}
