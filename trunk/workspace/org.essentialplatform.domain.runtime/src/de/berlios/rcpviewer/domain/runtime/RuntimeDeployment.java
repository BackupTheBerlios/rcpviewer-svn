package de.berlios.rcpviewer.domain.runtime;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.osgi.framework.Bundle;

import de.berlios.rcpviewer.authorization.IAuthorizationManager;
import de.berlios.rcpviewer.domain.Deployment;
import de.berlios.rcpviewer.domain.DomainClass;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainBuilder;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainClass.IAttribute;
import de.berlios.rcpviewer.domain.IDomainClass.ICollectionReference;
import de.berlios.rcpviewer.progmodel.ProgrammingModelException;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.RelativeOrder;
import de.berlios.rcpviewer.progmodel.rcpviewer.RcpViewerProgModelSemanticsEmfSerializer;
import de.berlios.rcpviewer.progmodel.standard.DomainObject;
import de.berlios.rcpviewer.progmodel.standard.ExtendedProgModelSemanticsEmfSerializer;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.OppositeReferencesIdentifier;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelDomainBuilder;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelSemanticsEmfSerializer;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.local.Session;

/**
 * A binding of {@link IDomainClass} for the runtime environment.
 * 
 * @author Dan Haywood
 */
public final class RuntimeDeployment extends Deployment {

	private static StandardProgModelSemanticsEmfSerializer __standardSerializer = new StandardProgModelSemanticsEmfSerializer();
	private static ExtendedProgModelSemanticsEmfSerializer __extendedSerializer = new ExtendedProgModelSemanticsEmfSerializer();
	private static RcpViewerProgModelSemanticsEmfSerializer __rcpViewerSerializer = new RcpViewerProgModelSemanticsEmfSerializer();

	//////////////////////////////////////////////////////////////////////


	/**
	 * Sets the current binding to be RUNTIME.
	 * 
	 * @throws RuntimeException if a binding has already been set.
	 */
	public RuntimeDeployment() {}

	@Override
	public Bundle getBundle() {
		return Platform.getBundle("de.berlios.rcpviewer.domain.runtime");
	}

	@Override
	public IDomainBuilder getPrimaryBuilder() {
		return new StandardProgModelDomainBuilder();
	}

	@Override
	public final InDomain getInDomainOf(final Object classRepresentation) {
		return getInDomainOf((Class<?>)classRepresentation);
	}
	private <V> InDomain getInDomainOf(final Class<V> javaClass) {
		InDomain inDomain = javaClass.getAnnotation(InDomain.class);
		if (inDomain == null) {
			return null;
		}
		return inDomain;
	}

	@Override
	public IDomainBinding bindingFor(IDomain domain) {
		return new RuntimeDomainBinding(domain);
	}
	@Override
	public IClassBinding bindingFor(IDomainClass domainClass, Object classRepresentation) {
		return bindingFor(domainClass, (Class<?>)classRepresentation);
	}
	private <V> RuntimeClassBinding<V> bindingFor(IDomainClass domainClass, Class<V> javaClass) {
		return new RuntimeClassBinding<V>(domainClass, javaClass);
	}
	
	@Override
	public IAttributeBinding bindingFor(IDomainClass.IAttribute attribute) {
		return new RuntimeAttributeBinding(attribute);
	}
	@Override
	public IOneToOneReferenceBinding bindingFor(IDomainClass.IOneToOneReference oneToOneReference) {
		return new RuntimeOneToOneReferenceBinding(oneToOneReference);
	}
	@Override
	public ICollectionReferenceBinding bindingFor(IDomainClass.ICollectionReference collectionReference) {
		return new RuntimeCollectionReferenceBinding(collectionReference);
	}
	@Override
	public IOperationBinding bindingFor(IDomainClass.IOperation operation) {
		return new RuntimeOperationBinding(operation);
	}

	//////////////////////////////////////////////////////////////////////
	
	public final static class RuntimeDomainBinding implements IDomainBinding {
		private final IDomain _domain;

		/**
		 * Defaults to {@link IAuthorizationManager#NOOP} but can be overridden
		 * by dependency injection if required.
		 */
		private IAuthorizationManager _authorizationManager = IAuthorizationManager.NOOP;
		
		public RuntimeDomainBinding(IDomain domain) {
			_domain = domain;
		}
		public IDomain getDomain() {
			return _domain;
		}
		
