package de.berlios.rcpviewer.gui.dnd;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import de.berlios.rcpviewer.domain.RuntimeDomain;
import de.berlios.rcpviewer.gui.dnd.exts.BigDecimalTransfer;
import de.berlios.rcpviewer.gui.dnd.exts.DateTransfer;

/**
 * Static factory methods
 * @author Mike
 */
public class DndTransferFactory {
	
	private static final Map<Class,Transfer> TRANSFERS
		= new HashMap<Class,Transfer>();
	
	static {
		TRANSFERS.put( boolean.class, new PrimitiveTransfer( boolean.class ) );
		TRANSFERS.put( byte.class, new PrimitiveTransfer( byte.class ) );
		TRANSFERS.put( char.class, new PrimitiveTransfer( char.class ) );
		TRANSFERS.put( short.class, new PrimitiveTransfer( short.class ) );
		TRANSFERS.put( int.class, new PrimitiveTransfer( int.class ) ); 
		TRANSFERS.put( long.class, new PrimitiveTransfer( long.class ) ); 
		TRANSFERS.put( float.class, new PrimitiveTransfer( float.class ) );
		TRANSFERS.put( double.class, new PrimitiveTransfer( double.class ) ); 
		TRANSFERS.put( Boolean.class, new PrimitiveTransfer( boolean.class ) );
		TRANSFERS.put( Byte.class, new PrimitiveTransfer( byte.class ) );
		TRANSFERS.put( Character.class, new PrimitiveTransfer( char.class ) );
		TRANSFERS.put( Short.class, new PrimitiveTransfer( short.class ) );
		TRANSFERS.put( Integer.class, new PrimitiveTransfer( int.class ) ); 
		TRANSFERS.put( Long.class, new PrimitiveTransfer( long.class ) ); 
		TRANSFERS.put( Float.class, new PrimitiveTransfer( float.class ) );
		TRANSFERS.put( Double.class, new PrimitiveTransfer( double.class ) ); 
		TRANSFERS.put( Date.class, DateTransfer.getInstance() );
		TRANSFERS.put( BigDecimal.class, BigDecimalTransfer.getInstance() );
		TRANSFERS.put( String.class, TextTransfer.getInstance() );
	}
	
	/**
	 * Register another transfer type for the passed class.
	 * <br>if the transfer type for this calss has already been registered
	 * returns <code>false</code> and does nothing.
	 * <br><b>NOTE</b>: this concept is fundamentally broken until I find a 
	 * way to ensute this method is called <b>before</b> any <code>get()</code>
	 * method.
	 * @param clazz
	 * @param transfer
	 * @return
	 */
	public static boolean registerTransfer( Class clazz, Transfer transfer ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		if ( transfer == null ) throw new IllegalArgumentException();
		if ( TRANSFERS.get( clazz ) != null ) return false;
		TRANSFERS.put( clazz, transfer );
		return true;
	}
	
	/**
	 * Returns all transfer types known to this factory.
	 * @return
	 */
	public static Transfer[] getTransfers() {
		List<Transfer> all = new ArrayList<Transfer>();
		all.addAll( TRANSFERS.values() );
		all.addAll( DomainObjectTransfer.getInstances() );
		return all.toArray( new Transfer[0] );
	}
	
	
	/**
	 * Returns the DnD transfer type appropriate for the passed pojo class.
	 * <br>Currently could be null.
	 * @param clazz
	 * @return
	 */
	public static Transfer getTransfer( Class<?> clazz ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		Transfer transfer = TRANSFERS.get( clazz );
		if ( transfer == null ) {
			assert !clazz.isPrimitive();
			transfer = DomainObjectTransfer.getInstance(
				 RuntimeDomain.instance().lookupNoRegister( clazz ) );
			TRANSFERS.put( clazz, transfer );
		}
		return transfer;
	}
	
	

	// prevent instantiation
	private DndTransferFactory() {
		super();
	}

}
