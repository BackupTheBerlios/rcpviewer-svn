package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IReference;

/**
 * Represents runtime-specific functionality (common to client and server) for 
 * an <i>instance of</i> an {@link IReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectReferenceRuntimeBinding 
		extends IObjectMemberRuntimeBinding {
	

	/**
	 * Invoked after {@link IDomainObject#getReference(IDomainClass.IReference)}
	 * so that the binding can perform any further processing.
	 * 
	 * <p>
	 * For example, on the client, can add the reference to the list of
	 * observed features.
	 */
	public void gotReference();

}