		/*
		 * @see de.berlios.rcpviewer.domain.Deployment.IDomainBinding#getPackageNameFor(java.lang.Object)
		 */
		public String getPackageNameFor(final Object classRepresentation) {
			return getPackageNameFor((Class<?>)classRepresentation);
		}
		private <V> String getPackageNameFor(final Class<V> javaClass) {
			Package javaPackage = javaClass.getPackage();
			return javaPackage.getName();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.Deployment.IDomainBinding#getClassSimpleNameFor(java.lang.Object)
		 */
		public String getClassSimpleNameFor(final Object classRepresentation) {
			return getClassSimpleNameFor((Class<?>)classRepresentation);
		}
		private <V> String getClassSimpleNameFor(final Class<V> javaClass) {
			return javaClass.getSimpleName();
		}
		/*
		 * @see de.berlios.rcpviewer.domain.Deployment.IDomainBinding#processEClass(org.eclipse.emf.ecore.EClass, java.lang.Object)
		 */
		public void processEClass(final EClass eClass, final Object classRepresentation) {
			processEClass(eClass, (Class<?>)classRepresentation);
		}
		private <V> void processEClass(final EClass eClass, final Class<V> javaClass) {
			eClass.setInstanceClass(javaClass);
		}
		/*
		 * @see de.berlios.rcpviewer.domain.Deployment.IDomainBinding#classRepresentationfor(org.eclipse.emf.ecore.EClass)
		 */
		public Object classRepresentationFor(final EClass eClass) {
			return (Class<?>)eClass.getInstanceClass();
		}


		/*
		 * @see de.berlios.rcpviewer.domain.Deployment.IDomainBinding#assertValid(java.lang.Object)
		 */
		public void assertValid(final Object classRepresentation) {
			if (!(classRepresentation instanceof Class)) {
				throw new IllegalArgumentException(
					"Class representation is not an instance of java.lang.Class (is a " + classRepresentation.getClass().getCanonicalName() + ")");
			}
			assertValid((Class<?>)classRepresentation);
		}
		private <V> void assertValid(final Class<V> javaClass) {
			if ( javaClass.isPrimitive() ) {
				throw new IllegalArgumentException("Java class is primitive.");
			}
		}

		/**
		 * Authorization manager to enforce constraints.
		 * 
		 * <p> 
		 * Defaults to {@link IAuthorizationManager#NOOP} but can be overridden
		 * using dependency injection, see {@link #setAuthorizationManager(IAuthorizationManager)}.
		 * 
		 * @see de.berlios.rcpviewer.domain.runtime.IRuntimeDomain#getAuthorizationManager()
		 */
		public IAuthorizationManager getAuthorizationManager() {
			return _authorizationManager;
		}
		/**
		 * Set the authorization manager for this binding of the domain to a
		 * runtime deployment.
		 * 
		 * @param authorizationManager
		 */
		public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
			if (authorizationManager == null) {
				throw new IllegalArgumentException("IAuthorizationManager may not be null - consider IAuthorizationManager.NOOP instead");
			}
			_authorizationManager = authorizationManager;
		}


	}
	
	public final static class RuntimeClassBinding<T> implements IClassBinding {

		private final IDomainClass _domainClass;
		private final Class<T> _javaClass;
		private final EClass _eClass;
		
		public RuntimeClassBinding(IDomainClass domainClass, Class<T> javaClass) {
			_domainClass = domainClass;
			_javaClass = javaClass;
			_eClass = domainClass.getEClass();
		}
		
		public Class<T> getJavaClass() {
			return _javaClass;
		}
		
		public T newInstance() throws ProgrammingModelException {
			try {
				return getJavaClass().newInstance();
			} catch(IllegalAccessException ex) {
				throw new ProgrammingModelException("Cannot instantiate", ex);
			} catch(InstantiationException ex) {
				throw new ProgrammingModelException("Cannot instantiate", ex);
			}
		}
		
		/**
		 * Returns the specified annotation (if any) on the class.
		 * 
		 * @param <T>
		 * @param annotationClass
		 * @return
		 */
		public <Q extends Annotation> Q getAnnotation(Class<Q> annotationClass) {
			return _javaClass.getAnnotation(annotationClass);
		}
		
	}

	public final static class RuntimeAttributeBinding implements IAttributeBinding {

		private final IDomainClass.IAttribute _attribute;
		private final EAttribute _eAttribute;
		
		/**
		 * Derived, may be null for write-only attribute.
		 */
		private final Method _accessor;
		/**
		 * Derived, may be null for write-only reference.
		 */
		private final Method _mutator;
		/**
		 * Derived, typically is the accessor method, but will be the mutator 
		 * method for a write-only attribute; never null.
		 */
		private final Method _accessorOrMutator;
		/**
		 * Derived, may be null.
		 */
		private final Method _accessorPre;
		/**
		 * Derived, may be null.
		 */
		private final Method _mutatorPre;

