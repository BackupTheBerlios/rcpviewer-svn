package de.berlios.rcpviewer.transaction;

import java.util.EventObject;
import de.berlios.rcpviewer.transaction.ITransaction;


/**
 * Event object for events that relate to an {@link ITransaction}.
 * 
 * @author Dan Haywood
 */
public final class TransactionEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public TransactionEvent(final ITransaction source) {
		super(source);
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the {@link ITransaction} that raised the event.
	 */
	public ITransaction getTransaction() {
		return (ITransaction)this.getSource();
	}

}
