package de.berlios.rcpviewer.gui.dnd;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import de.berlios.rcpviewer.domain.RuntimeDomain;

/**
 * Static utitlity methods
 * @author Mike
 */
public class DndUtil {
	
	private static final Map<Class,Transfer> TRANSFERS
		= new HashMap<Class,Transfer>();
	
	/**
	 * Returns the DnD transfer type appropriate for the passed class.
	 * <br>Currently could be null.
	 * @param clazz
	 * @return
	 */
	public static Transfer getTransfer( Class clazz ) {
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
					 RuntimeDomain.instance().lookupNoRegister( clazz ) ); // JAVA_5_FIXME 
			}
			TRANSFERS.put( clazz, transfer );
		}
		return transfer;
	}
	
	

	// prevent instantiation
	private DndUtil() {
		super();
	}

}