		public RuntimeAttributeBinding(IDomainClass.IAttribute attribute) {
			_attribute = attribute;
			_eAttribute = _attribute.getEAttribute();
			
			_accessor = __standardSerializer.getAttributeAccessorMethod(_eAttribute);
			_mutator = __standardSerializer.getAttributeMutatorMethod(_eAttribute);
			_accessorOrMutator = _accessor!=null?_accessor:_mutator;
			
			_accessorPre = __extendedSerializer.getAttributeAccessorPreMethod(_eAttribute);
			_mutatorPre = __extendedSerializer.getAttributeMutatorPreMethod(_eAttribute);
		}
		
		public Object invokeAccessor(final Object pojo) {
			if (_accessor == null) {
				throw new UnsupportedOperationException("Accesor method '" + _accessor + "' not accessible / could not be found");
			}
			String accessorMethodName = _accessor.getName();
			try {
				return _accessor.invoke(pojo);
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Accessor method '" + accessorMethodName + "' not accessible", e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
			}
		}

		public void invokeMutator(final Object pojo, final Object newValue) {
			if (_mutator == null) {
				throw new UnsupportedOperationException("Mutator method '" + _mutator + "' not accessible / could not be found");
			}
			String mutatorMethodName = _mutator.getName();
			try {
				_mutator.invoke(pojo, newValue);
				// previously there was a call to notifyAttributeListeners(newValue) here;
				// however this is superfluous since the NotifyListeners aspect will do
				// our bidding for us.
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Mutator method '" + mutatorMethodName + "' not accessible");
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke mutator method '" + mutatorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke mutator method '" + mutatorMethodName + "'", e.getTargetException());
			}
		}
		
		/**
		 * Returns the specified annotation (if any) on the accessor for the
		 * attribute (or the mutator if this is a write-only attribute).
		 * 
		 * @param <T>
		 * @param annotationClss
		 * @return
		 */
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			return _accessorOrMutator.getAnnotation(annotationClass);
		}

