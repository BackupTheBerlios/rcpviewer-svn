package de.berlios.rcpviewer.petstore.gui;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

import de.berlios.rcpviewer.gui.dnd.DndTransferFactory;
import de.berlios.rcpviewer.petstore.domain.FilePath;

/**
 * Used for transferring <code>FilePath</code> within the application.
 * <br>Currently ignores filter concepts of <code>FilePath</code>.
 * @author Mike
 *
 */
public class FilePathTransfer extends ByteArrayTransfer {
	
	private static final String FILEPATH_TYPE_NAME
		= FilePathTransfer.class.getName();
	private static final int FILEPATH_TYPE_ID
		= registerType ( FILEPATH_TYPE_NAME );
	
	private static FilePathTransfer __instance = new FilePathTransfer();
	
	// yuk!! does not work as this only happend once the transfer class is
	// instantiated
	static {
		DndTransferFactory.registerTransfer( FilePath.class, __instance );
	}

	/**
	 * Returns the singleton instance of the DateTransfer class.
	 *
	 * @return the singleton instance of the DateTransfer class
	 */
	public static FilePathTransfer getInstance () {
		return __instance;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		if ( object == null || !( object instanceof FilePath ) ) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		else {
			super.javaToNative( 
					((FilePath)object).getPath().getBytes(), transferData );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[])super.nativeToJava(transferData);
		String s = new String( bytes );
		FilePath fPath = new FilePath();
		fPath.init( (String[])null );
		assert fPath.isValidString( s );
		fPath.parseString( s );
		return fPath;
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	protected String [] getTypeNames () {
		return new String [] { FILEPATH_TYPE_NAME };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	protected int [] getTypeIds () {
		return new int [] { FILEPATH_TYPE_ID };
	}
	
	// private constructor 
	private FilePathTransfer() {
		super();
	}
	

}
