package org.essentialplatform.core.deployment;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;
import org.essentialplatform.core.domain.IDomainClass.IOneToOneReference;
import org.essentialplatform.core.domain.IDomainClass.IOperation;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.osgi.framework.Bundle;


/**
 * Parameterizes domain model with respect to how it has been deployed,
 * acting as an <i>Abstract Factory</i> (kit) for each.
 * 
 * <p>
 * There are two bindings: RUNTIME and COMPILETIME.  This class also
 * acts as a singleton registry of either (similar to {@link java.util.Calendar}
 * with {@link java.util.GregorianCalendar}
 * 
 * 
 * 
 * @author Dan Haywood
 */
public abstract class Binding implements IBinding {
	
	/**
	 * Sets the binding, as returned by {@link #getBinding()}.
	 * 
	 * @throws RuntimeException if a binding has already been instantiated.
	 */
	public synchronized static void setBinding(IBinding binding) {
		IBinding currentBindingIfAny = __threadLocalBinding.get();
		if (currentBindingIfAny != null && currentBindingIfAny != binding) {
			throw new RuntimeException(String.format("A different binding '%s' has already defined for this thread (was trying to set binding '%s')", currentBindingIfAny, binding));
		}
		__threadLocalBinding.set(binding);
	}

	/**
	 * Holds this thread's current binding.
	 */
	private static ThreadLocal<IBinding> __threadLocalBinding = new ThreadLocal();
	
	/**
	 * Returns the current binding.
	 * 
	 * 
	 * @return
	 * @throws RuntimeException if no binding has been instantiated.
	 */
	public static IBinding getBinding() {
		if (__threadLocalBinding == null) {
			throw new RuntimeException("No binding set.");
		}
		return __threadLocalBinding.get();
	}

	public abstract IDomainBinding bindingFor(IDomain domain);

	// JAVA5_FIXME: return type
	public abstract <V extends IDomainClassBinding> V bind(IDomainClass domainClass, Object classRepresentation);

	public abstract <V extends IAttributeBinding> V bindingFor(IAttribute attribute);

	// JAVA5_FIXME: return type
	public abstract <V extends IOneToOneReferenceBinding> V bindingFor(IOneToOneReference oneToOneReference);

	// JAVA5_FIXME: return type
	public abstract <V extends ICollectionReferenceBinding> V bindingFor(ICollectionReference collectionReference);

	public abstract <V extends IOperationBinding> V bindingFor(IOperation operation);
	
	public abstract IDomainBuilder getPrimaryBuilder();

	public abstract InDomain getInDomainOf(Object classRepresentation);

	/**
	 * for class loading.
	 * 
	 * @return
	 */
	public abstract Bundle getBundle();

	/**
	 * For testing.
	 *
	 */
	public static void reset() {
		__threadLocalBinding = null;
	}

}
