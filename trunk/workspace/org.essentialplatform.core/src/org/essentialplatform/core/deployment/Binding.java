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
public abstract class Binding {
	
	/**
	 * Sets the binding, as returned by {@link #getDeployment()}.
	 * 
	 * @throws RuntimeException if a binding has already been instantiated.
	 */
	public synchronized static void setBinding(Binding binding) {
		if (__binding != null) {
			throw new RuntimeException("Binding already defined.");
		}
		__binding = binding;
	}

	/**
	 * The current binding (if any).
	 */
	private static Binding __binding;
	
	/**
	 * Returns the current binding.
	 * 
	 * 
	 * @return
	 * @throws RuntimeException if no binding has been instantiated.
	 */
	public static Binding getDeployment() {
		if (__binding == null) {
			throw new RuntimeException("No binding set.");
		}
		return __binding;
	}

	public abstract IDomainBinding bindingFor(IDomain domain);

	// JAVA5_FIXME: return type
	public abstract IClassBinding bind(IDomainClass domainClass, Object classRepresentation);

	public abstract IAttributeBinding bindingFor(IAttribute attribute);

	// JAVA5_FIXME: return type
	public abstract IOneToOneReferenceBinding bindingFor(IOneToOneReference oneToOneReference);

	// JAVA5_FIXME: return type
	public abstract ICollectionReferenceBinding bindingFor(ICollectionReference collectionReference);

	public abstract IOperationBinding bindingFor(IOperation operation);
	
	public abstract IDomainBuilder getPrimaryBuilder();

	public abstract InDomain getInDomainOf(Object classRepresentation);

	/**
	 * For testing.
	 *
	 */
	public static void reset() {
		__binding = null;
	}

	public abstract Bundle getBundle();

}
