package org.essentialplatform.runtime.shared;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.essentialplatform.core.IBundlePeer;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.deployment.ICollectionReferenceBinding;
import org.essentialplatform.core.deployment.IDomainBinding;
import org.essentialplatform.core.deployment.IOneToOneReferenceBinding;
import org.essentialplatform.core.deployment.IOperationBinding;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.progmodel.ProgrammingModelException;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelExtendedSemanticsEmfSerializer;
import org.essentialplatform.progmodel.essential.core.emf.EssentialProgModelStandardSemanticsEmfSerializer;
import org.essentialplatform.progmodel.louis.core.emf.LouisProgModelSemanticsEmfSerializer;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.domain.bindings.IAttributeRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.osgi.framework.Bundle;

/**
 * A binding of {@link IDomainClass} for the runtime environment.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractRuntimeBinding extends Binding implements IRuntimeBinding {
	
	private static Logger _logger = Logger.getLogger(AbstractRuntimeBinding.class);
	protected Logger getLogger() {
		return _logger;
	}

	protected EssentialProgModelStandardSemanticsEmfSerializer __standardSerializer = new EssentialProgModelStandardSemanticsEmfSerializer();
	protected EssentialProgModelExtendedSemanticsEmfSerializer __extendedSerializer = new EssentialProgModelExtendedSemanticsEmfSerializer();
	protected LouisProgModelSemanticsEmfSerializer __louisSerializer = new LouisProgModelSemanticsEmfSerializer();

	protected static Map<Class<?>, Object> __defaultValueByPrimitiveType = new HashMap<Class<?>, Object>();
	protected static Map<Class<?>, Class<?>> __wrapperTypeByPrimitiveType = new HashMap<Class<?>, Class<?>>();

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
	protected Class<?> wrapperTypeIfRequired(Class<?> type) {
		Class<?> wrappedType = __wrapperTypeByPrimitiveType.get(type);
		if (wrappedType != null) {
			return wrappedType;
		} else {
			return type;
		}

	}
	
	
	//////////////////////////////////////////////////////////////////////
	// Bundle
	//////////////////////////////////////////////////////////////////////

	private Bundle _bundle;
	/*
	 * @see org.essentialplatform.runtime.shared.IRuntimeBinding#init(org.osgi.framework.Bundle)
	 */
	public void init(IBundlePeer bundlePeer) {
		_bundle = bundlePeer.getBundle();
	}

	/*
	 * @see org.essentialplatform.core.deployment.IBinding#getBundle()
	 */
	public Bundle getBundle() {
		return _bundle;
	}


	//////////////////////////////////////////////////////////////////////
	// getInDomainOf
	//////////////////////////////////////////////////////////////////////
	

	/*
	 * The <tt>classRepresentation</tt> can be either a string or an actual
	 * class.
	 * 
	 * @see org.essentialplatform.core.deployment.Binding#getInDomainOf(java.lang.Object)
	 */
	/*
	 * @see org.essentialplatform.runtime.shared.IRuntmieBinding#getInDomainOf(java.lang.Object)
	 */
	@Override
	public final InDomain getInDomainOf(final Object classRepresentation) {
		
		if (!(classRepresentation instanceof Class)) {
			throw new IllegalArgumentException("Class representation not recognized (classRepresentation = '" + classRepresentation + "'");
		}
		Class<?> javaClass = (Class<?>)classRepresentation;
		return getInDomainOf(javaClass);
	}
	private <V> InDomain getInDomainOf(final Class<V> javaClass) {
		InDomain inDomain = javaClass.getAnnotation(InDomain.class);
		if (inDomain == null) {
			return null;
		}
		return inDomain;
	}

	//////////////////////////////////////////////////////////////////////

	/*
	 * @see org.essentialplatform.runtime.shared.IRuntmieBinding#assertValid(java.lang.Object)
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


	//////////////////////////////////////////////////////////////////////
	
	public abstract class AbstractRuntimeDomainBinding implements IDomainBinding {

		
		public AbstractRuntimeDomainBinding(IDomain domain) {
			_domain = domain;
		}
		
		private final IDomain _domain;
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


//		/**
//		 * Defaults to {@link IAuthorizationManager#NOOP} but can be overridden
//		 * by dependency injection if required.
//		 */
//		private IAuthorizationManager _authorizationManager = IAuthorizationManager.NOOP;
//		/**
//		 * Authorization manager to enforce constraints.
//		 * 
//		 * <p> 
//		 * Defaults to {@link IAuthorizationManager#NOOP} but can be overridden
//		 * using dependency injection, see {@link #setAuthorizationManager(IAuthorizationManager)}.
//		 * 
//		 * @see org.essentialplatform.domain.runtime.IRuntimeDomain#getAuthorizationManager()
//		 */
//		public IAuthorizationManager getAuthorizationManager() {
//			return _authorizationManager;
//		}
//		/**
//		 * Set the authorization manager for this binding of the domain to a
//		 * runtime deployment.
//		 * 
//		 * @param authorizationManager
//		 */
//		public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
//			if (authorizationManager == null) {
//				throw new IllegalArgumentException("IAuthorizationManager may not be null - consider IAuthorizationManager.NOOP instead");
//			}
//			_authorizationManager = authorizationManager;
//		}

	}
	
	public abstract static class AbstractRuntimeClassBinding<T> implements IDomainClassRuntimeBinding<T> {

		private final IDomainClass _domainClass;
		private final Class<T> _javaClass;
		
		/**
		 * Delegates either to a composite handle assigner or a
		 * sequential handle assigner dependent on the semantics of the
		 * <tt>AssignmentType</tt> of the domain class.
		 * 
		 * @param domainClass
		 * @param javaClass
		 */
		public AbstractRuntimeClassBinding(IDomainClass domainClass, Class<T> javaClass) {
			_domainClass = domainClass;
			_javaClass = javaClass;
			domainClass.setBinding(this);
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding#getJavaClass()
		 */
		public Class<T> getJavaClass() {
			return _javaClass;
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding#newInstance(org.essentialplatform.runtime.shared.session.SessionBinding, org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState, org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState)
		 */
		public T newInstance(final SessionBinding sessionBinding, PersistState persistState, ResolveState resolveState) throws ProgrammingModelException {
			try {
				IPojo pojo = (IPojo)getJavaClass().newInstance();
				IDomainObject<T> domainObject = pojo.domainObject();
				domainObject.init(sessionBinding, persistState, resolveState, this);
				return (T)pojo;
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

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public abstract class AbstractRuntimeAttributeBinding implements IAttributeRuntimeBinding {

		protected final IDomainClass.IAttribute _attribute;
		protected final EAttribute _eAttribute;
		
		public AbstractRuntimeAttributeBinding(IDomainClass.IAttribute attribute) {
			_attribute = attribute;
			_eAttribute = _attribute.getEAttribute();
		}
		
		protected Method getAccessor() {
			return __standardSerializer.getAttributeAccessorMethod(_eAttribute);
		}
		protected Method getMutator() {
			return __standardSerializer.getAttributeMutatorMethod(_eAttribute);
		}
		protected Method getAccessorOrMutator() {
			return getAccessor()!=null?getAccessor():getMutator();
		}
		protected Method getAccessorPre() {
			return __extendedSerializer.getAttributeAccessorPreMethod(_eAttribute);
		}
		protected Method getMutatorPre() {
			return __extendedSerializer.getAttributeMutatorPreMethod(_eAttribute);
		}

		public Object invokeAccessor(final Object pojo) {
			return invokeInvoker(getAccessor(), pojo, new Object[]{}, "accessor");
		}

		/**
		 * There is no need to call to notifyAttributeListeners(newValue);
		 * the NotifyListeners aspect will do our bidding for us.
		 */
		public void invokeMutator(final Object pojo, final Object newValue) {
			invokeInvoker(getMutator(), pojo, new Object[]{newValue}, "mutator");
		}
		
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			return getAccessorOrMutator().getAnnotation(annotationClass);
		}

//		public IPrerequisites authorizationPrerequisites() {
//			IDomain domain = _attribute.getDomainClass().getDomain();
//			RuntimeClientDomainBinding domainBinding = (RuntimeClientDomainBinding)domain.getBinding();
//			return domainBinding.getAuthorizationManager().preconditionsFor(_attribute.getFeatureId());
//		}
//
//		public IPrerequisites accessorPrerequisitesFor(final Object pojo)  {
//			return invokePrerequisites(getAccessorPre(), pojo, new Object[]{}, "accessor");
//		}
//
//		public IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue) {
//			return invokePrerequisites(getMutatorPre(), pojo, new Object[]{candidateValue}, "mutator");
//		}
	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public abstract class AbstractRuntimeReferenceBinding {
		
		protected final IDomainClass.IReference _reference;
		protected final EReference _eReference;

		public AbstractRuntimeReferenceBinding(IDomainClass.IReference reference) {
			_reference = reference;
			_eReference = reference.getEReference();
		}
		
		protected Method getAccessor() {
			return __standardSerializer.getReferenceAccessor(_eReference);
		}
		protected Method getMutator() {
			return __standardSerializer.getReferenceMutator(_eReference);
		}
		
		public Object invokeAccessor(final Object pojo) {
			return invokeInvoker(getAccessor(), pojo, new Object[]{}, "accessor");
		}

//		public IPrerequisites authorizationPrerequisites() {
//			IDomain domain = _reference.getDomainClass().getDomain();
//			RuntimeClientDomainBinding domainBinding = (RuntimeClientDomainBinding)domain.getBinding();
//			return domainBinding.getAuthorizationManager().preconditionsFor(_reference.getFeatureId());
//		}
//
//		public IPrerequisites accessorPrerequisitesFor(final Object pojo) {
//			Method accessorPre = __extendedSerializer.getReferenceAccessorPreMethod(_eReference); 
//			return invokePrerequisites(accessorPre, pojo, new Object[]{}, "accessor");
//		}


	}
	
	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public abstract class AbstractRuntimeOneToOneReferenceBinding extends AbstractRuntimeReferenceBinding implements IOneToOneReferenceBinding {

		protected final IDomainClass.IOneToOneReference _oneToOneReference;

		public AbstractRuntimeOneToOneReferenceBinding(IDomainClass.IOneToOneReference oneToOneReference) {
			super(oneToOneReference);
			_oneToOneReference = oneToOneReference; // downcast
		}

		protected Method getAssociator() {
			return __standardSerializer.getReferenceOneToOneAssociator(_eReference);
		}
		protected Method getDissociator() {
			return __standardSerializer.getReferenceOneToOneDissociator(_eReference);
		}
		protected Method getMutatorPre() {
			return __extendedSerializer.getReferenceMutatorPreMethod(_eReference);
		}

		/*
		 * @see org.essentialplatform.core.deployment.Binding.IReferenceBinding#canAssociate()
		 */
		public boolean canAssociate() {
			return getAssociator() != null;
		}
		/*
		 * @see org.essentialplatform.core.deployment.Binding.IReferenceBinding#invokeAssociator(java.lang.Object, java.lang.Object)
		 */
		public void invokeAssociator(final Object pojo, final Object referencedObject) {
			invokeInvoker(getAssociator(), pojo, new Object[]{referencedObject}, "associator");
		}
		/*
		 * @see org.essentialplatform.core.deployment.Binding.IReferenceBinding#canDissociate()
		 */
		public boolean canDissociate() {
			return getDissociator() != null;
		}
		/*
		 * @see org.essentialplatform.core.deployment.Binding.IReferenceBinding#invokeDissociator(java.lang.Object, java.lang.Object)
		 */
		public void invokeDissociator(final Object pojo, final Object referencedObject) {
			invokeInvoker(getDissociator(), pojo, new Object[]{referencedObject}, "dissociator");
		}

//		/*
//		 * @see org.essentialplatform.core.deployment.Binding.IOneToOneReferenceBinding#mutatorPrerequisitesFor(java.lang.Object, java.lang.Object)
//		 */
//		public IPrerequisites mutatorPrerequisitesFor(final Object pojo, final Object candidateValue) {
//			return invokePrerequisites(getMutatorPre(), pojo, new Object[]{}, "mutator");
//		}
		
	}
		
	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public abstract class AbstractRuntimeCollectionReferenceBinding extends AbstractRuntimeReferenceBinding implements ICollectionReferenceBinding {

		protected final IDomainClass.ICollectionReference _collectionReference;

		public AbstractRuntimeCollectionReferenceBinding(IDomainClass.ICollectionReference collectionReference) {
			super(collectionReference);
			_collectionReference = collectionReference; // downcast
		}

		protected Method getAssociator() {
			return __standardSerializer.getReferenceCollectionAssociator(_eReference);
		}
		protected Method getDissociator() {
			return __standardSerializer.getReferenceCollectionDissociator(_eReference);
		}

		protected Method getAddToPre() {
			return __extendedSerializer.getReferenceAddToPreMethod(_eReference);
		}
		protected Method getRemoveFromPre() {
			return __extendedSerializer.getReferenceRemoveFromPreMethod(_eReference);
		}

		/*
		 * @see org.essentialplatform.core.deployment.Binding.IReferenceBinding#canAssociate()
		 */
		public boolean canAssociate() {
			return getAssociator() != null;
		}
		/*
		 * @see org.essentialplatform.core.deployment.Binding.IReferenceBinding#invokeAssociator(java.lang.Object, java.lang.Object)
		 */
		public void invokeAssociator(final Object pojo, final Object referencedObject) {
			invokeInvoker(getAssociator(), pojo, new Object[]{referencedObject}, "associator");
		}
		/*
		 * @see org.essentialplatform.core.deployment.Binding.IReferenceBinding#canDissociate()
		 */
		public boolean canDissociate() {
			return getDissociator() != null;
		}
		/*
		 * @see org.essentialplatform.core.deployment.Binding.IReferenceBinding#invokeDissociator(java.lang.Object, java.lang.Object)
		 */
		public void invokeDissociator(final Object pojo, final Object referencedObject) {
			invokeInvoker(getDissociator(), pojo, new Object[]{referencedObject}, "dissociator");
		}

//		/*
//		 * @see org.essentialplatform.core.deployment.Binding.ICollectionReferenceBinding#mutatorPrerequisitesFor(java.lang.Object, java.lang.Object, boolean)
//		 */
//		public IPrerequisites mutatorPrerequisitesFor(final Object pojo, final Object candidateValue, final boolean beingAdded) {
//			Method mutatorPre = beingAdded ? getAddToPre() : getRemoveFromPre();
//			return invokePrerequisites(mutatorPre, pojo, new Object[]{candidateValue}, "mutator");
//		}

	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public abstract class AbstractRuntimeOperationBinding implements IOperationBinding {

		protected final IDomainClass.IOperation _operation;
		protected final EOperation _eOperation;
		
//		/*
//		 * Extended semantics support.
//		 */
//		private final Class<?>[] _parameterTypes;


		public AbstractRuntimeOperationBinding(IDomainClass.IOperation operation) {
			_operation = operation;
			_eOperation = _operation.getEOperation();
			
//			// extended semantics
//			EList eParameters = _eOperation.getEParameters();
//			_parameterTypes = new Class<?>[eParameters.size()];
//			for (int i = 0; i < _parameterTypes.length; i++) {
//				EParameter eParameter = (EParameter) eParameters.get(i);
//				_parameterTypes[i] = eParameter.getEType().getInstanceClass();
//			}
		}

//		/**
//		 * Not null.
//		 */
//		private Method getOperationInvoker() {
//			return __standardSerializer.getOperationMethod(_eOperation);
//		}
//		/**
//		 * Derived, may be null.
//		 */
//		private Method getOperationPre() {
//			return __extendedSerializer.getOperationPreMethod(_eOperation);
//		}
//		/**
//		 * Derived, may be null.
//		 */
//		private Method getOperationDefaults() {
//			return __extendedSerializer.getOperationDefaultsMethod(_eOperation);
//		}
//
//		public Object invokeOperation(final Object pojo, final Object[] args) {
//			return invokeInvoker(getOperationInvoker(), pojo, args, "operation");
//		}
//
//		public IPrerequisites authorizationPrerequisites() {
//			IDomain domain = _operation.getDomainClass().getDomain();
//			RuntimeClientDomainBinding domainBinding = (RuntimeClientDomainBinding)domain.getBinding();
//			return domainBinding.getAuthorizationManager().preconditionsFor(_operation.getFeatureId());
//		}
//
//		public IPrerequisites prerequisitesFor(final Object pojo, Object[] args) {
//			Method invokePre = __extendedSerializer.getOperationPreMethod(_eOperation);
//			return invokePrerequisites(invokePre, pojo, args, "operation");
//		}
//		
//		public Object[] reset(final Object pojo, final Object[] args, Map<Integer, Object> argsByPosition) {
//			Method invokeDefaults = __extendedSerializer.getOperationDefaultsMethod(_eOperation);
//			if (invokeDefaults == null) {
//				return args;
//			}
//			
//			try {
//				// create an array of 1-element arrays; the defaulter method
//				// will expect this structure and will populate the element
//				// (simulates passing by reference)
//				Object[] argDefaultArrays = new Object[_parameterTypes.length];
//				for (int i = 0; i < argDefaultArrays.length; i++) {
//					argDefaultArrays[i] = Array.newInstance(_parameterTypes[i], 1);
//				}
//
//				// invoke the defaults method itself
//				invokeDefaults.invoke(pojo, argDefaultArrays);
//				// now copy out the elements into the actual arguments array
//				// held by this extendedOp object.
//				for (int i = 0; i < argDefaultArrays.length; i++) {
//					Object argDefaultArray = argDefaultArrays[i];
//					Object argDefault = Array.get(argDefaultArray, 0);
//					argsByPosition.put(i, argDefault);
//				}
//
//				return getArgs(argsByPosition);
//			} catch (IllegalArgumentException ex) {
//				return getArgs(argsByPosition);
//			} catch (IllegalAccessException ex) {
//				return getArgs(argsByPosition);
//			} catch (InvocationTargetException ex) {
//				return getArgs(argsByPosition);
//			}
//		}
//		
//		/*
//		 * Extended semantics.
//		 */
//		public Object[] getArgs(final Map<Integer, Object> argumentsByPosition) {
//			Object[] args = new Object[_eOperation.getEParameters().size()];
//			for (int i = 0; i < args.length; i++) {
//				args[i] = getArg(i, argumentsByPosition);
//			}
//			return args;
//		}
//
//
//
//		// helpers
//
//		private Object getArg(final int position, Map<Integer, Object> argsByPosition) {
//			Object arg = argsByPosition.get(position);
//			if (arg != null) {
//				return arg;
//			}
//			arg = __defaultValueByPrimitiveType.get(_parameterTypes[position]);
//			if (arg != null) {
//				return arg;
//			}
//			try {
//				Constructor<?> publicConstructor = _parameterTypes[position]
//						.getConstructor();
//				arg = publicConstructor.newInstance();
//				return arg;
//			} catch (Exception ex) {
//				throw new IllegalArgumentException(
//						"No public constructor for '"
//								+ _parameterTypes[position].getCanonicalName()
//								+ "'" + " (arg position=" + position + ")", ex);
//			}
//		}
//
//		public boolean isAssignableFromIncludingAutoboxing(Class<?> requiredType, Class<? extends Object> candidateType) {
//			if (requiredType == byte.class && candidateType == Byte.class) {
//				return true;
//			}
//			if (requiredType == short.class && candidateType == Short.class) {
//				return true;
//			}
//			if (requiredType == int.class && candidateType == Integer.class) {
//				return true;
//			}
//			if (requiredType == long.class && candidateType == Long.class) {
//				return true;
//			}
//			if (requiredType == char.class && candidateType == Character.class) {
//				return true;
//			}
//			if (requiredType == float.class && candidateType == Float.class) {
//				return true;
//			}
//			if (requiredType == double.class && candidateType == Double.class) {
//				return true;
//			}
//			if (requiredType == boolean.class && candidateType == Boolean.class) {
//				return true;
//			}
//			return requiredType.isAssignableFrom(candidateType);
//		}
//
//		public void assertIsValid(int position, Object arg) {
//
//			if (position < 0 || position >= _parameterTypes.length) {
//				throw new IllegalArgumentException(
//						"Invalid position: 0 <= position < "
//								+ _parameterTypes.length);
//			}
//			if (arg != null) {
//				if (!isAssignableFromIncludingAutoboxing(
//						_parameterTypes[position], arg.getClass())) {
//					throw new IllegalArgumentException(
//							"Incompatible argument for position '" + position
//									+ "'; formal='"
//									+ _parameterTypes[position].getName()
//									+ ", actual='" + arg.getClass().getName()
//									+ "'");
//				}
//			}
//		}
		
	}

	/**
	 * Invokes accessor, mutator etc (so-called "invoker" method to distinguish
	 * from "prerequisites" method).
	 * 
	 * @param method
	 * @param pojo
	 * @param args
	 * @param contextDescription
	 * @return
	 */
	private Object invokeInvoker(Method method, Object pojo, Object[] args, String contextDescription) {
		if (method==null) {
			String formattedMessage = buildErrorMessage(null, contextDescription, "invoker", method);
			getLogger().error(formattedMessage);
			throw new UnsupportedOperationException(formattedMessage);
		}
		try {
			return method.invoke(pojo, args);
		} catch (IllegalArgumentException ex) {
			String formattedMessage = buildErrorMessage(ex, contextDescription, "invoker", method);
			getLogger().error(formattedMessage, ex);
			throw ex;
		} catch (IllegalAccessException ex) {
			String formattedMessage = buildErrorMessage(ex, contextDescription, "invoker", method);
			getLogger().error(formattedMessage, ex);
			throw new UnsupportedOperationException(formattedMessage);
		} catch (InvocationTargetException ex) {
			String formattedMessage = buildErrorMessage(ex.getTargetException(), contextDescription, "invoker", method);
			getLogger().error(formattedMessage, ex);
			throw new UnsupportedOperationException(formattedMessage);
		}
	}

//	/**
//	 * Invokes method to obtain {@link IPrerequisites} and does necessary error 
//	 * handling on failure.
//	 * 
//	 * <p>
//	 * Static so can be invoked from instances of the nested static classes.
//	 * 
//	 * @param m
//	 * @param pojo
//	 * @param args
//	 * @return the prerequisites applicable.
//	 */
//	private static IPrerequisites invokePrerequisites(Method method, Object pojo, Object[] args, String contextDescription) {
//		if (method==null) {
//			return Prerequisites.none();
//		}
//		try {
//			return (IPrerequisites)method.invoke(pojo, args);
//		} catch (IllegalArgumentException ex) {
//			String formattedMessage = buildErrorMessage(ex, contextDescription, "prerequisites", method);
//			_logger.error(formattedMessage, ex);
//			throw ex;
//		} catch (IllegalAccessException ex) {
//			String formattedMessage = buildErrorMessage(ex, contextDescription, "prerequisites", method);
//			_logger.error(formattedMessage, ex);
//			throw new UnsupportedOperationException(formattedMessage);
//		} catch (InvocationTargetException ex) {
//			String formattedMessage = buildErrorMessage(ex.getTargetException(), contextDescription, "prerequisites", method);
//			_logger.error(formattedMessage, ex);
//			throw new UnsupportedOperationException(formattedMessage);
//		}
//	}
	/**
	 * Static so can be invoked from instances of the nested static classes.
	 * 
	 * @param ex
	 * @param contextDescription1
	 * @param contextDescription2
	 * @param method
	 * @return
	 */
	protected static String buildErrorMessage(
			Throwable ex, 
			String contextDescription1, String contextDescription2, 
			Method method) {
		return String.format(
				"Problem invoking %s %s method '%s' (ex=%s)", 
				new Object[]{contextDescription1, contextDescription2, method.getName(), ex.getMessage()});
	}






	//////////////////////////////////////////////////////////////////////
	// PrimaryBuilder (injected)
	//////////////////////////////////////////////////////////////////////

	private IDomainBuilder _primaryBuilder;
	/*
	 * @see org.essentialplatform.runtime.shared.IRuntmieBinding#getPrimaryBuilder()
	 */
	@Override
	public IDomainBuilder getPrimaryBuilder() {
		return _primaryBuilder;
	}

	/**
	 * For dependency injection.
	 */
	public void setPrimaryBuilder(IDomainBuilder primaryBuilder) {
		_primaryBuilder = primaryBuilder;
	}
	
	/**
	 * For programmatic configuration.
	 */
	public IRuntimeBinding initPrimaryBuilder(IDomainBuilder primaryBuilder) {
		setPrimaryBuilder(primaryBuilder);
		return this;
	}
	
	
}