		public IPrerequisites authorizationPrerequisites() {
			IDomain domain = _attribute.getDomainClass().getDomain();
			RuntimeDomainBinding domainBinding = (RuntimeDomainBinding)domain.getBinding();
			return domainBinding.getAuthorizationManager().preconditionsFor(_attribute.attributeIdFor());
		}
		
	}

	static abstract class AbstractRuntimeReferenceBinding {
		
		final IDomainClass.IReference _reference;
		final EReference _eReference;

		final Method _accessor;
		final Method _mutator;

		public AbstractRuntimeReferenceBinding(IDomainClass.IReference reference) {
			_reference = reference;
			_eReference = reference.getEReference();
			
			_accessor = __standardSerializer.getReferenceAccessor(_eReference);
			_mutator = __standardSerializer.getReferenceMutator(_eReference);
		}
		
		public Object invokeAccessor(final Object pojo) {
			if (_accessor == null) {
				throw new UnsupportedOperationException("No accessor method");
			}
			String accessorMethodName = _accessor.getName();
			try {
				return _accessor.invoke(pojo);
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Accessor method '" + accessorMethodName + "' not accessible", e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke accessor method '" + accessorMethodName + "'", e);
			}
		}

		public IPrerequisites authorizationPrerequisites() {
			IDomain domain = _reference.getDomainClass().getDomain();
			RuntimeDomainBinding domainBinding = (RuntimeDomainBinding)domain.getBinding();
			return domainBinding.getAuthorizationManager().preconditionsFor(_reference.referenceIdFor());
		}

	}
	
	public final static class RuntimeOneToOneReferenceBinding extends AbstractRuntimeReferenceBinding implements IOneToOneReferenceBinding {

		private final IDomainClass.IOneToOneReference _oneToOneReference;
		private final Method _associator;
		private final Method _dissociator;
		private final Method _associatorPre;
		private final Method _dissociatorPre;

		public RuntimeOneToOneReferenceBinding(IDomainClass.IOneToOneReference oneToOneReference) {
			super(oneToOneReference);
			_oneToOneReference = oneToOneReference; // downcast
			
			_associator = __standardSerializer.getReferenceOneToOneAssociator(_eReference);
			_dissociator = __standardSerializer.getReferenceOneToOneDissociator(_eReference);
			_associatorPre = __extendedSerializer.getReferenceAccessorPreMethod(_eReference);
			_dissociatorPre = __extendedSerializer.getReferenceMutatorPreMethod(_eReference);
		}

		public boolean canAssociate() {
			return _associator != null;
		}
		public Object invokeAssociator(final Object pojo, final Object referencedObject) {
			if (_associator == null) {
				throw new UnsupportedOperationException("No associator method");
			}
			String associatorMethodName = _associator.getName();
			try {
				return _associator.invoke(pojo, new Object[]{referencedObject});
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Associator method '" + associatorMethodName + "' not accessible", e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke associator method '" + associatorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke associator method '" + associatorMethodName + "'", e);
			}
		}
		public boolean canDissociate() {
			return _dissociator != null;
		}
		public Object invokeDissociator(final Object pojo, final Object referencedObject) {
			if (_dissociator == null) {
				throw new UnsupportedOperationException("No dissociator method");
			}
			String dissociatorMethodName = _dissociator.getName();
			try {
				return _dissociator.invoke(pojo, new Object[]{referencedObject});
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Dissociator method '" + dissociatorMethodName + "' not accessible", e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke dissociator method '" + dissociatorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke dissociator method '" + dissociatorMethodName + "'", e);
			}
		}
	}
		
	public final static class RuntimeCollectionReferenceBinding extends AbstractRuntimeReferenceBinding implements ICollectionReferenceBinding {

		private final IDomainClass.ICollectionReference _collectionReference;
		private final Method _associator;
		private final Method _dissociator;
		private final Method _associatorPre;
		private final Method _dissociatorPre;

		public RuntimeCollectionReferenceBinding(IDomainClass.ICollectionReference collectionReference) {
			super(collectionReference);
			_collectionReference = collectionReference; // downcast
			
			_associator = __standardSerializer.getReferenceCollectionAssociator(_eReference);
			_dissociator = __standardSerializer.getReferenceCollectionDissociator(_eReference);
			_associatorPre = __extendedSerializer.getReferenceAddToPreMethod(_eReference);
			_dissociatorPre = __extendedSerializer.getReferenceRemoveFromPreMethod(_eReference);
		}

		public boolean canAddTo() {
			return _associator != null;
		}
		public Object invokeAddTo(final Object pojo, final Object referencedObject) {
			if (_associator == null) {
				throw new UnsupportedOperationException("No associator method");
			}
			String associatorMethodName = _associator.getName();
			try {
				return _associator.invoke(pojo, new Object[]{referencedObject});
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Associator method '" + associatorMethodName + "' not accessible", e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke associator method '" + associatorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke associator method '" + associatorMethodName + "'", e);
			}
		}
		public boolean canRemoveFrom() {
			return _dissociator != null;
		}
		public Object invokeRemoveFrom(final Object pojo, final Object referencedObject) {
			if (_dissociator == null) {
				throw new UnsupportedOperationException("No dissociator method");
			}
			String dissociatorMethodName = _dissociator.getName();
			try {
				return _dissociator.invoke(pojo, new Object[]{referencedObject});
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Dissociator method '" + dissociatorMethodName + "' not accessible", e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke dissociator method '" + dissociatorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke dissociator method '" + dissociatorMethodName + "'", e);
			}
		}
	}

	public final static class RuntimeOperationBinding implements IOperationBinding {

		private final IDomainClass.IOperation _operation;
		private final EOperation _eOperation;
		
		/**
		 * Not null.
		 */
		private final Method _operationInvoker;
		/**
		 * Derived, may be null.
		 */
		private final Method _operationPre;
		/**
		 * Derived, may be null.
		 */
		private final Method _operationDefaults;

		public RuntimeOperationBinding(IDomainClass.IOperation operation) {
			_operation = operation;
			_eOperation = _operation.getEOperation();
			
			_operationInvoker = __standardSerializer.getOperationMethod(_eOperation);
			_operationPre = __extendedSerializer.getOperationPreMethod(_eOperation);
			_operationDefaults = __extendedSerializer.getOperationDefaultsMethod(_eOperation);
		}

		public Object invokeOperation(final Object pojo, final Object[] args) {
			if (_operationInvoker == null) {
				throw new UnsupportedOperationException("No operation invoker method.");
			}
			String operationInvokerMethodName = _operationInvoker.getName();
			try {
				return _operationInvoker.invoke(pojo, args);
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Operation invoker method '" + operationInvokerMethodName + "' not accessible");
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Operation invoker method '" + operationInvokerMethodName + "' not accessible");
			} catch (InvocationTargetException e) {
				throw new RuntimeException("Operation method threw an exception '" + operationInvokerMethodName + "'", e.getCause());
			}
		}

		public IPrerequisites authorizationPrerequisites() {
			IDomain domain = _operation.getDomainClass().getDomain();
			RuntimeDomainBinding domainBinding = (RuntimeDomainBinding)domain.getBinding();
			return domainBinding.getAuthorizationManager().preconditionsFor(_operation.operationIdFor());
		}

	}



}
