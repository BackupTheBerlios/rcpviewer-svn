package org.essentialplatform.runtime.client.domain.bindings;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EParameter;
import org.essentialplatform.core.deployment.IDomainBinding;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.progmodel.essential.app.Prerequisites;
import org.essentialplatform.runtime.client.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.shared.AbstractRuntimeBinding;
import org.osgi.framework.Bundle;

/**
 * A binding of {@link IDomainClass} for the client environment.
 * 
 * @author Dan Haywood
 */
public final class RuntimeClientBinding extends AbstractRuntimeBinding {
	
	private static Logger _logger = Logger.getLogger(RuntimeClientBinding.class);
	protected Logger getLogger() {
		return _logger;
	}



	//////////////////////////////////////////////////////////////////////

	@Override
	public Bundle getBundle() {
		return Platform.getBundle("org.essentialplatform.domain.runtime");
	}


	/**
	 * Saves the primary builder, and sets up a sequential persistence Id assigner.
	 *
	 * <p>
	 * TODO: at some point, anticipate that the IPersistenceIdAssigner will be 
	 * injected.
	 * 
	 * @throws RuntimeException if a binding has already been set.
	 */
	public RuntimeClientBinding(IDomainBuilder primaryBuilder) {
		super(primaryBuilder);
	}

	@Override
	public IDomainBinding bindingFor(IDomain domain) {
		return new RuntimeClientDomainBinding(domain);
	}
	@Override
	public IDomainClassClientBinding bind(IDomainClass domainClass, Object classRepresentation) {
		IDomainClassClientBinding binding = bind(domainClass, (Class<?>)classRepresentation);
		return binding;
	}
	private <V> IDomainClassClientBinding<V> bind(IDomainClass domainClass, Class<V> javaClass) {
		return new RuntimeClientClassBinding<V>(domainClass, javaClass);
	}
	
	@Override
	public IAttributeClientBinding bindingFor(IDomainClass.IAttribute attribute) {
		return new RuntimeClientAttributeBinding(attribute);
	}
	@Override
	public IOneToOneReferenceClientBinding bindingFor(IDomainClass.IOneToOneReference oneToOneReference) {
		return new RuntimeClientOneToOneReferenceBinding(oneToOneReference);
	}
	@Override
	public ICollectionReferenceClientBinding bindingFor(IDomainClass.ICollectionReference collectionReference) {
		return new RuntimeClientCollectionReferenceBinding(collectionReference);
	}
	@Override
	public IOperationClientBinding bindingFor(IDomainClass.IOperation operation) {
		return new RuntimeClientOperationBinding(operation);
	}

	//////////////////////////////////////////////////////////////////////
	
	public final class RuntimeClientDomainBinding extends AbstractRuntimeDomainBinding implements IDomainBinding {

		
		public RuntimeClientDomainBinding(IDomain domain) {
			super(domain);
		}
		

		/**
		 * Defaults to {@link IAuthorizationManager#NOOP} but can be overridden
		 * by dependency injection if required.
		 */
		private IAuthorizationManager _authorizationManager = IAuthorizationManager.NOOP;
		
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
	
	public final class RuntimeClientClassBinding<T> extends AbstractRuntimeClassBinding<T> implements IDomainClassClientBinding<T> {

