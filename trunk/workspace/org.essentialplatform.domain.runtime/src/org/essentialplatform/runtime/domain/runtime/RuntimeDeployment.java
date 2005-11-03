package org.essentialplatform.runtime.domain.runtime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.osgi.framework.Bundle;

import org.essentialplatform.core.deployment.Deployment;
import org.essentialplatform.core.domain.DomainClass;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.IDomainClass.IAttribute;
import org.essentialplatform.core.domain.IDomainClass.ICollectionReference;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.progmodel.ProgrammingModelException;
import org.essentialplatform.progmodel.essential.core.domain.OppositeReferencesIdentifier;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelExtendedSemanticsEmfSerializer;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelStandardSemanticsEmfSerializer;
import org.essentialplatform.progmodel.extended.IPrerequisites;
import org.essentialplatform.progmodel.extended.Prerequisites;
import org.essentialplatform.progmodel.extended.RelativeOrder;
import org.essentialplatform.progmodel.louis.core.emf.LouisProgModelSemanticsEmfSerializer;
import org.essentialplatform.progmodel.standard.InDomain;
import org.essentialplatform.runtime.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.progmodel.standard.DomainObject;
import org.essentialplatform.runtime.progmodel.standard.EssentialProgModelDomainBuilder;
import org.essentialplatform.runtime.session.IDomainObject;
import org.essentialplatform.runtime.session.local.Session;

/**
 * A binding of {@link IDomainClass} for the runtime environment.
 * 
 * @author Dan Haywood
 */
public final class RuntimeDeployment extends Deployment {
	
	private static Logger _logger = Logger.getLogger(RuntimeDeployment.class);

	private static EssentialProgModelStandardSemanticsEmfSerializer __standardSerializer = new EssentialProgModelStandardSemanticsEmfSerializer();
	private static EssentialProgModelExtendedSemanticsEmfSerializer __extendedSerializer = new EssentialProgModelExtendedSemanticsEmfSerializer();
	private static LouisProgModelSemanticsEmfSerializer __rcpViewerSerializer = new LouisProgModelSemanticsEmfSerializer();

	private static Map<Class<?>, Object> __defaultValueByPrimitiveType = new HashMap<Class<?>, Object>();
	private static Map<Class<?>, Class<?>> __wrapperTypeByPrimitiveType = new HashMap<Class<?>, Class<?>>();

	static {
		__defaultValueByPrimitiveType.put(byte.class, 0);
		__defaultValueByPrimitiveType.put(short.class, 0);
		__defaultValueByPrimitiveType.put(int.class, 0);
		__defaultValueByPrimitiveType.put(long.class, 0);
		__defaultValueByPrimitiveType.put(char.class, 0);
		__defaultValueByPrimitiveType.put(float.class, 0);
		__defaultValueByPrimitiveType.put(double.class, 0);
		__defaultValueByPrimitiveType.put(boolean.class, false);
		__defaultValueByPrimitiveType.put(String.class, null); // rather than
																// ""
		__defaultValueByPrimitiveType.put(Byte.class, 0);
		__defaultValueByPrimitiveType.put(Short.class, 0);
		__defaultValueByPrimitiveType.put(Integer.class, 0);
		__defaultValueByPrimitiveType.put(Long.class, 0);
		__defaultValueByPrimitiveType.put(Character.class, 0);
		__defaultValueByPrimitiveType.put(Float.class, 0);
		__defaultValueByPrimitiveType.put(Double.class, 0);
		__defaultValueByPrimitiveType.put(Boolean.class, false);
		__defaultValueByPrimitiveType
				.put(BigInteger.class, new BigInteger("0"));
		__defaultValueByPrimitiveType.put(BigDecimal.class, new BigDecimal(
				"0.0"));

		__wrapperTypeByPrimitiveType.put(byte.class, Byte.class);
		__wrapperTypeByPrimitiveType.put(short.class, Short.class);
		__wrapperTypeByPrimitiveType.put(int.class, Integer.class);
		__wrapperTypeByPrimitiveType.put(long.class, Long.class);
		__wrapperTypeByPrimitiveType.put(char.class, Character.class);
		__wrapperTypeByPrimitiveType.put(float.class, Float.class);
		__wrapperTypeByPrimitiveType.put(double.class, Double.class);
		__wrapperTypeByPrimitiveType.put(boolean.class, Boolean.class);

	}

	/**
	 * Converts primitive types into corresponding wrapped types, or leaves
	 * alone if not a primitive type.
	 * 
	 * @param type
	 * @return
	 */
	private Class<?> wrapperTypeIfRequired(Class<?> type) {
		Class<?> wrappedType = __wrapperTypeByPrimitiveType.get(type);
		if (wrappedType != null) {
			return wrappedType;
		} else {
			return type;
		}

	}


