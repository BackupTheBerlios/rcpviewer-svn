package org.essentialplatform.runtime.transaction;

import org.essentialplatform.runtime.transaction.ITransaction;

/**
 * Represents objects that participate in {@link ITransaction}s.
 */
public interface ITransactable {
	
	/**
	 * The {@link ITransaction} in which this object is enlisted.
	 * 
	 * <p>
	 * If there is no transaction then one will be created (in a state of 
	 * {@link ITransaction.State#IN_PROGRESS}) and this object will be enlisted
	 * within it.
	 * 
	 * @return
	 */
	public ITransaction getTransaction();

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
	public ITransaction getTransaction(final boolean autoEnlist);

}

