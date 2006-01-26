package org.essentialplatform.runtime.shared.domain;


public interface IPojo {

	/**
	 * Tracks the resolve state and other aspects on behalf of the pojo.
	 * 
	 * <p>
	 * Note that transaction state is relevant only on the client and so is
	 * no longer part of IPojo. 
	 * 
	 * <p>
	 * Implementation note: previously this method was called <tt>getDomainObject()</tt>.
	 * It was renamed so that Hibernate would not pick it up as a property.  (An
	 * alternative would have been to use the <tt>@javax.persistence.Transient</tt>
	 * annotation, however since the field is introduced by an aspect this area is
	 * still (Dec 2005) somewhat unstable).
	 * 
	 * TODO: <T>.  However, doesn't seem possible to introduce genericised
	 * members using aspects (PojoAspect).
	 */
	IDomainObject domainObject();
}