		/**
		 * Delegates either to a composite persistence Id assigner or a
		 * sequential persistence Id assigner dependent on the semantics of the
		 * <tt>AssignmentType</tt> of the domain class.
		 * 
		 * @param domainClass
		 * @param javaClass
		 */
		public RuntimeClientClassBinding(IDomainClass domainClass, Class<T> javaClass) {
			super(domainClass, javaClass);
		}

	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeClientAttributeBinding extends AbstractRuntimeAttributeBinding implements IAttributeClientBinding {

		public RuntimeClientAttributeBinding(IDomainClass.IAttribute attribute) {
			super(attribute);
		}
		

		public IPrerequisites authorizationPrerequisites() {
			IDomain domain = _attribute.getDomainClass().getDomain();
			RuntimeClientDomainBinding domainBinding = (RuntimeClientDomainBinding)domain.getBinding();
			return domainBinding.getAuthorizationManager().preconditionsFor(_attribute.getFeatureId());
		}

		public IPrerequisites accessorPrerequisitesFor(final Object pojo)  {
			return invokePrerequisites(getAccessorPre(), pojo, new Object[]{}, "accessor");
		}

		public IPrerequisites mutatorPrerequisitesFor(Object pojo, Object candidateValue) {
			return invokePrerequisites(getMutatorPre(), pojo, new Object[]{candidateValue}, "mutator");
		}
	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class DelegateRuntimeClientReferenceBinding extends AbstractRuntimeReferenceBinding {
		
		public DelegateRuntimeClientReferenceBinding(IDomainClass.IReference reference) {
			super(reference);
		}
		
		public IPrerequisites authorizationPrerequisites() {
			IDomain domain = _reference.getDomainClass().getDomain();
			RuntimeClientDomainBinding domainBinding = (RuntimeClientDomainBinding)domain.getBinding();
			return domainBinding.getAuthorizationManager().preconditionsFor(_reference.getFeatureId());
		}

		public IPrerequisites accessorPrerequisitesFor(final Object pojo) {
			Method accessorPre = __extendedSerializer.getReferenceAccessorPreMethod(_eReference); 
			return invokePrerequisites(accessorPre, pojo, new Object[]{}, "accessor");
		}


	}
	
	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeClientOneToOneReferenceBinding extends AbstractRuntimeOneToOneReferenceBinding implements IOneToOneReferenceClientBinding {

		private final DelegateRuntimeClientReferenceBinding _delegateBinding; 

		public RuntimeClientOneToOneReferenceBinding(IDomainClass.IOneToOneReference oneToOneReference) {
			super(oneToOneReference);
			_delegateBinding = new DelegateRuntimeClientReferenceBinding(oneToOneReference);
		}


		/*
		 * @see org.essentialplatform.runtime.client.IReferenceClientBinding#authorizationPrerequisites()
		 */
		public IPrerequisites authorizationPrerequisites() {
			return _delegateBinding.authorizationPrerequisites();
		}

		/*
		 * @see org.essentialplatform.runtime.client.IReferenceClientBinding#accessorPrerequisitesFor(java.lang.Object)
		 */
		public IPrerequisites accessorPrerequisitesFor(Object pojo) {
			return _delegateBinding.accessorPrerequisitesFor(pojo);
		}
		
		/*
		 * @see org.essentialplatform.core.deployment.Binding.IOneToOneReferenceBinding#mutatorPrerequisitesFor(java.lang.Object, java.lang.Object)
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object pojo, final Object candidateValue) {
			return invokePrerequisites(getMutatorPre(), pojo, new Object[]{}, "mutator");
		}

	}
		
	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeClientCollectionReferenceBinding extends AbstractRuntimeCollectionReferenceBinding implements ICollectionReferenceClientBinding {

		private final DelegateRuntimeClientReferenceBinding _delegateBinding; 

		public RuntimeClientCollectionReferenceBinding(IDomainClass.ICollectionReference collectionReference) {
			super(collectionReference);
			_delegateBinding = new DelegateRuntimeClientReferenceBinding(collectionReference);
		}


		/*
		 * @see org.essentialplatform.runtime.client.IReferenceClientBinding#authorizationPrerequisites()
		 */
		public IPrerequisites authorizationPrerequisites() {
			return _delegateBinding.authorizationPrerequisites();
		}

		/*
		 * @see org.essentialplatform.runtime.client.IReferenceClientBinding#accessorPrerequisitesFor(java.lang.Object)
		 */
		public IPrerequisites accessorPrerequisitesFor(Object pojo) {
			return _delegateBinding.accessorPrerequisitesFor(pojo);
		}

		/*
		 * @see org.essentialplatform.core.deployment.Binding.ICollectionReferenceBinding#mutatorPrerequisitesFor(java.lang.Object, java.lang.Object, boolean)
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object pojo, final Object candidateValue, final boolean beingAdded) {
			Method mutatorPre = beingAdded ? getAddToPre() : getRemoveFromPre();
			return invokePrerequisites(mutatorPre, pojo, new Object[]{candidateValue}, "mutator");
		}

	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeClientOperationBinding extends AbstractRuntimeOperationBinding implements IOperationClientBinding {

		
		/*
		 * Extended semantics support.
		 */
		private final Class<?>[] _parameterTypes;


		public RuntimeClientOperationBinding(IDomainClass.IOperation operation) {
			super(operation);
			
			// extended semantics
			EList eParameters = _eOperation.getEParameters();
			_parameterTypes = new Class<?>[eParameters.size()];
			for (int i = 0; i < _parameterTypes.length; i++) {
				EParameter eParameter = (EParameter) eParameters.get(i);
				_parameterTypes[i] = eParameter.getEType().getInstanceClass();
			}
		}

		/**
		 * Not null.
		 */
		private Method getOperationInvoker() {
			return __standardSerializer.getOperationMethod(_eOperation);
		}
		/**
		 * Derived, may be null.
		 */
		private Method getOperationPre() {
			return __extendedSerializer.getOperationPreMethod(_eOperation);
		}
		/**
		 * Derived, may be null.
		 */
		private Method getOperationDefaults() {
			return __extendedSerializer.getOperationDefaultsMethod(_eOperation);
		}

		public Object invokeOperation(final Object pojo, final Object[] args) {
			return invokeInvoker(getOperationInvoker(), pojo, args, "operation");
		}

		public IPrerequisites authorizationPrerequisites() {
			IDomain domain = _operation.getDomainClass().getDomain();
			RuntimeClientDomainBinding domainBinding = (RuntimeClientDomainBinding)domain.getBinding();
			return domainBinding.getAuthorizationManager().preconditionsFor(_operation.getFeatureId());
		}

		public IPrerequisites prerequisitesFor(final Object pojo, Object[] args) {
			Method invokePre = __extendedSerializer.getOperationPreMethod(_eOperation);
			return invokePrerequisites(invokePre, pojo, args, "operation");
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
	private static Object invokeInvoker(Method method, Object pojo, Object[] args, String contextDescription) {
		if (method==null) {
			String formattedMessage = buildErrorMessage(null, contextDescription, "invoker", method);
			_logger.error(formattedMessage);
			throw new UnsupportedOperationException(formattedMessage);
		}
		try {
			return method.invoke(pojo, args);
		} catch (IllegalArgumentException ex) {
			String formattedMessage = buildErrorMessage(ex, contextDescription, "invoker", method);
			_logger.error(formattedMessage, ex);
			throw ex;
		} catch (IllegalAccessException ex) {
			String formattedMessage = buildErrorMessage(ex, contextDescription, "invoker", method);
			_logger.error(formattedMessage, ex);
			throw new UnsupportedOperationException(formattedMessage);
		} catch (InvocationTargetException ex) {
			String formattedMessage = buildErrorMessage(ex.getTargetException(), contextDescription, "invoker", method);
			_logger.error(formattedMessage, ex);
			throw new UnsupportedOperationException(formattedMessage);
		}
	}

	/**
	 * Invokes method to obtain {@link IPrerequisites} and does necessary error 
	 * handling on failure.
	 * 
	 * <p>
	 * Static so can be invoked from instances of the nested static classes.
	 * 
	 * @param m
	 * @param pojo
	 * @param args
	 * @return the prerequisites applicable.
	 */
	private static IPrerequisites invokePrerequisites(Method method, Object pojo, Object[] args, String contextDescription) {
		if (method==null) {
			return Prerequisites.none();
		}
		try {
			return (IPrerequisites)method.invoke(pojo, args);
		} catch (IllegalArgumentException ex) {
			String formattedMessage = buildErrorMessage(ex, contextDescription, "prerequisites", method);
			_logger.error(formattedMessage, ex);
			throw ex;
		} catch (IllegalAccessException ex) {
			String formattedMessage = buildErrorMessage(ex, contextDescription, "prerequisites", method);
			_logger.error(formattedMessage, ex);
			throw new UnsupportedOperationException(formattedMessage);
		} catch (InvocationTargetException ex) {
			String formattedMessage = buildErrorMessage(ex.getTargetException(), contextDescription, "prerequisites", method);
			_logger.error(formattedMessage, ex);
			throw new UnsupportedOperationException(formattedMessage);
		}
	}

}
