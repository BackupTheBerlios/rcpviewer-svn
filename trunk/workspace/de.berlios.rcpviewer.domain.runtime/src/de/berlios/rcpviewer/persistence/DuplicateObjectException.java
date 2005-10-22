package de.berlios.rcpviewer.persistence;

/**
 * Attempted to save (persist) an object when another object has already
 * been persisted with a duplicate identifier.
 * 
 * <p>
 * In terms of an RDBMS, this represents an exception resulting from a
 * <code>CREATE UNIQUE INDEX</code> being violated.
 * 
 * @author Dan Haywood
 */
public class DuplicateObjectException extends RuntimeException {

	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = 1L;

	private Object _pojo;
	
	public DuplicateObjectException(String message, Object pojo) {
		super(message);
		_pojo = pojo;
	}

	public DuplicateObjectException(String message, Object pojo, Throwable cause) {
		super(message, cause);
		_pojo = pojo;
	}

	public final Object getPojo() {
		return _pojo;
	}
}
