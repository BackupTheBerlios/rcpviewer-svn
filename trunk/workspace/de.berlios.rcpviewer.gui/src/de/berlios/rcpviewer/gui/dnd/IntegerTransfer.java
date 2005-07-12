package de.berlios.rcpviewer.gui.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

/**
 * Used for transferring int's within the application.
 * @author Mike
 *
 */
public class IntegerTransfer extends ByteArrayTransfer {
	
	private static final String INTEGER_TYPE_NAME
		= IntegerTransfer.class.getName();
	private static final int INTEGER_TYPE_ID
		= registerType ( INTEGER_TYPE_NAME );
	
	private static IntegerTransfer __instance = new IntegerTransfer();

	/**
	 * Returns the singleton instance of the IntegerTransfer class.
	 *
	 * @return the singleton instance of the IntegerTransfer class
	 */
	public static IntegerTransfer getInstance () {
		return __instance;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		if ( object == null || !( object instanceof Integer ) ) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		else {
			super.javaToNative( 
					String.valueOf( (Integer)object ).getBytes(), 
					transferData );
		}
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[])super.nativeToJava(transferData);
		return new Integer( new String( bytes ) ) ;
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	protected String [] getTypeNames () {
		return new String [] { INTEGER_TYPE_NAME };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	protected int [] getTypeIds () {
		return new int [] { INTEGER_TYPE_ID };
	}
	
	// private constructor 
	private IntegerTransfer() {
		super();
	}
	

}