	//////////////////////////////////////////////////////////////////////


	/**
	 * Sets the current binding to be RUNTIME.
	 * 
	 * @throws RuntimeException if a binding has already been set.
	 */
	public RuntimeDeployment() {}

	@Override
	public Bundle getBundle() {
		return Platform.getBundle("org.essentialplatform.domain.runtime");
	}

	@Override
	public IDomainBuilder getPrimaryBuilder() {
		return new EssentialProgModelDomainBuilder();
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
	private <V> IClassBinding<V> bindingFor(IDomainClass domainClass, Class<V> javaClass) {
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
		 * @see org.essentialplatform.domain.Deployment.IDomainBinding#getPackageNameFor(java.lang.Object)
		 */
		public String getPackageNameFor(final Object classRepresentation) {
			return getPackageNameFor((Class<?>)classRepresentation);
		}
		private <V> String getPackageNameFor(final Class<V> javaClass) {
			Package javaPackage = javaClass.getPackage();
			return javaPackage.getName();
		}
		/*
		 * @see org.essentialplatform.domain.Deployment.IDomainBinding#getClassSimpleNameFor(java.lang.Object)
		 */
		public String getClassSimpleNameFor(final Object classRepresentation) {
			return getClassSimpleNameFor((Class<?>)classRepresentation);
		}
		private <V> String getClassSimpleNameFor(final Class<V> javaClass) {
			return javaClass.getSimpleName();
		}
		/*
		 * @see org.essentialplatform.domain.Deployment.IDomainBinding#processEClass(org.eclipse.emf.ecore.EClass, java.lang.Object)
		 */
		public void processEClass(final EClass eClass, final Object classRepresentation) {
			processEClass(eClass, (Class<?>)classRepresentation);
		}
		private <V> void processEClass(final EClass eClass, final Class<V> javaClass) {
			eClass.setInstanceClass(javaClass);
		}
		/*
		 * @see org.essentialplatform.domain.Deployment.IDomainBinding#classRepresentationfor(org.eclipse.emf.ecore.EClass)
		 */
		public Object classRepresentationFor(final EClass eClass) {
			return (Class<?>)eClass.getInstanceClass();
		}


		/*
		 * @see org.essentialplatform.domain.Deployment.IDomainBinding#assertValid(java.lang.Object)
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
		 * @see org.essentialplatform.domain.runtime.IRuntimeDomain#getAuthorizationManager()
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
	
	public final static class RuntimeClassBinding<T> implements IClassBinding<T> {

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
			return domainBinding.getAuthorizationManager().preconditionsFor(_attribute.getFeatureId());
		}

		public IPrerequisites accessorPrerequisitesFor(final Object pojo)  {
			Method accessorPre = __extendedSerializer.getAttributeAccessorPreMethod(_eAttribute);
			if (accessorPre == null) {
				return Prerequisites.none();
			}
			try {
				return (IPrerequisites)accessorPre.invoke(pojo, new Object[]{});
			} catch (IllegalArgumentException ex) {
				_logger.error("Problem obtaining accessor prerequisites for '" + _eAttribute + "'", ex);
				return Prerequisites.invisible();
			} catch (IllegalAccessException ex) {
				_logger.error("Problem obtaining accessor prerequisites for '" + _eAttribute + "'", ex);
				return Prerequisites.invisible();
			} catch (InvocationTargetException ex) {
				_logger.error("Problem obtaining accessor prerequisites for '" + _eAttribute + "'", ex);
				return Prerequisites.invisible();
			}
		}

		public IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue) {
			Method mutatorPre = __extendedSerializer.getAttributeMutatorPreMethod(_eAttribute);;
			if (mutatorPre == null) {
				return Prerequisites.none();
			}
			try {
				return (IPrerequisites)mutatorPre.invoke(pojo, new Object[]{candidateValue});
			} catch (IllegalArgumentException ex) {
				_logger.error("Problem obtaining mutator prerequisites for '" + _eAttribute + "'", ex);
				return Prerequisites.invisible();
			} catch (IllegalAccessException ex) {
				_logger.error("Problem obtaining mutator prerequisites for '" + _eAttribute + "'", ex);
				return Prerequisites.invisible();
			} catch (InvocationTargetException ex) {
				_logger.error("Problem obtaining mutator prerequisites for '" + _eAttribute + "'", ex);
				return Prerequisites.invisible();
			}
		}
		
	}

	public static abstract class AbstractRuntimeReferenceBinding {
		
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
			return domainBinding.getAuthorizationManager().preconditionsFor(_reference.getFeatureId());
		}

		public IPrerequisites accessorPrerequisitesFor(final Object pojo) {
			Method accessorPre = __extendedSerializer.getReferenceAccessorPreMethod(_eReference); 
			if (accessorPre == null) {
				return Prerequisites.none();
			}
			try {
				return (IPrerequisites)accessorPre.invoke(pojo, new Object[]{});
			} catch (IllegalArgumentException ex) {
				// TODO log?
			} catch (IllegalAccessException ex) {
				// TODO Auto-generated catch block
			} catch (InvocationTargetException ex) {
				// TODO Auto-generated catch block
			}
			return Prerequisites.none();
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
		public void invokeAssociator(final Object pojo, final Object referencedObject) {
			if (_associator == null) {
				throw new UnsupportedOperationException("No associator method");
			}
			String associatorMethodName = _associator.getName();
			try {
				_associator.invoke(pojo, new Object[]{referencedObject});
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
		public void invokeDissociator(final Object pojo, final Object referencedObject) {
			if (_dissociator == null) {
				throw new UnsupportedOperationException("No dissociator method");
			}
			String dissociatorMethodName = _dissociator.getName();
			try {
				_dissociator.invoke(pojo, new Object[]{referencedObject});
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Dissociator method '" + dissociatorMethodName + "' not accessible", e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke dissociator method '" + dissociatorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke dissociator method '" + dissociatorMethodName + "'", e);
			}
		}

		public IPrerequisites mutatorPrerequisitesFor(final Object pojo, final Object candidateValue) {
			Method mutatorPre = __extendedSerializer.getReferenceMutatorPreMethod(_eReference);
			if (mutatorPre == null) {
				return Prerequisites.none();
			}
			try {
				return (IPrerequisites) mutatorPre.invoke(pojo, new Object[] { candidateValue });
			} catch (IllegalArgumentException ex) {
				_logger.error("Problem obtaining mutator prerequisites for '" + _eReference + "'", ex);
				return Prerequisites.invisible();
			} catch (IllegalAccessException ex) {
				_logger.error("Problem obtaining mutator prerequisites for '" + _eReference + "'", ex);
				return Prerequisites.invisible();
			} catch (InvocationTargetException ex) {
				_logger.error("Problem obtaining mutator prerequisites for '" + _eReference + "'", ex);
				return Prerequisites.invisible();
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
		public void invokeAddTo(final Object pojo, final Object referencedObject) {
			if (_associator == null) {
				throw new UnsupportedOperationException("No associator method");
			}
			String associatorMethodName = _associator.getName();
			try {
				_associator.invoke(pojo, new Object[]{referencedObject});
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
		public void invokeRemoveFrom(final Object pojo, final Object referencedObject) {
			if (_dissociator == null) {
				throw new UnsupportedOperationException("No dissociator method");
			}
			String dissociatorMethodName = _dissociator.getName();
			try {
				_dissociator.invoke(pojo, new Object[]{referencedObject});
			} catch (SecurityException e) {
				throw new UnsupportedOperationException("Dissociator method '" + dissociatorMethodName + "' not accessible", e);
			} catch (IllegalAccessException e) {
				throw new UnsupportedOperationException("Could not invoke dissociator method '" + dissociatorMethodName + "'", e);
			} catch (InvocationTargetException e) {
				throw new UnsupportedOperationException("Could not invoke dissociator method '" + dissociatorMethodName + "'", e);
			}
		}
		
		public IPrerequisites mutatorPrerequisitesFor(final Object pojo, final Object candidateValue, final boolean beingAdded) {
			Method mutatorPre = beingAdded 
									? __extendedSerializer.getReferenceAddToPreMethod(_eReference)
									: __extendedSerializer.getReferenceRemoveFromPreMethod(_eReference);
			if (mutatorPre == null) {
				return Prerequisites.none();
			}
			try {
				return (IPrerequisites) mutatorPre.invoke(pojo, new Object[] { candidateValue });
			} catch (IllegalArgumentException ex) {
				// TODO log?
			} catch (IllegalAccessException ex) {
				// TODO Auto-generated catch block
			} catch (InvocationTargetException ex) {
				// TODO Auto-generated catch block
			}
			return Prerequisites.none();
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

		/*
		 * Extended semantics support.
		 */
		private final Class<?>[] _parameterTypes;


		public RuntimeOperationBinding(IDomainClass.IOperation operation) {
			_operation = operation;
			_eOperation = _operation.getEOperation();
			
			_operationInvoker = __standardSerializer.getOperationMethod(_eOperation);
			_operationPre = __extendedSerializer.getOperationPreMethod(_eOperation);
			_operationDefaults = __extendedSerializer.getOperationDefaultsMethod(_eOperation);

			// extended semantics
			EList eParameters = _eOperation.getEParameters();
			_parameterTypes = new Class<?>[eParameters.size()];
			for (int i = 0; i < _parameterTypes.length; i++) {
				EParameter eParameter = (EParameter) eParameters.get(i);
				_parameterTypes[i] = eParameter.getEType().getInstanceClass();
			}
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
			return domainBinding.getAuthorizationManager().preconditionsFor(_operation.getFeatureId());
		}

		public IPrerequisites prerequisitesFor(final Object pojo, Object[] args) {

			Method invokePre = __extendedSerializer.getOperationPreMethod(_eOperation);
			if (invokePre == null) {
				return Prerequisites.none();
			}

			try {
				return (IPrerequisites) invokePre.invoke(pojo, args);
			} catch (IllegalArgumentException ex) {
				return Prerequisites.unusable(ex.getLocalizedMessage());
			} catch (IllegalAccessException ex) {
				return Prerequisites.unusable(ex.getLocalizedMessage());
			} catch (InvocationTargetException ex) {
				return Prerequisites.unusable(ex.getLocalizedMessage());
			}
		}

		
		public Object[] reset(final Object pojo, final Object[] args, Map<Integer, Object> argsByPosition) {
			Method invokeDefaults = __extendedSerializer.getOperationDefaultsMethod(_eOperation);
			if (invokeDefaults == null) {
				return args;
			}
			
			try {
				// create an array of 1-element arrays; the defaulter method
				// will expect this structure and will populate the element
				// (simulates passing by reference)
				Object[] argDefaultArrays = new Object[_parameterTypes.length];
				for (int i = 0; i < argDefaultArrays.length; i++) {
					argDefaultArrays[i] = Array.newInstance(_parameterTypes[i], 1);
				}

				// invoke the defaults method itself
				invokeDefaults.invoke(pojo, argDefaultArrays);
				// now copy out the elements into the actual arguments array
				// held by this extendedOp object.
				for (int i = 0; i < argDefaultArrays.length; i++) {
					Object argDefaultArray = argDefaultArrays[i];
					Object argDefault = Array.get(argDefaultArray, 0);
					argsByPosition.put(i, argDefault);
				}

				return getArgs(argsByPosition);
			} catch (IllegalArgumentException ex) {
				return getArgs(argsByPosition);
			} catch (IllegalAccessException ex) {
				return getArgs(argsByPosition);
			} catch (InvocationTargetException ex) {
				return getArgs(argsByPosition);
			}
		}
		
		/*
		 * Extended semantics.
		 */
		public Object[] getArgs(final Map<Integer, Object> argumentsByPosition) {
			Object[] args = new Object[_eOperation.getEParameters().size()];
			for (int i = 0; i < args.length; i++) {
				args[i] = getArg(i, argumentsByPosition);
			}
			return args;
		}



		// helpers

		private Object getArg(final int position, Map<Integer, Object> argsByPosition) {
			Object arg = argsByPosition.get(position);
			if (arg != null) {
				return arg;
			}
			arg = __defaultValueByPrimitiveType.get(_parameterTypes[position]);
			if (arg != null) {
				return arg;
			}
			try {
				Constructor<?> publicConstructor = _parameterTypes[position]
						.getConstructor();
				arg = publicConstructor.newInstance();
				return arg;
			} catch (Exception ex) {
				throw new IllegalArgumentException(
						"No public constructor for '"
								+ _parameterTypes[position].getCanonicalName()
								+ "'" + " (arg position=" + position + ")", ex);
			}
		}

		public boolean isAssignableFromIncludingAutoboxing(Class<?> requiredType, Class<? extends Object> candidateType) {
			if (requiredType == byte.class && candidateType == Byte.class) {
				return true;
			}
			if (requiredType == short.class && candidateType == Short.class) {
				return true;
			}
			if (requiredType == int.class && candidateType == Integer.class) {
				return true;
			}
			if (requiredType == long.class && candidateType == Long.class) {
				return true;
			}
			if (requiredType == char.class && candidateType == Character.class) {
				return true;
			}
			if (requiredType == float.class && candidateType == Float.class) {
				return true;
			}
			if (requiredType == double.class && candidateType == Double.class) {
				return true;
			}
			if (requiredType == boolean.class && candidateType == Boolean.class) {
				return true;
			}
			return requiredType.isAssignableFrom(candidateType);
		}

		public void assertIsValid(int position, Object arg) {

			if (position < 0 || position >= _parameterTypes.length) {
				throw new IllegalArgumentException(
						"Invalid position: 0 <= position < "
								+ _parameterTypes.length);
			}
			if (arg != null) {
				if (!isAssignableFromIncludingAutoboxing(
						_parameterTypes[position], arg.getClass())) {
					throw new IllegalArgumentException(
							"Incompatible argument for position '" + position
									+ "'; formal='"
									+ _parameterTypes[position].getName()
									+ ", actual='" + arg.getClass().getName()
									+ "'");
				}
			}
		}
		
	}



}
