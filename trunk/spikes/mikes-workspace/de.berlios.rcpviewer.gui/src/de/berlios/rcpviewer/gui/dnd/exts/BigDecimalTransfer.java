package de.berlios.rcpviewer.gui.dnd.exts;

import java.math.BigDecimal;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

/**
 * Used for transferring <code>BigDecimals</code> within the application.
 * @author Mike
 *
 */
public class BigDecimalTransfer extends ByteArrayTransfer {
	
	private static final String BIGDECIMAL_TYPE_NAME
		= BigDecimalTransfer.class.getName();
	private static final int BIGDECIMAL_TYPE_ID
		= registerType ( BIGDECIMAL_TYPE_NAME );
	
	private static BigDecimalTransfer __instance = new BigDecimalTransfer();

	/**
	 * Returns the singleton instance of the DateTransfer class.
	 *
	 * @return the singleton instance of the DateTransfer class
	 */
	public static BigDecimalTransfer getInstance () {
		return __instance;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		if ( object == null || !( object instanceof BigDecimal ) ) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		else {
			super.javaToNative( object.toString().getBytes(), transferData );
		}
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[])super.nativeToJava(transferData);
		return new BigDecimal( new String( bytes ) );
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	protected String [] getTypeNames () {
		return new String [] { BIGDECIMAL_TYPE_NAME };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	protected int [] getTypeIds () {
		return new int [] { BIGDECIMAL_TYPE_ID };
	}
	
	// private constructor 
	private BigDecimalTransfer() {
		super();
	}
	

}
