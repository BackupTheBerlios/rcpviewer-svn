package de.berlios.rcpviewer.gui.dnd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import de.berlios.rcpviewer.domain.RuntimeDomain;

/**
 * Static factory methods
 * @author Mike
 */
public class DndTransferFactory {
	
	private static final Map<Class,Transfer> TRANSFERS
		= new HashMap<Class,Transfer>();
	
	/**
	 * Returns all transfer types known to this factory.
	 * @return
	 */
	public static Transfer[] getTransfers() {
		List<Transfer> all = new ArrayList<Transfer>();
		all.addAll( Arrays.asList( DomainObjectTransfer.getInstances() ) );
		all.add( IntegerTransfer.getInstance() );
		all.add( BooleanTransfer.getInstance() );
		all.add( TextTransfer.getInstance() );
		return all.toArray( new Transfer[0] );
	}
	
	
	/**
	 * Returns the DnD transfer type appropriate for the passed class.
	 * <br>Currently could be null.
	 * @param clazz
	 * @return
	 */
	public static Transfer getTransfer( Class<?> clazz ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		Transfer transfer = TRANSFERS.get( clazz );
		if ( transfer == null ) {
			if ( clazz.isPrimitive() ) {
			    if ( int.class == clazz ) {
					transfer = IntegerTransfer.getInstance();
				}
				else if ( boolean.class == clazz ) {
					transfer = BooleanTransfer.getInstance();
				}
			}
			else if ( String.class == clazz ) {
				transfer = TextTransfer.getInstance();
			}
			else if ( Integer.class == clazz ) {
				transfer = IntegerTransfer.getInstance();
			}
			else if ( Boolean.class == clazz ) {
				transfer = BooleanTransfer.getInstance();
			}
			else if ( Date.class == clazz ) {
				transfer = IntegerTransfer.getInstance();
			}
			else {	
				transfer = DomainObjectTransfer.getInstance(
					 RuntimeDomain.instance().lookupNoRegister( clazz ) );
			}
			TRANSFERS.put( clazz, transfer );
		}
		return transfer;
	}
	
	

	// prevent instantiation
	private DndTransferFactory() {
		super();
	}

}
