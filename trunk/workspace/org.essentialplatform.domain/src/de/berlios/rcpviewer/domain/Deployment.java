package de.berlios.rcpviewer.domain;

import org.eclipse.emf.ecore.EClass;
import org.osgi.framework.Bundle;

import de.berlios.rcpviewer.domain.IDomainClass.IAttribute;
import de.berlios.rcpviewer.domain.IDomainClass.ICollectionReference;
import de.berlios.rcpviewer.domain.IDomainClass.IOneToOneReference;
import de.berlios.rcpviewer.domain.IDomainClass.IOperation;
import de.berlios.rcpviewer.progmodel.standard.InDomain;


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
public abstract class Deployment {
	
	/**
	 * Sets the binding, as returned by {@link #getDeployment()}.
	 * 
	 * @throws RuntimeException if a binding has already been instantiated.
	 */
	protected Deployment() {
		synchronized(Deployment.class) {
			if (__deployment != null) {
				throw new RuntimeException("Binding already defined.");
			}
			__deployment = this;
		}
	}

	/**
	 * The current binding (if any).
	 */
	private static Deployment __deployment;
	
	/**
	 * Returns the current binding.
	 * 
	 * 
	 * @return
	 * @throws RuntimeException if no binding has been instantiated.
	 */
	public static Deployment getDeployment() {
		if (__deployment == null) {
			throw new RuntimeException("No binding set.");
		}
		return __deployment;
	}

	// JAVA5_FIXME: return type
	public abstract IDomainBinding bindingFor(IDomain domain);

	// JAVA5_FIXME: return type
	public abstract IClassBinding bindingFor(IDomainClass domainClass, Object classRepresentation);

	// JAVA5_FIXME: return type
	public abstract IAttributeBinding bindingFor(IAttribute attribute);

	// JAVA5_FIXME: return type
	public abstract IOneToOneReferenceBinding bindingFor(IOneToOneReference oneToOneReference);

	// JAVA5_FIXME: return type
	public abstract ICollectionReferenceBinding bindingFor(ICollectionReference collectionReference);

	// JAVA5_FIXME: return type
	public abstract IOperationBinding bindingFor(IOperation operation);
	
	public abstract IDomainBuilder getPrimaryBuilder();

	public abstract InDomain getInDomainOf(Object classRepresentation);

	/**
	 * For testing.
	 *
	 */
	public static void reset() {
		__deployment = null;
	}

	public abstract Bundle getBundle();

	public interface IDomainBinding {
		public String getPackageNameFor(final Object classRepresentation);
		public String getClassSimpleNameFor(final Object classRepresentation);
		/**
		 * Perform any post-creation processing on the EClass specific to this
		 * deployment binding.
		 * 
		 * <p>
		 * For example, runtime binding will set the Java {@link java.lang.Class}
		 * on the {@link EClass}' <tt>instanceClass</tt> property.
		 * @param eClass
		 * @param classRepresentation
		 */
		public void processEClass(final EClass eClass, final Object classRepresentation);
		
		/**
		 * Ensure that this class representation is valid for this deployment.
		 * 
		 * <p>
		 * The implementation does not need to check for null.
		 * 
		 * @param classRepresentation - will be non-null.
		 */
		public void assertValid(final Object classRepresentation);
		
		/**
		 * Lookup of this deployment's representation of the class for the
		 * specified {@link EClass}.
		 * 
		 * @param eClass
		 * @return
		 */
		public Object classRepresentationFor(EClass eClass);
	}
	public interface IClassBinding {
	}
	public interface IAttributeBinding {
	}
	public interface IOneToOneReferenceBinding {
	}
	public interface ICollectionReferenceBinding {
	}
	public interface IOperationBinding {
	}

}
