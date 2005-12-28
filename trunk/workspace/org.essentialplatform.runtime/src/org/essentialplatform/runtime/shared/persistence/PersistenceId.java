package org.essentialplatform.runtime.shared.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Identifier for a persisted instance of some type.
 * 
 * <p>
 * Conceptually, this is the concatenation of all attributes annotated as <tt>@Id</tt>.
 * 
 * <p>
 * This object has value semantics.
 * 
 * @author Dan Haywood
 */
public final class PersistenceId {

	private Class _javaClass;
	private List<Object> _componentValues = new ArrayList<Object>();
	
	public PersistenceId(final Class javaClass) {
		_javaClass = javaClass;
	}

	/**
	 * Convenience constructor for when the components of the Id are known.
	 * 
	 * @param javaClass
	 * @param componentValues - the values of each of the components of this Id.
	 */
	public PersistenceId(final Class javaClass, final Object... componentValues) {
		this(javaClass);
		for(Object componentValue: componentValues) {
			addComponentValue(componentValue);
		}
	}
	
	public Class getJavaClass() {
		return _javaClass;
	}

	/**
	 * Adds the value of one of the components of this persistence Id.
	 * 
	 * <p>
	 * For example, will be called respectively for attributes annotated as 
	 * <tt>@Id(1)</tt>, <tt>@Id(2)</tt>, <tt>@Id(3)</tt> and so forth.
	 * 
	 * <p>
	 * Only intended to be called by the platform.
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
	
	
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PersistenceId)) { return false; }
		return equals((PersistenceId)other);
	}
	
	public boolean equals(final PersistenceId other) {
		if (this._javaClass != other._javaClass) { return false; }
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
		StringBuffer buf = new StringBuffer("OID: ");
		buf.append(_componentValues);
		buf.append(" ");
		buf.append("{")
           .append(getJavaClass().getName())
	       .append("}");
		return buf.toString();
	}
	
}
