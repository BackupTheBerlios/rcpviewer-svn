package org.essentialplatform.runtime.persistence;

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

	private Class _type;
	private List<Object> _componentValues = new ArrayList<Object>();
	
	public PersistenceId(final Class type) {
		_type = type;
	}
	
	public Class getType() {
		return _type;
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
	
	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof PersistenceId)) { return false; }
		return equals((PersistenceId)other);
	}
	
	public boolean equals(final PersistenceId other) {
		if (this._type != other._type) { return false; }
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
}
