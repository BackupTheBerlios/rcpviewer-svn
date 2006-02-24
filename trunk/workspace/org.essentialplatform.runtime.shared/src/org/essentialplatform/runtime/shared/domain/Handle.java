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

	

	/**
	 * Convenience constructor for when the components of the Id are known.
	 * 
	 * @param javaClass       - as per {@link #getJavaClass()}
	 * @param abbreviation    - as per {@link #getAbbreviation()}.
	 * @param componentValues - as per {@link #getComponentValues()}.
	 */
	public Handle(final Class javaClass, final Object... componentValues) {
		_javaClass = javaClass;
		addComponentValues(componentValues);
	}

	
	///////////////////////////////////////////////////////////////////////
	// JavaClass
	///////////////////////////////////////////////////////////////////////

	private Class _javaClass;
	public Class getJavaClass() {
		return _javaClass;
	}

	
	///////////////////////////////////////////////////////////////////////
	// Abbreviation
	///////////////////////////////////////////////////////////////////////

	private String _abbreviation;
	/**
	 * Shortened (and expected to be unique) version of the java Class name.
	 * 
	 * <p>
	 * For example, <tt>DPT</tt> for <tt>Department</tt>, <tt>EMP</tt> for 
	 * <tt>Employee</tt>.
	 *  
	 * @return
	 */
	public String getAbbreviation() {
		return _abbreviation;
	}
	public Handle updateAbbreviation(String abbreviation) {
		_abbreviation = abbreviation;
		updateCachedHashCodeAndToStringIfRequired();
		return this;
	}
	

	///////////////////////////////////////////////////////////////////////
	// ComponentValues
	///////////////////////////////////////////////////////////////////////
	
	private List<Object> _componentValues = new ArrayList<Object>();
	/**
	 * Adds the value of one or several of the components of this handle.
	 * 
	 * @param componentValue
	 */
	public void addComponentValues(final Object... componentValues) {
		for(Object componentValue: componentValues) {
			addComponentValue(componentValue);
		}
		_cachedHashCodeAndToStringOk = false;
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

	
	///////////////////////////////////////////////////////////////////////
	// update, hasPrevious
	///////////////////////////////////////////////////////////////////////


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
	 * <p>
	 * <b>If the handle is being used as a key within a hash, then it should
	 * be removed prior to calling this method.  This is because the {@link #hashCode} result
	 * will (almost certainly) change as a result of making this call.</b>
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
		_previous = new Handle(_javaClass, _componentValues.toArray()).updateAbbreviation(getAbbreviation());
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

	
	
	///////////////////////////////////////////////////////////////////////
	// equals, hashCode, toString
	///////////////////////////////////////////////////////////////////////

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
	 * Will be set to false if de-serialized.
	 */
	private transient boolean _cachedHashCodeAndToStringOk;
	private transient int _cachedHashCode;
	@Override
	public int hashCode() {
		updateCachedHashCodeAndToStringIfRequired();
		return _cachedHashCode;
	}
	private void updateCachedHashCodeAndToStringIfRequired() {
		if (_cachedHashCodeAndToStringOk) return;
	    final int hashMultiplier = 41;
	    int result = 7;
		for (Object componentValue: _componentValues) {
			result = result * hashMultiplier + componentValue.hashCode();
		}
	    _cachedHashCode = result;
	    
		StringBuffer buf = new StringBuffer();
		buf.append(getAbbreviation())
		   .append("|");
		append(buf, _componentValues);

		_cachedAsString = buf.toString();
		
		if (hasPrevious()) {
			buf.append("<<");
			append(buf, _previous._componentValues);
		}
		_cachedToString = "H:" + _cachedAsString;

	    _cachedHashCodeAndToStringOk = true;
	}
	private void append(StringBuffer buf, List<Object> values) {
		int i=0;
		for (Object value: values) {
			buf.append(value);
			if (++i < values.size()) {
				buf.append("|");
			}
		}
	}

	
	private transient String _cachedAsString;
	/**
	 * In format <tt>ORD|123|456</tt> (eg Order identified by (123,456)).
	 * 
	 * @return
	 */
	public String asString() {
		updateCachedHashCodeAndToStringIfRequired();
		return _cachedAsString;
	}

	private transient String _cachedToString;
	/**
	 * In format <tt>ORD|123|456&lt;&lt;6523-A234-801F-123B</tt>  (eg Order identified by 
	 * (123,456)), where the content after the <tt>&lt;&lt;</tt> is the 
	 * previous value of the hashcode, if any.
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		updateCachedHashCodeAndToStringIfRequired();
		return _cachedToString;
	}
	
}
