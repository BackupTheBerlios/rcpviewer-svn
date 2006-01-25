package org.essentialplatform.runtime.shared.domain;

import org.essentialplatform.runtime.shared.transaction.ITransactable;



public interface IPojo extends ITransactable {

	/**
	 * Tracks the resolve state, transaction state and other aspects
	 * on behalf of the pojo.
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