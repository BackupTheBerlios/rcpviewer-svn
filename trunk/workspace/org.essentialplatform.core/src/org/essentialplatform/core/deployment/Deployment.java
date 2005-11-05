package org.essentialplatform.core.deployment;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.osgi.framework.Bundle;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;
import org.essentialplatform.core.domain.IDomainClass.IOneToOneReference;
import org.essentialplatform.core.domain.IDomainClass.IOperation;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.progmodel.ProgrammingModelException;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.InDomain;


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
	public interface IClassBinding<T> {
		public T newInstance();
		
		/**
		 * Returns the specified annotation (if any) on the class.
		 * 
		 * @param <T>
		 * @param annotationClass
		 * @return
		 */
		public <Q extends Annotation> Q getAnnotation(Class<Q> annotationClass);

	}
	public interface IAttributeBinding {
		Object invokeAccessor(Object pojo);
		void invokeMutator(Object pojo, Object newValue);
		IPrerequisites accessorPrerequisitesFor(Object pojo);
		IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
	}
	public interface IReferenceBinding {
		/**
		 * Returns the pojo for a 1:1 reference, or a collection of pojos for
		 * a collection reference.
		 * 
		 * @param pojo
		 * @return
		 */
		Object invokeAccessor(Object pojo);
		IPrerequisites authorizationPrerequisites();
		IPrerequisites accessorPrerequisitesFor(Object pojo);
	}
	public interface IOneToOneReferenceBinding extends IReferenceBinding {
		void invokeAssociator(Object pojo, Object referencedPojo);
		void invokeDissociator(Object pojo, Object referencedPojo);
		IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue);
	}
	public interface ICollectionReferenceBinding extends IReferenceBinding {
		void invokeAddTo(Object pojo, Object referencedPojo);
		void invokeRemoveFrom(Object pojo, Object referencedPojo);
		IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue, boolean beingAdded);
	}
	public interface IOperationBinding {

		Object invokeOperation(Object pojo, Object[] args);

		void assertIsValid(int position, Object arg);

		Object[] getArgs(Map<Integer, Object> argsByPosition);

		IPrerequisites prerequisitesFor(Object pojo, Object[] args);

		Object[] reset(Object pojo, Object[] args, Map<Integer, Object> argsByPosition); 
	}

}
