package org.essentialplatform.runtime.shared.transaction;

public class IrreversibleTransactionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IrreversibleTransactionException() {
		super();
	}

	public IrreversibleTransactionException(String message) {
		super(message);
	}

	public IrreversibleTransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public IrreversibleTransactionException(Throwable cause) {
		super(cause);
	}
	
	

}
