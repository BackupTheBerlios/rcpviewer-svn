package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IOneToOneReference;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.client.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOneToOneReferenceRuntimeBinding;

/**
 * Represents client-specific functionality for an <i>instance of</i> an 
 * {@link IOneToOneReference} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectOneToOneReferenceClientBinding 
		extends IObjectOneToOneReferenceRuntimeBinding, IObjectReferenceClientBinding {
	/**
	 * Prerequisites applicable to modify this single-valued (simple)
	 * reference with a specific reference (in other words, validation).
	 * 
	 * <p>
	 * The prerequisites are dependent upon the candidate referenced
	 * object.  For example, an reference might not accept certain objects; 
	 * if so then these prerequisites would effectively veto that candidate value.
	 * 
	 * <p>
	 * In the programming model, the domain object prerequisites 
	 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
	 * returned by the <code>setXxxPre(..)</code> method.  Note there will also 
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
	public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue);

	/**
	 * Convenience method that combines all prerequisites to access/modify
	 * a single-valued reference (that is, representing a simple reference
	 * rather than a collection).
	 * 
	 * <p>
	 * There are three sets of prerequisites that can apply to a
	 * single-valued reference:
	 * <ul>
	 * <li> the prerequisites to access the reference, as defined in the 
	 *      programming model by the <code>getXxxPre()</code>; 
	 *      see {@link #accessorPrerequisitesFor()}
	 * <li> the prerequisites to modify the reference, as defined in the
	 *      programming model by the <code>setXxxPre(...)</code>;
	 *      see {@link #mutatorPrerequisitesFor(Object)}.
	 * <li> the prerequisites of the configured {@link IAuthorizationManager},
	 *      see {@link #authorizationPrerequisitesFor()}.
	 * </ul>
	 *  
	 * <p>
	 * If there is no mutator then the method can still be called with
	 * <code>null</code> as the candidate value.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @param candidateValue
	 * @return
	 */
	public IPrerequisites prerequisitesFor(final Object candidateValue);
	
	/**
	 * Notify listeners of a 1:1 reference that it the reference is now 
	 * to a new referenced object (or possibly <code>null</code>).
	 * 
	 * <p>
	 * For use by aspects, not general use.
	 * 
	 * @param newReferencedObjectOrNull
	 */
	public void notifyListeners(Object newReferencedObjectOrNull);

}