package de.berlios.rcpviewer.progmodel.extended;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.AbstractDomainObjectAdapter;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedAttribute;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedOperation;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedReference;
import de.berlios.rcpviewer.progmodel.standard.DomainObject;
import de.berlios.rcpviewer.session.DomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.ExtendedDomainObjectAttributeEvent;
import de.berlios.rcpviewer.session.ExtendedDomainObjectOperationEvent;
import de.berlios.rcpviewer.session.ExtendedDomainObjectReferenceEvent;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.IDomainObjectAttributeListener;
import de.berlios.rcpviewer.session.IExtendedDomainObjectAttributeListener;
import de.berlios.rcpviewer.session.IExtendedDomainObjectOperationListener;
import de.berlios.rcpviewer.session.IExtendedDomainObjectReferenceListener;
import de.berlios.rcpviewer.session.IDomainObject.IAttribute;
import de.berlios.rcpviewer.session.IDomainObject.IOperation;
import de.berlios.rcpviewer.session.IDomainObject.IReference;
import de.berlios.rcpviewer.authorization.IAuthorizationManager;

public class ExtendedDomainObject<T> extends AbstractDomainObjectAdapter<T> implements IExtendedDomainObject<T> {

	private WeakHashMap<EAttribute, IExtendedAttribute> _attributesByEAttribute = new WeakHashMap<EAttribute, IExtendedAttribute>();
	private WeakHashMap<EReference, IExtendedReference> _referencesByEReference = new WeakHashMap<EReference, IExtendedReference>();
	private WeakHashMap<EOperation, IExtendedOperation> _operationsByEOperation = new WeakHashMap<EOperation, IExtendedOperation>();

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
		__defaultValueByPrimitiveType.put(String.class, null); // rather than ""
		__defaultValueByPrimitiveType.put(Byte.class, 0);
		__defaultValueByPrimitiveType.put(Short.class, 0);
		__defaultValueByPrimitiveType.put(Integer.class, 0);
		__defaultValueByPrimitiveType.put(Long.class, 0);
		__defaultValueByPrimitiveType.put(Character.class, 0);
		__defaultValueByPrimitiveType.put(Float.class, 0);
		__defaultValueByPrimitiveType.put(Double.class, 0);
		__defaultValueByPrimitiveType.put(Boolean.class, false);
		__defaultValueByPrimitiveType.put(BigInteger.class, new BigInteger("0"));
		__defaultValueByPrimitiveType.put(BigDecimal.class, new BigDecimal("0.0"));
		
