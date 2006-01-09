package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.client.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectCollectionReferenceRuntimeBinding;

/**
 * Represents client-specific functionality for an <i>instance of</i> an 
 * {@link ICollectionReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectCollectionReferenceClientBinding 
		extends IObjectCollectionReferenceRuntimeBinding, IObjectReferenceClientBinding {

	/**
	 * Prerequisites applicable to add a reference or remove from a
	 * reference from this multi-valued reference (in other words, 
	 * validation of the contents of a collection).
	 * 
	 * <p>
	 * The prerequisites are dependent upon the candidate referenced object.
	 * For example, a collection might not accept certain objects; if so 
	 * then these prerequisites would effectively veto that candidate 
	 * reference.  Or, it might not be possible to remove an object once
	 * added.
	 * 
	 * <p>
	 * In the programming model, the domain object prerequisites 
	 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
	 * returned by the <code>addToXxxPre(..)</code> method and 
	 * <code>removeFromXxxPre(..)</code>.  Note there will also 
	 * be prerequisites corresponding to be able to access the attribute, 
	 * see {@link #accessorPrerequisitesFor()}.  In the 
	 * programming model these correspond to the <code>getXxxPre()</code> 
	 * method.
	 * 
	 * <p>
	 * In addition, there may be authorization prerequisites, see
	 * {@link #authorizationPrerequisitesFor()}.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @param candidateValue - the new value that this attribute can be allowed to take, if meets prerequisites.
	 */
	public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue, boolean beingAdded);


	/**
	 * Convenience method that combines all prerequisites to access/modify
	 * a multi-valued reference representing a collection.
	 * 
	 * <p>
	 * There are three sets of prerequisites that can apply to a
	 * multi-valued reference:
	 * <ul>
	 * <li> the prerequisites to access the reference, as defined in the 
	 *      programming model by the <code>getXxxPre()</code>; 
	 *      see {@link #accessorPrerequisitesFor()}
	 * <li> the prerequisites to either add to or remove from the collection,
	 *      as defined in the programming model as either 
	 *      <code>addToXxxPre(...)</code> or <code>removeFromXxxPre(...)</code>.
	 *      The value of the <code>beingAdded</code> parameter is used to 
	 *      check the appropriate prerequisite; 
	 *      {@link #mutatorPrerequisitesFor(Object, boolean)}.
	 * <li> the prerequisites of the configured {@link IAuthorizationManager},
	 *      see {@link #authorizationPrerequisitesFor()}.
	 * </ul>
	 *  
	 * <p>
	 * If there is no mutator then the method can still be called with
	 * <code>null</code> as the <code>candidateValue</code>; the value of
	 * the <code>beingAdded</code> parameter is ignored.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @param candidateValue
	 * @param beingAdded - true if being add to the collection, false if being removed. 
	 * @return
	 */
	public IPrerequisites prerequisitesFor(final Object candidateValue, boolean beingAdded);

	/**
	 * Notify listeners of a collection that it the reference has had 
	 * added to it a new object.
	 * 
	 * <p>
	 * Do not use otherwise.
	 * 
	 * @param referencedObject
	 * @param beingAdded - <code>true</code> if being added to, <code>false</code> if being removed.
	 */
	public void notifyListeners(Object referencedObject, boolean beingAdded);


}