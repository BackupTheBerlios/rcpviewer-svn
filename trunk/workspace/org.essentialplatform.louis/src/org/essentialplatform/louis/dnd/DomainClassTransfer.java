package org.essentialplatform.louis.dnd;


import org.essentialplatform.runtime.session.IDomainObject;

/**
 * Used for transferring an arbitary class within the application.
 * @author Mike
 */
public class DomainClassTransfer extends GenericTransfer {

	/**
	 * Due to autoboxing must use wrapper class.
	 * @param clazz
	 */
	DomainClassTransfer( Class clazz ) {
		super( clazz );
	}
	
	
	@Override
	protected boolean isObjectValid(Object object, Class<?> transferClass) {
		if ( object instanceof IDomainObject<?> ) {
			return transferClass.equals( 
					((IDomainObject<?>)object).getPojo().getClass() );
		}
		return false;
	}
}
