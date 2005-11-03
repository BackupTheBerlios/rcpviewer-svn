package org.essentialplatform.runtime.transaction;

/**
 * Thrown if an attempt is made to perform an operation that would enlist
 * a pojo in a transaction when that pojo is already enlisted in some
 * other current transaction.
 * 
 * @author Dan Haywood
 */
public class PojoAlreadyEnlistedException extends RuntimeException {

	private static String messageFor(final ITransactable transactable, final ITransaction transaction) {
		return "Pojo '" + transactable + "' already enlisted in transaction '" + transaction + "'";
	}

	private ITransactable _transactable;
	private ITransaction _transaction;
	
	public PojoAlreadyEnlistedException() {
		super();
	}

	public PojoAlreadyEnlistedException(String message) {
		super(message);
	}

	public PojoAlreadyEnlistedException(final ITransactable transactable, final ITransaction transaction) {
		super(messageFor(transactable, transaction));
		_transactable = transactable;
		_transaction = transaction;
	}
	
	public final ITransactable getTransactable() {
		return _transactable;
	}
	
	public final ITransaction getTransaction() {
		return _transaction;
	}
	
}
