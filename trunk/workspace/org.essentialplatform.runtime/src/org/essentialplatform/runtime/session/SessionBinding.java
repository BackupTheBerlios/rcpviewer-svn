package org.essentialplatform.runtime.session;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.DomainConstants;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.runtime.persistence.PersistenceConstants;

/**
 * Representing a (domain, objectstore) binding, with value object semantics.
 * 
 * <p>
 * Holds the <i>name</i> of the {@link IDomain} and the <i>Id</i> of the 
 * {@link IObjectStore}, rather than references to the actual objects 
 * themselves.   This allows the object to be serialized across the wire, 
 * (eg, so that the server-side code can locate the appropriate persistence
 * context for an IObjectStore implementation). 
 * 
 * @author Dan Haywood
 *
 */
public final class SessionBinding {
	
	public SessionBinding(String domainName, String objectStoreId) {
		if (domainName == null)
			throw new IllegalArgumentException();
		if (objectStoreId == null)
			throw new IllegalArgumentException();
		_domainName = domainName;
		_objectStoreId = objectStoreId;
	}

	///////////////////////////////////////////////////////////////////
	// domainName, objectStoreId
	///////////////////////////////////////////////////////////////////

	private final String _domainName;
	public String getDomainName() {
		return _domainName;
	}
	
	
	private final String _objectStoreId;
	public String getObjectStoreId() {
		return _objectStoreId;
	}
	

	///////////////////////////////////////////////////////////////////
	// equals, hashCode
	///////////////////////////////////////////////////////////////////


	public boolean equals(final Object other) {
		if (other == null) return false;
		if (this.getClass() != other.getClass()) return false;
		return equals((SessionBinding)other);
	}
	
	public boolean equals(final SessionBinding other) {
		if (other == null) return false;
		return _domainName.equals(other._domainName) &&
		       _objectStoreId.equals(other._objectStoreId);
	}
	
	@Override
	public int hashCode() {
		return (_domainName + "|" + _objectStoreId).hashCode();
	}

	@Override
	public String toString() {
		return "(" + _domainName + "," + _objectStoreId + ")";
	}

}
