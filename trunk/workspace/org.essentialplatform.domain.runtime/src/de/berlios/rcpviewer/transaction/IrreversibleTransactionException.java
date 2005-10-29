package de.berlios.rcpviewer.transaction;

public class IrreversibleTransactionException extends RuntimeException {

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