		__wrapperTypeByPrimitiveType.put(byte.class, Byte.class);
		__wrapperTypeByPrimitiveType.put(short.class, Short.class);
		__wrapperTypeByPrimitiveType.put(int.class, Integer.class);
		__wrapperTypeByPrimitiveType.put(long.class, Long.class);
		__wrapperTypeByPrimitiveType.put(char.class, Character.class);
		__wrapperTypeByPrimitiveType.put(float.class, Float.class);
		__wrapperTypeByPrimitiveType.put(double.class, Double.class);
		__wrapperTypeByPrimitiveType.put(boolean.class, Boolean.class);

	}

	public ExtendedDomainObject(IDomainObject<T> domainObject) {
		super(domainObject);
	}

	/*
	 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject#getExtendedRuntimeDomainClass()
	 */
	public IExtendedRuntimeDomainClass<T> getExtendedRuntimeDomainClass() {
		IRuntimeDomainClass<T> domainClass = adapts().getDomainClass(); 
		return (IExtendedRuntimeDomainClass<T>)domainClass.getAdapter(IExtendedRuntimeDomainClass.class); 
	}

	public synchronized IExtendedAttribute getAttribute(EAttribute eAttribute) {
		IExtendedAttribute attribute = _attributesByEAttribute.get(eAttribute);
		if (attribute == null) {
			attribute = new ExtendedAttribute(eAttribute);
			_attributesByEAttribute.put(eAttribute, attribute);
		}
		return attribute;
	}

	public synchronized IExtendedReference getReference(EReference eReference) {
		IExtendedReference reference = _referencesByEReference.get(eReference);
		if (reference == null) {
			reference = new ExtendedReference(eReference);
			_referencesByEReference.put(eReference, reference);
		}
		return reference;
	}

	public synchronized IExtendedOperation getOperation(EOperation eOperation) {
		IExtendedOperation operation = _operationsByEOperation.get(eOperation);
		if (operation == null) {
			operation = new ExtendedOperation(eOperation);
			_operationsByEOperation.put(eOperation, operation);
		}
		return operation;
	}

	public class ExtendedAttribute implements IExtendedDomainObject.IExtendedAttribute {
		
		private final EAttribute _eAttribute;
		private final List<IExtendedDomainObjectAttributeListener> _domainObjectAttributeListeners = 
			new ArrayList<IExtendedDomainObjectAttributeListener>();

		/**
		 * Do not instantiate directly, instead use {@link IExtendedDomainObject#getAttribute(EAttribute)}
		 * 
		 * @param eAttribute
		 */
		private ExtendedAttribute(final EAttribute eAttribute) {
			this._eAttribute = eAttribute;
		}

		public <T> IExtendedDomainObject<T> getExtendedDomainObject() {
			return (IExtendedDomainObject)ExtendedDomainObject.this; // JAVA_5_FIXME
		}

		public EAttribute getEAttribute() {
			return _eAttribute;
		}


		/*
		 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedAttribute#accessorPrerequisitesFor()
		 */
		public IPrerequisites accessorPrerequisitesFor()  {
			IExtendedRuntimeDomainClass<T> erdc = getExtendedRuntimeDomainClass();
			
			Method accessorPre = erdc.getAccessorPre(_eAttribute);
			if (accessorPre == null) {
				return Prerequisites.none();
			}
			
			try {
				return (IPrerequisites)accessorPre.invoke(adapts().getPojo(), new Object[]{});
			} catch (IllegalArgumentException ex) {
				// TODO log?
			} catch (IllegalAccessException ex) {
				// TODO Auto-generated catch block
			} catch (InvocationTargetException ex) {
				// TODO Auto-generated catch block
			}
			return Prerequisites.none();
		}

		/*
		 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject#mutatorPrerequisitesFor(org.eclipse.emf.ecore.EAttribute, java.lang.Object)
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue)  {
			IExtendedRuntimeDomainClass<T> erdc = getExtendedRuntimeDomainClass();
			
			Method mutatorPre = erdc.getMutatorPre(_eAttribute);
			if (mutatorPre == null) {
				return Prerequisites.none();
			}
			
			try {
				return (IPrerequisites)mutatorPre.invoke(adapts().getPojo(), new Object[]{candidateValue});
			} catch (IllegalArgumentException ex) {
				// TODO log?
			} catch (IllegalAccessException ex) {
				// TODO Auto-generated catch block
			} catch (InvocationTargetException ex) {
				// TODO Auto-generated catch block
			}
			return Prerequisites.none();
		}


		/*
		 * 
		 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedAttribute#prerequisitesFor(java.lang.Object)
		 */
		public IPrerequisites prerequisitesFor(final Object candidateValue)  {
			return          accessorPrerequisitesFor()
			    .andRequire(authorizationPrerequisitesFor())
				.andRequire(mutatorPrerequisitesFor(candidateValue));
		}
		

		/*
		 * 
		 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedFeature#authorizationPrerequisitesFor()
		 */
		public IPrerequisites authorizationPrerequisitesFor()  {
			IRuntimeDomainClass<T> rdc = adapts().getDomainClass();
			return rdc.authorizationConstraintFor(_eAttribute);
		}

		/**
		 * Returns listener only because it simplifies test implementation to do so.
		 */
		public <T extends IExtendedDomainObjectAttributeListener> T addExtendedDomainObjectAttributeListener(T listener) {
			synchronized(_domainObjectAttributeListeners) {
				if (!_domainObjectAttributeListeners.contains(listener)) {
					_domainObjectAttributeListeners.add(listener);
				}
			}
			return listener;
		}
		public void removeExtendedDomainObjectAttributeListener(IExtendedDomainObjectAttributeListener listener) {
			synchronized(_domainObjectAttributeListeners) {
				_domainObjectAttributeListeners.remove(listener);
			}
		}
		
		/**
		 * public so that it can be invoked by NotifyListenersAspect.
		 * 
		 * @param attribute
		 * @param newPrerequisites
		 */
		public void notifyAttributeListeners(IPrerequisites newPrerequisites) {
			ExtendedDomainObjectAttributeEvent event = 
				new ExtendedDomainObjectAttributeEvent(this, newPrerequisites);
			for(IExtendedDomainObjectAttributeListener listener: _domainObjectAttributeListeners) {
				listener.attributePrerequisitesChanged(event);
			}
		}

	}

	public class ExtendedReference implements IExtendedDomainObject.IExtendedReference {

		private final EReference _eReference;
		private final List<IExtendedDomainObjectReferenceListener> _domainObjectReferenceListeners = 
			new ArrayList<IExtendedDomainObjectReferenceListener>();

		
		/**
		 * Do not instantiate directly, instead use {@link IExtendedDomainObject#getAttribute(EReference)}
		 * 
		 * @param eAttribute
		 */
		private ExtendedReference(final EReference eReference) {
			this._eReference = eReference;
		}

		/*
		 * 
		 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedFeature#authorizationPrerequisitesFor()
		 */
		public IPrerequisites authorizationPrerequisitesFor()  {
			IRuntimeDomainClass<T> rdc = adapts().getDomainClass();
			return rdc.authorizationConstraintFor(_eReference);
		}

		
		public EReference getEReference() {
			return _eReference;
		}

		public IPrerequisites accessorPrerequisitesFor() {
			IExtendedRuntimeDomainClass<T> erdc = getExtendedRuntimeDomainClass();
			
			Method accessorPre = erdc.getAccessorPre(_eReference);
			if (accessorPre == null) {
				return Prerequisites.none();
			}
			
			try {
				return (IPrerequisites)accessorPre.invoke(adapts().getPojo(), new Object[]{});
			} catch (IllegalArgumentException ex) {
				// TODO log?
			} catch (IllegalAccessException ex) {
				// TODO Auto-generated catch block
			} catch (InvocationTargetException ex) {
				// TODO Auto-generated catch block
			}
			return Prerequisites.none();
		}

		/*
		 * 
		 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedReference#mutatorPrerequisitesFor(java.lang.Object)
		 */
		public IPrerequisites mutatorPrerequisitesFor(Object candidateValue) {
			assert !_eReference.isMany(): "must be a single-valued (simple) reference";
				
			IExtendedRuntimeDomainClass<T> erdc = getExtendedRuntimeDomainClass();
			Method mutatorPre = erdc.getMutatorPre(_eReference);
			if (mutatorPre == null) {
				return Prerequisites.none();
			}
			
			try {
				return (IPrerequisites)mutatorPre.invoke(adapts().getPojo(), new Object[]{candidateValue});
			} catch (IllegalArgumentException ex) {
				// TODO log?
			} catch (IllegalAccessException ex) {
				// TODO Auto-generated catch block
			} catch (InvocationTargetException ex) {
				// TODO Auto-generated catch block
			}
			return Prerequisites.none();
		}

		/*
		 * 
		 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedReference#mutatorPrerequisitesFor(java.lang.Object, boolean)
		 */
		public IPrerequisites mutatorPrerequisitesFor(Object candidateValue, boolean beingAdded) {
			assert _eReference.isMany(): "must be a multi-valued references (collections)";
			
			IExtendedRuntimeDomainClass<T> erdc = getExtendedRuntimeDomainClass();
			Method mutatorPre = beingAdded
									?erdc.getAddToPre(_eReference)
									:erdc.getRemoveFromPre(_eReference);
			if (mutatorPre == null) {
				return Prerequisites.none();
			}
			
			try {
				return (IPrerequisites)mutatorPre.invoke(adapts().getPojo(), new Object[]{candidateValue});
			} catch (IllegalArgumentException ex) {
				// TODO log?
			} catch (IllegalAccessException ex) {
				// TODO Auto-generated catch block
			} catch (InvocationTargetException ex) {
				// TODO Auto-generated catch block
			}
			return Prerequisites.none();

		}

		/**
		 * Returns listener only because it simplifies test implementation to do so.
		 */
		public <T extends IExtendedDomainObjectReferenceListener> T addExtendedDomainObjectReferenceListener(T listener) {
			synchronized(_domainObjectReferenceListeners) {
				if (!_domainObjectReferenceListeners.contains(listener)) {
					_domainObjectReferenceListeners.add(listener);
				}
			}
			return listener;
		}
		public void removeExtendedDomainObjectReferenceListener(IExtendedDomainObjectReferenceListener listener) {
			synchronized(_domainObjectReferenceListeners) {
				_domainObjectReferenceListeners.remove(listener);
			}
		}
		
		/**
		 * public so that it can be invoked by NotifyListenersAspect.
		 * 
		 * @param attribute
		 * @param newPrerequisites
		 */
		public void notifyReferenceListeners(IPrerequisites newPrerequisites) {
			ExtendedDomainObjectReferenceEvent event = 
				new ExtendedDomainObjectReferenceEvent(this, newPrerequisites);
			for(IExtendedDomainObjectReferenceListener listener: _domainObjectReferenceListeners) {
				listener.referencePrerequisitesChanged(event);
			}
		}

		/*
		 * 
		 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedReference#prerequisitesFor(java.lang.Object)
		 */
		public IPrerequisites prerequisitesFor(Object candidateValue) {
			return          accessorPrerequisitesFor()
		    .andRequire(authorizationPrerequisitesFor())
			.andRequire(mutatorPrerequisitesFor(candidateValue));
		}

		/*
		 * 
		 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedReference#prerequisitesFor(java.lang.Object, boolean)
		 */
		public IPrerequisites prerequisitesFor(Object candidateValue, boolean beingAdded) {
			return          accessorPrerequisitesFor()
		    .andRequire(authorizationPrerequisitesFor())
			.andRequire(mutatorPrerequisitesFor(candidateValue, beingAdded));
		}


	}

	public class ExtendedOperation implements IExtendedDomainObject.IExtendedOperation {

		private final EOperation _eOperation;
		private final Class<?>[] _parameterTypes;
		private final Map<Integer, Object> argsByPosition = new HashMap<Integer, Object>();
		private final List<IExtendedDomainObjectOperationListener> _domainObjectOperationListeners = 
			new ArrayList<IExtendedDomainObjectOperationListener>();

		/**
		 * Will reset the arguments, according to {@link #reset()}.
		 * 
		 * <p>
		 * Cannot instantiate directly, instead use 
		 * {@link IExtendedDomainObject#getOperation(EOperation)}.
		 * 
		 * @param eOperation
		 */
		private ExtendedOperation(final EOperation eOperation) {
			this._eOperation = eOperation;
			EList eParameters = eOperation.getEParameters();
			this._parameterTypes = new Class<?>[eParameters.size()];
			for(int i=0; i<_parameterTypes.length; i++) {
				EParameter eParameter = (EParameter)eParameters.get(i);
				_parameterTypes[i] = eParameter.getEType().getInstanceClass();
			}
			reset();
		}
		
		public IPrerequisites authorizationPrerequisitesFor() {
			IRuntimeDomainClass<T> rdc = adapts().getDomainClass();
			return rdc.authorizationConstraintFor(_eOperation);
		}
		
		
		/**
		 * Returns listener only because it simplifies test implementation to do so.
		 */
		public <T extends IExtendedDomainObjectOperationListener> T addExtendedDomainObjectOperationListener(T listener) {
			synchronized(_domainObjectOperationListeners) {
				if (!_domainObjectOperationListeners.contains(listener)) {
					_domainObjectOperationListeners.add(listener);
				}
			}
			return listener;
		}
		public void removeExtendedDomainObjectOperationListener(IExtendedDomainObjectOperationListener listener) {
			synchronized(_domainObjectOperationListeners) {
				_domainObjectOperationListeners.remove(listener);
			}
		}
		
		/**
		 * public so that it can be invoked by NotifyListenersAspect.
		 *
		 * @param attribute
		 * @param newPrerequisites
		 */
		public void notifyOperationListeners(IPrerequisites newPrerequisites) {
			ExtendedDomainObjectOperationEvent event = 
				new ExtendedDomainObjectOperationEvent(this, newPrerequisites);
			for(IExtendedDomainObjectOperationListener listener: _domainObjectOperationListeners) {
				listener.operationPrerequisitesChanged(event);
			}
		}

		public EOperation getEOperation() {
			return _eOperation;
		}

		public Object[] getArgs() {
			Object[] args = new Object[_eOperation.getEParameters().size()];
			for(int i=0; i<args.length; i++) {
				args[i] = getArg(i);
			}
			return args;
		}
		private Object getArg(final int position) {
			Object arg = argsByPosition.get(position);
			if (arg != null) {
				return arg;
			}
			arg = __defaultValueByPrimitiveType.get(_parameterTypes[position]);
			if (arg != null) {
				return arg;
			}
			try {
				Constructor<?> publicConstructor = _parameterTypes[position].getConstructor();
				arg = publicConstructor.newInstance();
				return arg;
			} catch (Exception ex) {
				throw new IllegalArgumentException(
					"No public constructor for '" + _parameterTypes[position].getCanonicalName() + "'" 
					+ " (arg position=" + position + ")", ex);
			} 
		}
 
		public void setArg(final int position, final Object arg) {
			if (position < 0 || position >= _parameterTypes.length) {
				throw new IllegalArgumentException("Invalid position: 0 <= position < " + _parameterTypes.length);
			}
			if (arg != null) {
				if (!isAssignableFromIncludingAutoboxing(_parameterTypes[position], arg.getClass())) {
					throw new IllegalArgumentException("Incompatible argument for position '" + position + "'; formal='" + _parameterTypes[position].getName() + ", actual='" + arg.getClass().getName() + "'"); 
				}
			}
			argsByPosition.put(position, arg);
		}

		private boolean isAssignableFromIncludingAutoboxing(Class<?> requiredType, Class<? extends Object> candidateType) {
			if (requiredType == byte.class && candidateType == Byte.class) { return true; }
			if (requiredType == short.class && candidateType == Short.class) { return true; }
			if (requiredType == int.class && candidateType == Integer.class) { return true; }
			if (requiredType == long.class && candidateType == Long.class) { return true; }
			if (requiredType == char.class && candidateType == Character.class) { return true; }
			if (requiredType == float.class && candidateType == Float.class) { return true; }
			if (requiredType == double.class && candidateType == Double.class) { return true; }
			if (requiredType == boolean.class && candidateType == Boolean.class) { return true; }
			return requiredType.isAssignableFrom(candidateType);
		}

		public IPrerequisites prerequisitesFor() {
			IExtendedRuntimeDomainClass<T> erdc = getExtendedRuntimeDomainClass();
			Method invokePre = erdc.getInvokePre(_eOperation);
			if (invokePre == null) {
				return Prerequisites.none();
			}
			
			try {
				Object[] args = getArgs();
				return (IPrerequisites)invokePre.invoke(adapts().getPojo(), args);
			} catch (IllegalArgumentException ex) {
				return Prerequisites.unusable(ex.getLocalizedMessage());
			} catch (IllegalAccessException ex) {
				return Prerequisites.unusable(ex.getLocalizedMessage());
			} catch (InvocationTargetException ex) {
				return Prerequisites.unusable(ex.getLocalizedMessage());
			}
		}

		/*
		 * 
		 * @see de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedOperation#reset()
		 */
		public Object[] reset() {
			argsByPosition.clear();
			IExtendedRuntimeDomainClass<T> erdc = getExtendedRuntimeDomainClass();
			Method invokeDefaults = erdc.getInvokeDefaults(_eOperation);
			if (invokeDefaults == null) {
				return getArgs();
			}
			try {
				// create an array of 1-element arrays; the defaulter method
				// will expect this structure and will populate the element
				// (simulates passing by reference)
				Object[] argDefaultArrays = new Object[_parameterTypes.length];
				for(int i=0; i<argDefaultArrays.length; i++) {
					setDefaultArray( argDefaultArrays, i, Array.newInstance(_parameterTypes[i], 1), _parameterTypes[i]);
				}
				
				// invoke the defaults method itself
				invokeDefaults.invoke(adapts().getPojo(), argDefaultArrays);
				// now copy out the elements into the actual arguments array
				// held by this extendedOp object.
				for(int i=0; i<argDefaultArrays.length; i++) {
					Object argDefaultArray = argDefaultArrays[i];
					Object argDefault = Array.get(argDefaultArray, 0);
					argsByPosition.put(i, argDefault);
				}
				
				return getArgs();
			} catch (IllegalArgumentException ex) {
				return getArgs();
			} catch (IllegalAccessException ex) {
				return getArgs();
			} catch (InvocationTargetException ex) {
				return getArgs();
			}
		}

		private void setDefaultArray(Object[] argDefaultArrays, int i, Object arrayOfType, Class<?> componentType) {
			argDefaultArrays[i] = arrayOfType;
//			if (componentType == byte.class) {
//				argDefaultArrays[i] = (byte[])arrayOfType;
//			}
//			else if (componentType == short.class) {
//				argDefaultArrays[i] = (short[])arrayOfType;
//			}
//			else if (componentType == int.class) {
//				argDefaultArrays[i] = (int[])arrayOfType;
//			}
//			else if (componentType == long.class) {
//				argDefaultArrays[i] = (long[])arrayOfType;
//			}
//			else if (componentType == char.class) {
//				argDefaultArrays[i] = (char[])arrayOfType;
//			}
//			else if (componentType == float.class) {
//				argDefaultArrays[i] = (float[])arrayOfType;
//			}
//			else if (componentType == double.class) {
//				argDefaultArrays[i] = (double[])arrayOfType;
//			}
//			else if (componentType == boolean.class) {
//				argDefaultArrays[i] = (boolean[])arrayOfType;
//			}
//			else {
//				argDefaultArrays[i] = (Object[])arrayOfType;
//			}
		}
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

}
