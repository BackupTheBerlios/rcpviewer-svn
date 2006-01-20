package org.essentialplatform.runtime.client.domain.bindings;

import org.eclipse.emf.ecore.EAttribute;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IOperation;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.client.domain.event.IDomainObjectOperationListener;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOperationRuntimeBinding;

/**
 * Represents client-specific functionality for an <i>instance of</i> an 
 * {@link IOperation} of a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectOperationClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IObjectOperationClientBinding 
		extends IObjectOperationRuntimeBinding, IObjectMemberClientBinding {

	/**
	 * The pending arguments to be invoked for this operation and with which
	 * the prerequisites will be evaluated.
	 * 
	 * @return
	 */
	public Object[] getArgs();

	/**
	 * Specify an argument with which to invoke this operation.
	 * 
	 * @param position - the 0-based position 
	 * @param args
	 */
	void setArg(int position, Object arg);

	/**
	 * Invoke the operation with the current set of arguments, applying any 
	 * preconditions before hand.
	 * 
	 * @return the return value from the operation (if not void).
	 */
	public Object invokeOperation();


	/**
	 * Prerequisites applicable to invoke this operation.
	 * 
	 * <p>
	 * In the programming model, the prerequisites 
	 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
	 * returned by the <code>xxxPre(..)</code> method (where 
	 * <code>xxx(..)</code> is the name of the operation itself).
	 * 
	 * <p>
	 * The prerequisites will be evaluated against the set of arguments
	 * currently specified, where {@link #setArg(int, Object[])} is used
	 * to set and {@link #getArgs()} can be used to retrieve. 
	 * 
	 * <p>
	 * In addition, there may be authorization prerequisites, see
	 * {@link #authorizationPrerequisitesFor(EAttribute)}.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 */
	IPrerequisites prerequisitesFor();

	/**
	 * Register interest in whether this operation is invoked or in
	 * whether the prerequisites (for the currently held arguments) or 
	 * other relevant state of this operation changes.
	 * 
	 * <p>
	 * If the listener is already known, does nothing.
	 * 
	 * <p> 
	 * Returns listener only because it simplifies test implementation to do
	 * so.
	 * 
	 * @param <T>
	 * @param listener
	 * @return
	 */
	<T extends IDomainObjectOperationListener> T addListener(T listener);
	
	/**
	 * Deregister interest in this operation and its prerequisite state.
	 * 
	 * @param listener
	 */
	void removeListener(IDomainObjectOperationListener listener);

	void notifyOperationListeners(IPrerequisites newPrerequisites);

	

}