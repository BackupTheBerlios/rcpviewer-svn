package de.berlios.rcpviewer.gui.dnd;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

/**
 * Class offering generic functionality for all primitive type transfers.
 * @author Mike
 *
 */
class PrimitiveTransfer extends ByteArrayTransfer {
	
	private static final Map<Class,Class> WRAPPERS = new HashMap<Class,Class>();
	
	static {
		WRAPPERS.put( boolean.class, Boolean.class );
		WRAPPERS.put( byte.class, Byte.class );
		WRAPPERS.put( char.class, Character.class );
		WRAPPERS.put( short.class, Short.class );
		WRAPPERS.put( int.class, Integer.class );
		WRAPPERS.put( long.class, Long.class );
		WRAPPERS.put( float.class, Float.class );
		WRAPPERS.put( double.class, Double.class );
	}

	private final Class _type;
	private final String[] _typeNames;
	private final int[] _typeIds;
	private final Method _valueOfMethod;
	
	PrimitiveTransfer( Class type ) {
		if( type == null ) throw new IllegalArgumentException();
		if( !type.isPrimitive() ) throw new IllegalArgumentException();
		
		_type = type;
		_typeNames = new String[]{ type.getName() };
		_typeIds = new int[]{ registerType( _typeNames[0] ) };

		// store reference to valueOf method - char does not have one
		if ( type == char.class ) {
			_valueOfMethod = null;
		}
		else {
			try {
				_valueOfMethod = WRAPPERS.get( _type ).getMethod( 
						"valueOf", new Class[]{ String.class } ); //$NON-NLS-1$
			}
			catch ( NoSuchMethodException nsfe ) {
				throw new IllegalArgumentException();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		if ( object == null ) {
			DND.error( DND.ERROR_INVALID_DATA );
		}
		if ( object.getClass() == _type || 
				object.getClass() == WRAPPERS.get( _type ) ) {
			super.javaToNative( 
					String.valueOf( object ).getBytes(), 
					transferData );
		}
		else {
			DND.error( DND.ERROR_INVALID_DATA );
		}
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[])super.nativeToJava(transferData);
		
		// char a special case
		if ( _type == char.class ) {
			return new String( bytes ).toCharArray()[0];
		}
		
		try {
			return _valueOfMethod.invoke( null, new Object[]{ new String( bytes ) } );
		}
		catch ( Exception ex ) {
			DND.error( DND.ERROR_INVALID_DATA );
			return null;
		}
	}



	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeNames()
	 */
	protected String [] getTypeNames () {
		return _typeNames;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.Transfer#getTypeIds()
	 */
	protected int [] getTypeIds () {
		return _typeIds;
	}

}
