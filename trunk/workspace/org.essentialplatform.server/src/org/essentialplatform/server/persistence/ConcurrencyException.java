package org.essentialplatform.server.persistence;

/**
 * Attempted to update an object that has been updated by some other user in
 * the meantime.
 * 
 * <p>
 * In terms of an RDBMS, this represents an exception resulting from a
 * optimistic locking version check.
 * 
 * @author Dan Haywood
 */
public class ConcurrencyException extends RuntimeException {

	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = 1L;

	private Object _pojo;
	
	public ConcurrencyException(String message, Object pojo) {
		super(message);
		_pojo = pojo;
	}

	public ConcurrencyException(String message, Object pojo, Throwable cause) {
		super(message, cause);
		_pojo = pojo;
	}

	public final Object getPojo() {
		return _pojo;
	}
}
