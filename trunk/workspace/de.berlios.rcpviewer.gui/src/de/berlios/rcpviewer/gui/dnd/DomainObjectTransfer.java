package de.berlios.rcpviewer.gui.dnd;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.gui.util.DomainRegistryUtil;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Used for transferring domain objects within the application.
 * <br>A seperate instance exists for each domain class.
 * <br>TODO : no allowance made for type hierarchies within the domain
 * @author Mike
 */
public class DomainObjectTransfer extends ByteArrayTransfer {

	private static Map<IDomainClass, DomainObjectTransfer> __instances 
		= new HashMap<IDomainClass, DomainObjectTransfer>();
	
	private final String[] _typeNames;
	private final int[] _typeIds;
	
	// yuk! - used until a solution based on serialised session id's is available
	private IDomainObject _obj = null;

	/**
	 * Returns the appropriate instance for the passed domain class
	 * @return 
	 */
	public static DomainObjectTransfer getInstance ( IDomainClass clazz ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		DomainObjectTransfer transfer = __instances.get( clazz );
		if ( transfer == null ) {
			transfer = new DomainObjectTransfer( clazz );
			__instances.put( clazz, transfer );
		}
		return transfer;
	}
	
	/**
	 * Return all possible instances.
	 * @return
	 */
	static Collection<DomainObjectTransfer> getInstances() {
		Iterator<IDomainClass> it = DomainRegistryUtil.iterateAllClasses();
		while( it.hasNext() ) {
			getInstance( it.next() );
		}
		return __instances.values();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		if ( object == null || !( object instanceof IDomainObject ) ) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		else {
//			ByteArrayOutputStream out = new ByteArrayOutputStream ();
//			try {
//				ObjectOutputStream writeOut = new ObjectOutputStream (out);
//				writeOut.writeObject( object );
//				byte[] buffer = out.toByteArray ();
//				out.close();
//				out = null;
//				super.javaToNative (buffer, transferData);
//			}
//			catch( IOException ioe ) {
//				DND.error( DND.ERROR_CANNOT_SET_CLIPBOARD );
//			}
//			finally {
//				if ( out != null ) {
//					try {
//						out.close();
//					}
//					catch (IOException ioe ) {
//						DND.error( DND.ERROR_CANNOT_SET_CLIPBOARD );
//					}
//					finally {
//						out = null;
//					}
//				}
//			}
			_obj = (IDomainObject)object;
			// use super functionality to set all low-level constructs used 
			// elsewhere in DnD framework
			super.javaToNative( new byte[1], transferData );
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	protected Object nativeToJava(TransferData transferData) {
//		ByteArrayInputStream in = new ByteArrayInputStream( 
//			(byte[])super.nativeToJava(transferData) );
//		try {
//			ObjectInputStream readIn = new ObjectInputStream (in );
//			Object obj = readIn.readObject();
//			in.close();
//			in = null;
//			return obj;
//		}
//		catch( Exception ex ) {
//			DND.error( DND.ERROR_INVALID_DATA );
//			return null;
//		}
//		finally {
//			if ( in != null ) {
//				try {
//					in.close();
//				}
//				catch (IOException ioe ) {
//					DND.error( DND.ERROR_INVALID_DATA );
//				}
//				finally {
//					in = null;
//				}
//			}
//		}
		// use super functionality to set all low-level constructs used 
		// elsewhere in DnD framework
		super.nativeToJava( transferData );
		if ( _obj != null ) {
			// clear object reference
			IDomainObject obj = _obj;
			_obj = null;
			return obj;
		}
		return _obj;
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
	
	// private constructor 
	private DomainObjectTransfer( IDomainClass<?> clazz ) {
		super();
		assert clazz != null;
		_typeNames = new String[]{ clazz.getName() }; 
		_typeIds = new int[]{ registerType( clazz.getName() ) }; 
	}
	

}
