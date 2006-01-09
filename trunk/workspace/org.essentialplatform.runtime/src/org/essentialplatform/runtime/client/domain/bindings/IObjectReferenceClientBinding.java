package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IReference;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.client.domain.event.IDomainObjectReferenceListener;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectReferenceRuntimeBinding;

/**
 * Represents client-specific functionality for an <i>instance of</i> an 
 * {@link IReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectReferenceClientBinding 
		extends IObjectReferenceRuntimeBinding, IObjectMemberClientBinding {
	
	/**
	 * Register interest in changes in either the value of this reference 
	 * or in whether the (accessor) prerequisites or other relevant state 
	 * of this reference changes.
	 * 
	 * @param <T>
	 * @param listener
	 * @return
	 */
	<T extends IDomainObjectReferenceListener> T addListener(T listener);

	/**
	 * Deregister interest in changes to the value or prerequisites of 
	 * this reference.
	 * 
	 * @param listener
	 */
	void removeListener(IDomainObjectReferenceListener listener);
	
	/**
	 * Prerequisites applicable to access this reference.
	 * 
	 * <p>
	 * In the programming model, the prerequisites 
	 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
	 * returned by the <code>getXxxPre()</code> method. Note there may also be
	 * prerequisites corresponding to whether the reference can be 
	 * modified / added to / removed from.
	 * 
	 * <p>
	 * In addition, there may be authorization prerequisites, see
	 * {@link #authorizationPrerequisitesFor()}.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @param attribute
	 */
	public IPrerequisites accessorPrerequisitesFor();

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