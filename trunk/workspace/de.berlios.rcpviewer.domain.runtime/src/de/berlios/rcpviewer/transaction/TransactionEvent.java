package de.berlios.rcpviewer.transaction;

import java.util.EventObject;
import de.berlios.rcpviewer.transaction.ITransaction;


/**
 * Event object for events that relate to an {@link ITransaction}.
 * 
 * @author Dan Haywood
 */
public final class TransactionEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The most recent change affected, if any.
	 */
	private final IChange _change;

	/**
	 * Standard constructor for {@link EventObject}s.
	 * 
	 * <p>
	 * The change property will be <code>null</code>.
	 * 
	 * @param source
	 */
	public TransactionEvent(final ITransaction source) {
		this(source, null);
	}
	
	/**
	 * Standard constructor for {@link EventObject}s, specifying a
	 * {@link IChange} relevant to the event.
	 * 
	 * @param source
	 */
	public TransactionEvent(final ITransaction source, final IChange change) {
		super(source);
		_change = change;
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the {@link ITransaction} that raised the event.
	 */
	public ITransaction getTransaction() {
		return (ITransaction)this.getSource();
	}
	
	/**
	 * The most recent {@link IChange} to occur for the {@link ITransaction}.
	 * 
	 * <p>
	 * The meaning of this property depends upon the event; the change could
	 * be <code>null</code> in some circumstances.
	 * @return
	 */
	public IChange getChange() {
		return _change;
	}

}
