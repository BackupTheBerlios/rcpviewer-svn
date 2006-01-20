package org.essentialplatform.runtime.shared.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle for and instance (persisted or otherwise) of some type, managed by
 * the pojo's {@link IDomainObject}.
 * 
 * <p>
 * The vast majority of domain objects will be persisted in an object store
 * (RDBMS etc) and so for these the handle would be derived from the primary
 * key (or perhaps an alternate key) used by the RDBMS etc.  However,  
 * objects that are being created are initially instantiated on the client.
 * The client-side session must assign a handle temporarily and send the
 * object over the wire such that it may be persisted.  The server-side
 * object store will assign a new value for the handle.  
 * 
 * <p>
 * When an object is persisted, it isn't sufficient for the server to simply 
 * replace the value of the handle because there would then be no way for the
 * client to update the handle of the object still held client-side.  Therefore
 * the handle is also able to hold the original value for this purpose.  That 
 * is, when a client obtains an object from the server, it first attempts to
 * locate the object by the handle's current value.  If that fails, the 
 * client attempts to locate the object by the original value of the handle, 
 * if any.  If found, it updates its hashes and then clears out the original 
 * value.  (If the object cannot be found by either the current or original
 * values of the handle, then the client will simply discard the object - it
 * isn't one which the user is interested in). 
 * 
 * <p>
 * This object has value semantics (with the comparison done by the current
 * value of the handle).
 * 
 * @author Dan Haywood
 */
public final class Handle {

	private Class _javaClass;
	private List<Object> _componentValues = new ArrayList<Object>();
	
	/**
	 * Convenience constructor for when the components of the Id are known.
	 * 
	 * @param javaClass
	 * @param componentValues - the values of each of the components of this Id.
	 */
	public Handle(final Class javaClass, final Object... componentValues) {
		_javaClass = javaClass;
		addComponentValues(componentValues);
	}
	
	public Class getJavaClass() {
		return _javaClass;
	}

	/**
	 * Adds the value of one or several of the components of this handle.
	 * 
	 * @param componentValue
	 */
	public void addComponentValues(final Object... componentValues) {
		for(Object componentValue: componentValues) {
			addComponentValue(componentValue);
		}
	}
	
	/**
	 * Adds the value of one of the components of this handle.
	 * 
	 * <p>
	 * Expected usage would for this method to be called successively for 
	 * attributes annotated as <tt>@Id(1)</tt>, <tt>@Id(2)</tt>, <tt>@Id(3)</tt>
	 * and so forth.
	 * 
	 * @param componentValue
	 */
	public void addComponentValue(final Object componentValue) {
		_componentValues.add(componentValue);
	}

	
	/**
	 * Returns the list of component values as an array.
	 * 
	 * <p>
	 * Primarily for testing purposes; use {@link #equals(Object)} to check
	 * for equality of two Ids.
	 * 
	 * @return
	 */
	public Object[] getComponentValues() {
		return _componentValues.toArray();
	}

	/**
	 * Updates the handle with a new set of values (assuming that they
	 * are different).
	 * 
	 * <p>
	 * If there are no current values (that is, that {@link #addComponentValue(Object)}
	 * has never been called, then the method effectively does nothing.
	 * 
	 * <p>
	 * If the set of component values provided as parameters happen to be
	 * identical to the current values, then again nothing will be done.
	 * 
	 * <p>
	 * Otherwise, though, the current set of component values are "moved" such
	 * that they are obtainable using {@link #getPrevious()}, represented as a 
	 * new {@link Handle}.  
	 * 
	 * <p>
	 * If this method is called twice, then the handle previously returned by
	 * {@link #getPrevious()} will be lost. 
	 * 
	 * <p>
	 * If this method is called with no or an incomplete set of component
	 * values, then the {@link #addComponentValue(Object)} method can be called
	 * (as many times as required) to build up the new values of the handle.
	 * 
	 * @param componentValues
	 */
	public void update(final Object... componentValues) {
		if (_componentValues.size() == 0) {
			return;
		}
		Handle pending = new Handle(_javaClass, componentValues);
		if (pending.equals(this)) {
			return;
		}
		_previous = new Handle(_javaClass, _componentValues.toArray());
		_componentValues.clear();
		addComponentValues(componentValues);
	}
	
	private Handle _previous;
	/**
	 * Returns the previous (original) value of the handle, if any.
	 * 
	 * <p>
	 * If {@link #update(Object[])} has not been called, then will return 
	 * <tt>null</tt>.
	 * 
	 * <p>
	 * The value of {@link #getPrevious()} is <i>not</i> considered when 
	 * comparing handles (by {@link #equals(Object)} or {@link #hashCode()}). 
	 * @return
	 */
	public Handle getPrevious() {
		return _previous;
	}
	public boolean hasPrevious() {
		return getPrevious() != null;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Handle)) { return false; }
		return equals((Handle)other);
	}
	
	public boolean equals(final Handle other) {
		if (other == null) { return false; }
		if (this._javaClass != other._javaClass) { return false; }
		if (_componentValues.size() != other._componentValues.size()) {
			return false;
		}
		for(int i=0; i<_componentValues.size(); i++) {
			if(!_componentValues.get(i).equals(other._componentValues.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * TODO: currently uses just the hashCode of the first component value.
	 */
	@Override
	public int hashCode() {
		if (_componentValues.size() == 0) { return 0; }
		return _componentValues.get(0).hashCode();
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("ID: ");
		buf.append(getJavaClass().getSimpleName())
		   .append(" ")
		   .append(_componentValues);
		return buf.toString();
	}
	
}
