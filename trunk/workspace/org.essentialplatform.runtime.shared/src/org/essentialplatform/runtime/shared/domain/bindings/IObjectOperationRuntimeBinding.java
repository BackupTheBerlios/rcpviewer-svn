package org.essentialplatform.runtime.shared.domain.bindings;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IOperation;

/**
 * Represents runtime-specific functionality (common to client and server) for 
 * an <i>instance of</i> an {@link IOperation} of a {@link IDomainClass}.
 * 
 * <p>
 * @see IObjectOperationClientBinding. 
 * 
 * @author Dan Haywood
 */
public interface IObjectOperationRuntimeBinding 
		extends IObjectMemberRuntimeBinding {

	/**
	 * Invoked after {@link IDomainObject#getOperation(IDomainClass.IOperation)}
	 * so that the binding can perform any further processing.
	 * 
	 * <p>
	 * For example, on the client, can add the operation to the list of
	 * observed features.
	 */
	public void gotOperation();


	/**
	 * Resets the argument list back to defaults.
	 * 
	 * <p>
	 * If a defaults method has been provided (<code>XxxDefaults(..)</code>),
	 * then it will be invoked.  Otherwise the built-in defaults for each
	 * type will be used (null for objects, 0 for int etc).
	 * 
	 * @return the reset arguments, same as {@link #getArgs()}.
	 * 
	 */
	Object[] reset();

}