package de.berlios.rcpviewer.progmodel;

/**
 * Indicates that the domain programmer has in some way violated the contract
 * implied by the programming model in use.
 *  
 * @author Dan Haywood
 */
public class ProgrammingModelException extends RuntimeException {

	public ProgrammingModelException() {
		super();
	}

	public ProgrammingModelException(String message) {
		super(message);
	}

	public ProgrammingModelException(String message, Throwable t) {
		super(message, t);
	}

	public ProgrammingModelException(Throwable t) {
		super(t);
	}

}
