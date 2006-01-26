package org.essentialplatform.runtime.client.transaction;

import org.essentialplatform.runtime.shared.domain.IPojo;

/**
 * Thrown if an attempt is made to perform an operation that would enlist
 * a pojo in a transaction when that pojo is already enlisted in some
 * other current transaction.
 * 
 * @author Dan Haywood
 */
public class PojoAlreadyEnlistedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static String messageFor(final IPojo pojo, final ITransaction transaction) {
		return "Pojo '" + pojo + "' already enlisted in transaction '" + transaction + "'";
	}

	private IPojo _pojo;
	private ITransaction _transaction;
	
	public PojoAlreadyEnlistedException() {
		super();
	}

	public PojoAlreadyEnlistedException(String message) {
		super(message);
	}

	public PojoAlreadyEnlistedException(final IPojo pojo, final ITransaction transaction) {
		super(messageFor(pojo, transaction));
		_pojo = pojo;
		_transaction = transaction;
	}
	
	public final IPojo getPojo() {
		return _pojo;
	}
	
	public final ITransaction getTransaction() {
		return _transaction;
	}
	
}
