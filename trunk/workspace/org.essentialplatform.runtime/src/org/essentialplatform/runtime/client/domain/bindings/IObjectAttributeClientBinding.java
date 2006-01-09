package org.essentialplatform.runtime.client.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.client.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.client.domain.event.IDomainObjectAttributeListener;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectAttributeRuntimeBinding;

/**
 * Represents client-specific functionality for an <i>instance of</i> an
 * {@link IAttribute} of a {@link IDomainClass}.
 * 
 * @author Dan Haywood
 */
public interface IObjectAttributeClientBinding extends IObjectAttributeRuntimeBinding, IObjectMemberClientBinding {
	
	/**
	 * Prerequisites applicable to access this attribute.
	 * 
	 * <p>
	 * In the programming model, the prerequisites 
	 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
	 * returned by the <code>getXxxPre()</code> method.  Note there may also be
	 * prerequisites corresponding to whether the attribute can be modified
	 * (in other words validation), see {@link #mutatorPrerequisitesFor()}.
	 * In the programming model these correspond to the 
	 * <code>setXxxPre(...)</code> method.
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
	 * Convenience method that combines all prerequisites to access/modify
	 * an attribute.
	 * 
	 * <p>
	 * There are three sets of prerequisites that can apply:
	 * <ul>
	 * <li> the prerequisites to access the attribute, as defined in the 
	 *      programming model by the <code>getXxxPre()</code>; 
	 *      see {@link #accessorPrerequisitesFor()}
	 * <li> the prerequisites to modify the attribute, as defined in the
	 *      programming model by the <code>setXxxPre(...)</code>;
	 *      see {@link #mutatorPrerequisitesFor(, Object)}.
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
	 * Prerequisites applicable to modify this attribute with a specific value
	 * (in other words, validation).
	 * 
	 * <p>
	 * The prerequisites are dependent upon the candidate value for the
	 * attribute.  For example, an attribute might not accept negative values;
	 * if so then these prerequisites would effectively veto that candidate
	 * value.
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
	 * Register interest in changes to either the values of this attribute
	 * or whether the (accessor) prerequisites or other relevant state of 
	 * this attribute changes.
	 * 
	 * @param <T>
	 * @param listener
	 * @return
	 */
	<T extends IDomainObjectAttributeListener> T  addListener(T listener);
	
	/**
	 * Deregister interest in changes to the values or prerequisites of 
	 * this attribute.
	 * 
	 * @param listener
	 */
	void removeListener(IDomainObjectAttributeListener listener);

	/**
	 * Notify listeners that this attribute has a new value.
	 * 
	 * <p>
	 * Public so that it can be invoked by NotifyListenersAspect.
	 * 
	 * @param attribute
	 * @param newValue
	 */
	public void notifyListeners(Object newValue);

}