package de.berlios.rcpviewer.gui.dnd;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

/**
 * Used for transferring int's within the application.
 * @author Mike
 *
 */
public class BooleanTransfer extends ByteArrayTransfer {
	
	private static final String BOOLEAN_TYPE_NAME
		= BooleanTransfer.class.getName();
	private static final int BOOLEAN_TYPE_ID
		= registerType ( BOOLEAN_TYPE_NAME );
	
	private static final byte[] TRUE = new byte[]{ 1 };
	private static final byte[] FALSE = new byte[]{ 0 };
	
	private static BooleanTransfer __instance = new BooleanTransfer();

	/**
	 * Returns the singleton instance of the BooleanTransfer class.
	 *
	 * @return the singleton instance of the BooleanTransfer class
	 */
	public static BooleanTransfer getInstance () {
		return __instance;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		if ( object == null || !( object instanceof Boolean ) ) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		else {
			super.javaToNative( 
					((Boolean)object).booleanValue() ? TRUE : FALSE, 
					transferData );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[])super.nativeToJava(transferData);
		if ( bytes[0] == TRUE[0] ) return Boolean.TRUE;
		return Boolean.FALSE;
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	protected String [] getTypeNames () {
		return new String [] { BOOLEAN_TYPE_NAME };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	protected int [] getTypeIds () {
		return new int [] { BOOLEAN_TYPE_ID };
	}
	
	// private constructor 
	private BooleanTransfer() {
		super();
	}
	

}
