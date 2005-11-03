package org.essentialplatform.core.progmodel;

/**
 * Indicates that the domain programmer has in some way violated the contract
 * implied by the programming model in use.
 *  
 * @author Dan Haywood
 */
public class ProgrammingModelException extends RuntimeException {

	/**
	 * Since RuntimeException is serializable.
	 */
	private static final long serialVersionUID = 1L;

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
