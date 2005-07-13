package de.berlios.rcpviewer.gui.dnd;

import java.util.Date;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

/**
 * Used for transferring int's within the application.
 * @author Mike
 *
 */
public class DateTransfer extends ByteArrayTransfer {
	
	private static final String DATE_TYPE_NAME
		= DateTransfer.class.getName();
	private static final int DATE_TYPE_ID
		= registerType ( DATE_TYPE_NAME );
	
	private static DateTransfer __instance = new DateTransfer();

	/**
	 * Returns the singleton instance of the IntegerTransfer class.
	 *
	 * @return the singleton instance of the IntegerTransfer class
	 */
	public static DateTransfer getInstance () {
		return __instance;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		if ( object == null || !( object instanceof Date ) ) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		else {
			super.javaToNative( 
					String.valueOf( (Long)((Date)object).getTime() ).getBytes(), 
					transferData );
		}
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[])super.nativeToJava(transferData);
		return new Date( new Long( new String( bytes ) ) );
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	protected String [] getTypeNames () {
		return new String [] { DATE_TYPE_NAME };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	protected int [] getTypeIds () {
		return new int [] { DATE_TYPE_ID };
	}
	
	// private constructor 
	private DateTransfer() {
		super();
	}
	

}
