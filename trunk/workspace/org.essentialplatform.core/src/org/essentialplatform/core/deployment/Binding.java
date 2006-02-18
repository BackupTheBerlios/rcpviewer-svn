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
 * There are two a number of bindings: COMPILETIME, CLIENT-SIDE RUNTIME and
 * SERVER-SIDE RUNTIME.  This class also acts as a thread-local singleton 
 * registry of either (a little similar to {@link java.util.Calendar}
 * with {@link java.util.GregorianCalendar}, albeit on a per-thread basis).
 * 
 * @author Dan Haywood
 */
public abstract class Binding implements IBinding {
	
	/**
	 * Sets the binding, as returned by {@link #getBinding()}.
	 * 
	 * <p>
	 * A binding <i>can</i> be replaced, but <i>only</i> if the replacement is
	 * the exact same class.  (This constraint is to ensure that a given thread
	 * runs either client or server.  Previously we didn't allow any replacement
	 * at all, however the limited replacement does allow us to boot from
	 * Spring and refresh the context more than once). 
	 * 
	 * @throws RuntimeException if a binding has already been instantiated.
	 */
	public synchronized static void setBinding(IBinding binding) {
		IBinding currentBindingIfAny = __threadLocalBinding.get();
		if (currentBindingIfAny != null && currentBindingIfAny != binding) {
			if (currentBindingIfAny.getClass() != binding.getClass()) {
				throw new RuntimeException(String.format("A different binding '%s' has already defined for this thread (was trying to set binding '%s')", currentBindingIfAny, binding));
			}
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
	 * For class loading of adapters (extension object pattern) by
	 * {@link IDomainClass#getAdapters()}.
	 * 
	 * <p>
	 * Should provide the bundle that holds (or has access to) the adapter
	 * factory classes (rather than LouisPlugin or ServerPlugin which by
	 * definition will not).
	 * 
	 * @return
	 */
	public abstract Bundle getBundle();

	/**
	 * For testing.
	 *
	 */
	public static void reset() {
		__threadLocalBinding.set(null);
	}

}
