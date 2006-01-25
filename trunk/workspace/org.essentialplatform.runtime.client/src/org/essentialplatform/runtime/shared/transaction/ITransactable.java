package org.essentialplatform.runtime.shared.transaction;

import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

/**
 * Represents objects that participate in {@link ITransaction}s.
 */
public interface ITransactable {
	
	public enum TransactionalState {
		/**
		 * Not yet been initialized.
		 * 
		 * <p>
		 * This is the default state before {@link IDomainObject#init} has
		 * been called.  Any changes picked up by aspects should be ignored.
		 */
		NOT_INITIALIZED,
		/**
		 * Has been created or recreated; any changes should be treated as
		 * being transactional.
		 */
		INITIALIZED
	}

	/**
	 * The {@link ITransaction} in which this object is enlisted.
	 * 
	 * <p>
	 * If there is no transaction then one will be created (in a state of 
	 * {@link ITransaction.State#IN_PROGRESS}) and this object will be enlisted
	 * within it.
	 * 
	 * <p>
	 * Implementation note: previously this method was called <tt>getTransaction()</tt>.
	 * It was renamed so that Hibernate would not pick it up as a property.  (An
	 * alternative would have been to use the <tt>@javax.persistence.Transient</tt>
	 * annotation, however since the field is introduced by an aspect this area is
	 * still (Dec 2005) somewhat unstable).
	 * 
	 * @return
	 */
	public ITransaction currentTransaction();

	/**
	 * The {@link ITransaction} in which this object is enlisted.
	 * 
	 * <p>
	 * If there is no current transaction then one will only be created if
	 * <code>autoEnlist</code> is <code>true</code>; otherwise will return 
	 * <code>null</code>.
	 * 
	 * @return
	 */
	public ITransaction currentTransaction(final boolean autoEnlist);

}

