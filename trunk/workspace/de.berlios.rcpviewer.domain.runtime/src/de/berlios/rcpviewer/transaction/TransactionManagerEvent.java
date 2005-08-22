package de.berlios.rcpviewer.transaction;

import java.util.EventObject;
import de.berlios.rcpviewer.transaction.ITransaction;


/**
 * Event object for events that relate to an {@link ITransactionManager}.
 * 
 * @author Dan Haywood
 */
public final class TransactionManagerEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final ITransaction _transaction;
	
	/**
	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public TransactionManagerEvent(final ITransactionManager source, final ITransaction transaction) {
		super(source);
		_transaction = transaction;
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the {@link ITransactionManager} that raised the event.
	 */
	public ITransactionManager getTransactionManager() {
		return (ITransactionManager)this.getSource();
	}

	
	/**
	 * The transaction to which this event relates.
	 * 
	 * @return 
	 */
	public ITransaction getTransaction() {
		return _transaction;
	}

}
