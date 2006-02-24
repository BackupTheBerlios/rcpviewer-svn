package org.essentialplatform.runtime.client.domain.bindings;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.essentialplatform.runtime.client.domain.event.DomainObjectAttributeEvent;
import org.essentialplatform.runtime.client.domain.event.DomainObjectReferenceEvent;
import org.essentialplatform.runtime.client.domain.event.ExtendedDomainObjectAttributeEvent;
import org.essentialplatform.runtime.client.domain.event.ExtendedDomainObjectOperationEvent;
import org.essentialplatform.runtime.client.domain.event.ExtendedDomainObjectReferenceEvent;
import org.essentialplatform.runtime.client.domain.event.IDomainObjectAttributeListener;
import org.essentialplatform.runtime.client.domain.event.IDomainObjectListener;
import org.essentialplatform.runtime.client.domain.event.IDomainObjectOperationListener;
import org.essentialplatform.runtime.client.domain.event.IDomainObjectReferenceListener;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.AbstractRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.ICachesToString;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IObservedFeature;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectCollectionReference;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOneToOneReference;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOperation;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectAttributeRuntimeBinding;
import org.osgi.framework.Bundle;

/**
 * A binding of {@link IDomainClass} for the client environment.
 * 
 * @author Dan Haywood
 */
public final class RuntimeClientBinding extends AbstractRuntimeBinding {

	/**
	 * Provided for nested static classes.
	 */
	private static Logger _logger = Logger.getLogger(RuntimeClientBinding.class); 
	@Override
	protected Logger getLogger() {
		return _logger;
	}

	//////////////////////////////////////////////////////////////////////

	

	@Override
	public IDomainBinding bindingFor(IDomain domain) {
		return new RuntimeClientDomainBinding(domain);
	}

	@Override
	public IDomainClassClientBinding bind(IDomainClass domainClass,
			Object classRepresentation) {
		IDomainClassClientBinding binding = bind(domainClass,
				(Class<?>) classRepresentation);
		return binding;
	}

	private <V> IDomainClassClientBinding<V> bind(IDomainClass domainClass,
			Class<V> javaClass) {
		return new RuntimeClientClassBinding<V>(domainClass, javaClass);
	}

	@Override
	public IAttributeClientBinding bindingFor(IDomainClass.IAttribute attribute) {
		return new RuntimeClientAttributeBinding(attribute);
	}

	@Override
	public IOneToOneReferenceClientBinding bindingFor(
			IDomainClass.IOneToOneReference oneToOneReference) {
		return new RuntimeClientOneToOneReferenceBinding(oneToOneReference);
	}

	@Override
	public ICollectionReferenceClientBinding bindingFor(
			IDomainClass.ICollectionReference collectionReference) {
		return new RuntimeClientCollectionReferenceBinding(collectionReference);
	}

	@Override
	public IOperationClientBinding bindingFor(IDomainClass.IOperation operation) {
		return new RuntimeClientOperationBinding(operation);
	}


	// ////////////////////////////////////////////////////////////////////

	public final class RuntimeClientDomainBinding extends
			AbstractRuntimeDomainBinding implements IDomainBinding {

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
		 * using dependency injection, see
		 * {@link #setAuthorizationManager(IAuthorizationManager)}.
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
		public void setAuthorizationManager(
				IAuthorizationManager authorizationManager) {
			if (authorizationManager == null) {
				throw new IllegalArgumentException(
						"IAuthorizationManager may not be null - consider IAuthorizationManager.NOOP instead");
			}
			_authorizationManager = authorizationManager;
		}

	}

	public final class RuntimeClientClassBinding<T> extends
			AbstractRuntimeClassBinding<T> implements
			IDomainClassClientBinding<T> {

		/**
		 * Delegates either to a composite handle assigner or a
		 * sequential handle assigner dependent on the semantics of the
		 * <tt>AssignmentType</tt> of the domain class.
		 * 
		 * @param domainClass
		 * @param javaClass
		 */
		public RuntimeClientClassBinding(IDomainClass domainClass,
				Class<T> javaClass) {
			super(domainClass, javaClass);
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding#getObjectBinding()
		 */
		public IDomainObjectRuntimeBinding<T> getObjectBinding(final IDomainObject domainObject) {
			return new IDomainObjectClientBinding<T>() {

				//////////////////////////////////////////////////////////////////////////
				// title() 
				//////////////////////////////////////////////////////////////////////////

				/*
				 * For the title we just return the POJO's <code>toString()</code>.
				 * 
				 * @see org.essentialplatform.session.IDomainObject#title()
				 */
				public String title() {
					return domainObject.getPojo().toString();
				}

				
				//////////////////////////////////////////////////////////////////////////
				// ClientSession, assertCanClearSessionBinding 
				//////////////////////////////////////////////////////////////////////////
				
				private IClientSession _session;

				/*
				 * @see org.essentialplatform.runtime.client.domain.bindings.IDomainObjectClientBinding#getSession()
				 */
				public IClientSession getSession() {
					return _session;
				}

				/*
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding#assertCanClearSessionBinding()
				 */
				public void assertCanClearSessionBinding() throws IllegalStateException {
					if (isAttached()) {
						throw new IllegalStateException(
								"Cannot clear session binding when attached to session");
					}
				}

				//////////////////////////////////////////////////////////////////////////
				// attached, detached, isAttached 
				//////////////////////////////////////////////////////////////////////////

				/*
				 * @see org.essentialplatform.runtime.client.domain.bindings.IDomainObjectClientBinding#attached()
				 */
				public void attached(IClientSession session) {
					if (session == null) {
						throw new IllegalArgumentException("Session is null");
					}
					if (domainObject.getSessionBinding() == null) {
						domainObject.setSessionBinding(session.getSessionBinding());
					} else {
						if (!domainObject.getSessionBinding().equals(session.getSessionBinding())) {
							throw new IllegalArgumentException("Session's sessionBinding does not match "
									+ "(session.sessionBinding = '" + session.getSessionBinding() + "', "
									+ "this (domain object's) sessionBinding = '" + domainObject.getSessionBinding() + "')");
						}
					}
					this._session = session;
				}

				/*
				 * @see org.essentialplatform.runtime.client.domain.bindings.IDomainObjectClientBinding#detached()
				 */
				public void detached() {
					this._session = null;
				}

				/*
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding#isAttached()
				 */
				public boolean isAttached() {
					return _session != null;
				}


				//////////////////////////////////////////////////////////////////////////
				// Listeners 
				//////////////////////////////////////////////////////////////////////////
				
				/**
				 * Marked as <tt>transient</tt> so that it is not distributed.
				 */
				private transient List<IDomainObjectListener> _domainObjectListeners = new ArrayList<IDomainObjectListener>();

				/*
				 * @see org.essentialplatform.session.IDomainObject#addListener(null)
				 */
				public <T extends IDomainObjectListener> T addListener(T listener) {
					synchronized (_domainObjectListeners) {
						if (!_domainObjectListeners.contains(listener)) {
							_domainObjectListeners.add(listener);
						}
					}
					return listener;
				}

				/*
				 * @see org.essentialplatform.session.IDomainObject#removeListener(org.essentialplatform.session.IDomainObjectListener)
				 */
				public void removeListener(IDomainObjectListener listener) {
					synchronized (_domainObjectListeners) {
						_domainObjectListeners.remove(listener);
					}
				}

				//////////////////////////////////////////////////////////////////////////
				// IObservableFeature (externalStateChanged) 
				//////////////////////////////////////////////////////////////////////////
				

				/*
				 * If the session is unknown (ie the domain object has been
				 * detached), then does nothing. 
				 */
				public void externalStateChanged() {
					IClientSession session = getSession();
					if (session == null) {
						return;
					}
					for(IObservedFeature observedFeature: session.getObservedFeatures()) {
						observedFeature.externalStateChanged();
					}
				}

			};
		}

	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeClientAttributeBinding extends
			AbstractRuntimeAttributeBinding implements IAttributeClientBinding {

		public RuntimeClientAttributeBinding(IDomainClass.IAttribute attribute) {
			super(attribute);
		}

		public IPrerequisites authorizationPrerequisites() {
			IDomain domain = _attribute.getDomainClass().getDomain();
			RuntimeClientDomainBinding domainBinding = (RuntimeClientDomainBinding) domain
					.getBinding();
			return domainBinding.getAuthorizationManager().preconditionsFor(
					_attribute.getFeatureId());
		}

		public IPrerequisites accessorPrerequisitesFor(final Object pojo) {
			return invokePrerequisites(getAccessorPre(), pojo, new Object[] {},
					"accessor");
		}

		public IPrerequisites mutatorPrerequisitesFor(Object pojo,
				Object candidateValue) {
			return invokePrerequisites(getMutatorPre(), pojo,
					new Object[] { candidateValue }, "mutator");
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.bindings.IAttributeRuntimeBinding#getObjectBinding(org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute)
		 */
		public IObjectAttributeRuntimeBinding getObjectBinding(
				final IObjectAttribute objectAttribute) {
			return new IObjectAttributeClientBinding() {
				private RuntimeClientAttributeBinding _runtimeClassBinding = RuntimeClientAttributeBinding.this;

				// ////////////////////////////////////////////////////////////////////////
				// xxxPrerequisitesFor
				// ////////////////////////////////////////////////////////////////////////

				/**
				 * Holds onto the current accessor prerequisites, if known.
				 * 
				 * <p>
				 * Used to determine whether listeners should be notified (see
				 * {@link #notifyListeners(IPrerequisites)}) whenever there is
				 * some external state change ({@link #externalStateChanged()}).
				 * 
				 */
				private IPrerequisites _currentPrerequisites;

				/*
				 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute#prerequisitesFor(java.lang.Object)
				 */
				public IPrerequisites prerequisitesFor(
						final Object candidateValue) {
					return accessorPrerequisitesFor().andRequire(
							authorizationPrerequisitesFor()).andRequire(
							mutatorPrerequisitesFor(candidateValue));
				}

				/*
				 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectMember#authorizationPrerequisitesFor()
				 */
				public IPrerequisites authorizationPrerequisitesFor() {
					IDomainClass rdc = objectAttribute.getAttribute()
							.getDomainClass();
					IDomainClass.IAttribute attribute = rdc
							.getIAttribute(_eAttribute);
					RuntimeClientAttributeBinding binding = (RuntimeClientAttributeBinding) attribute
							.getBinding();
					return binding.authorizationPrerequisites();
				}

				/*
				 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute#accessorPrerequisitesFor()
				 */
				public IPrerequisites accessorPrerequisitesFor() {
					return _runtimeClassBinding
							.accessorPrerequisitesFor(objectAttribute
									.getDomainObject().getPojo());
				}

				/*
				 * Extended semantics.
				 */
				public IPrerequisites mutatorPrerequisitesFor(
						final Object candidateValue) {
					return _runtimeClassBinding.mutatorPrerequisitesFor(
							objectAttribute.getDomainObject().getPojo(),
							candidateValue);
				}

				// ////////////////////////////////////////////////////////////////////////
				// Listeners
				// ////////////////////////////////////////////////////////////////////////

				private final List<IDomainObjectAttributeListener> _listeners = new ArrayList<IDomainObjectAttributeListener>();

				/*
				 * Returns listener only because it simplifies test
				 * implementation to do so.
				 */
				public <T extends IDomainObjectAttributeListener> T addListener(
						T listener) {
					synchronized (_listeners) {
						if (!_listeners.contains(listener)) {
							_listeners.add(listener);
						}
					}
					return listener;
				}

				public void removeListener(
						IDomainObjectAttributeListener listener) {
					synchronized (_listeners) {
						_listeners.remove(listener);
					}
				}

				/*
				 * @see org.essentialplatform.session.IDomainObject.IObjectAttribute#notifyListeners(java.lang.Object)
				 */
				public void notifyListeners(Object newValue) {
					DomainObjectAttributeEvent event = new DomainObjectAttributeEvent(
							objectAttribute, newValue);
					for (IDomainObjectAttributeListener listener : _listeners) {
						listener.attributeChanged(event);
					}
				}

				/**
				 * public so that it can be invoked by NotifyListenersAspect.
				 * 
				 * @param attribute
				 * @param newPrerequisites
				 */
				public void notifyListeners(IPrerequisites newPrerequisites) {
					ExtendedDomainObjectAttributeEvent event = new ExtendedDomainObjectAttributeEvent(
							objectAttribute, newPrerequisites);
					for (IDomainObjectAttributeListener listener : _listeners) {
						listener.attributePrerequisitesChanged(event);
					}
				}

				//////////////////////////////////////////////////////////////////////////
				// Session#addObservedFeature, externalStateChanged
				//////////////////////////////////////////////////////////////////////////


				/*
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IObjectAttributeRuntimeBinding#gotAttribute()
				 */
				public void gotAttribute() {
					final IDomainObject<?> domainObject = objectAttribute.getDomainObject();
					IDomainObjectClientBinding<?> objBinding = (IDomainObjectClientBinding)domainObject.getBinding();
					IClientSession session = objBinding.getSession();
					session.addObservedFeature(this);
				}

				/*
				 * @see org.essentialplatform.runtime.client.domain.IObservedFeature#externalStateChanged()
				 */
				public void externalStateChanged() {
					IPrerequisites prerequisitesPreviously = _currentPrerequisites;
					IPrerequisites prerequisitesNow = accessorPrerequisitesFor();

					boolean notifyListeners = prerequisitesPreviously == null
							|| !prerequisitesPreviously
									.equals(prerequisitesNow);

					_currentPrerequisites = prerequisitesNow;
					if (notifyListeners) {
						notifyListeners(_currentPrerequisites);
					}
				}

				// ////////////////////////////////////////////////////////////////////////
				// toString
				// ////////////////////////////////////////////////////////////////////////

				@Override
				public String toString() {
					return objectAttribute.getAttribute().toString() + ", "
							+ _listeners.size() + " listeners" + ", prereqs="
							+ _currentPrerequisites;
				}

			};
		}
	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class DelegateRuntimeClientReferenceBinding extends
			AbstractRuntimeReferenceBinding {

		public DelegateRuntimeClientReferenceBinding(
				IDomainClass.IReference reference) {
			super(reference);
		}

		public IPrerequisites authorizationPrerequisites() {
			IDomain domain = _reference.getDomainClass().getDomain();
			RuntimeClientDomainBinding domainBinding = (RuntimeClientDomainBinding) domain
					.getBinding();
			return domainBinding.getAuthorizationManager().preconditionsFor(
					_reference.getFeatureId());
		}

		public IPrerequisites accessorPrerequisitesFor(final Object pojo) {
			Method accessorPre = __extendedSerializer
					.getReferenceAccessorPreMethod(_eReference);
			return invokePrerequisites(accessorPre, pojo, new Object[] {},
					"accessor");
		}

	}
	

	private abstract class AbstractObjectReferenceClientBinding implements IObservedFeature {

		private final IDomainObject.IObjectReference _objectReference;

		final IReferenceClientBinding _runtimeClassBinding;

		AbstractObjectReferenceClientBinding(
				IDomainObject.IObjectReference operationReference,
				IReferenceClientBinding runtimeClassBinding) {
			_objectReference = operationReference;
			_runtimeClassBinding = runtimeClassBinding;
			addListener(new CachesToStringNotifier(_objectReference));
		}

		// ////////////////////////////////////////////////////////////////////////
		// xxxPrerequisitesFor
		// ////////////////////////////////////////////////////////////////////////

		/**
		 * Holds onto the current accessor prerequisites, if known.
		 * 
		 * <p>
		 * Used to determine whether listeners should be notified (see
		 * {@link #notifyReferenceListeners(IPrerequisites)}) whenever there is
		 * some external state change ({@link #externalStateChanged()}).
		 */
		private IPrerequisites _currentPrerequisites;

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectMember#authorizationPrerequisitesFor()
		 */
		public IPrerequisites authorizationPrerequisitesFor() {
			return _runtimeClassBinding.authorizationPrerequisites();
		}

		/*
		 * @see org.essentialplatform.session.IDomainObject.IObjectReference#accessorPrerequisitesFor()
		 */
		public IPrerequisites accessorPrerequisitesFor() {
			return _runtimeClassBinding.accessorPrerequisitesFor(_objectReference.getDomainObject().getPojo());
		}

		//////////////////////////////////////////////////////////////////////////
		// Listeners
		//////////////////////////////////////////////////////////////////////////

		final List<IDomainObjectReferenceListener> _listeners = new ArrayList<IDomainObjectReferenceListener>();

		public <T extends IDomainObjectReferenceListener> T addListener(
				T listener) {
			_listeners.add(listener);
			return listener;
		}

		public void removeListener(IDomainObjectReferenceListener listener) {
			_listeners.remove(listener);
		}

		/**
		 * public so that it can be invoked externally by aspects.
		 * 
		 * @param newPrerequisites
		 */
		public void notifyReferenceListeners(IPrerequisites newPrerequisites) {
			ExtendedDomainObjectReferenceEvent event = new ExtendedDomainObjectReferenceEvent(
					_objectReference, newPrerequisites);
			for (IDomainObjectReferenceListener listener : _listeners) {
				listener.referencePrerequisitesChanged(event);
			}
		}

		//////////////////////////////////////////////////////////////////////////
		// Session#addObservedFeature, externalStateChanged
		//////////////////////////////////////////////////////////////////////////


		/*
		 * @see org.essentialplatform.runtime.shared.domain.bindings.IObjectReferenceRuntimeBinding#gotReference()
		 */
		public void gotReference() {
			final IDomainObject<?> domainObject = _objectReference.getDomainObject();
			IDomainObjectClientBinding<?> objBinding = (IDomainObjectClientBinding)domainObject.getBinding();
			IClientSession session = objBinding.getSession();
			session.addObservedFeature(this);
		}

		
		/*
		 * @see org.essentialplatform.session.IObservedFeature#externalStateChanged()
		 */
		public void externalStateChanged() {
			IPrerequisites prerequisitesPreviously = _currentPrerequisites;
			IPrerequisites prerequisitesNow = accessorPrerequisitesFor();

			boolean notifyListeners = prerequisitesPreviously == null
					|| !prerequisitesPreviously.equals(prerequisitesNow);

			_currentPrerequisites = prerequisitesNow;
			if (notifyListeners) {
				notifyReferenceListeners(_currentPrerequisites);
			}
		}

		// ////////////////////////////////////////////////////////////////////////
		// toString
		// ////////////////////////////////////////////////////////////////////////

		@Override
		public String toString() {
			return _objectReference.toString() + ", " + _listeners.size()
					+ " listeners" + ", prereqs=" + _currentPrerequisites;
		}
	}


	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeClientOneToOneReferenceBinding 
			extends AbstractRuntimeOneToOneReferenceBinding 
			implements IOneToOneReferenceClientBinding {

		private final DelegateRuntimeClientReferenceBinding _delegateBinding;

		public RuntimeClientOneToOneReferenceBinding(
				IDomainClass.IOneToOneReference oneToOneReference) {
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
		 * @see org.essentialplatform.core.deployment.Binding.IOneToOneReferenceBinding#mutatorPrerequisitesFor(java.lang.Object,
		 *      java.lang.Object)
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object pojo,
				final Object candidateValue) {
			return invokePrerequisites(getMutatorPre(), pojo, new Object[] {},
					"mutator");
		}

		public IObjectOneToOneReferenceClientBinding getObjectBinding(
				final IObjectOneToOneReference reference) {
			class OneToOneReferenceClientBinding 
					extends AbstractObjectReferenceClientBinding 
					implements IObjectOneToOneReferenceClientBinding {
				
				OneToOneReferenceClientBinding() {
					super(reference, RuntimeClientOneToOneReferenceBinding.this);
				}
				
				private IOneToOneReferenceClientBinding getOneToOneRuntimeClassBinding() {
					return (IOneToOneReferenceClientBinding)_runtimeClassBinding;
				}
				
				//////////////////////////////////////////////////////////////////////////
				// xxxPrerequisitesFor
				//////////////////////////////////////////////////////////////////////////

				/*
				 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOneToOneReference#prerequisitesFor(java.lang.Object)
				 */
				public IPrerequisites prerequisitesFor(Object candidateValue) {
					return accessorPrerequisitesFor().andRequire(
							authorizationPrerequisitesFor()).andRequire(
							mutatorPrerequisitesFor(candidateValue));
				}

				/*
				 * Extended semantics.
				 */
				public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue) {
					return getOneToOneRuntimeClassBinding().mutatorPrerequisitesFor(
							reference.getDomainObject().getPojo(), candidateValue);
				}


				//////////////////////////////////////////////////////////////////////////
				// Listeners
				//////////////////////////////////////////////////////////////////////////

				/*
				 * @see org.essentialplatform.session.IDomainObject.IObjectReference#addListener(org.essentialplatform.session.IDomainObjectReferenceListener)
				 */
				public <Q extends IDomainObjectReferenceListener> Q addListener(Q listener) {
					synchronized (_listeners) {
						if (!_listeners.contains(listener)) {
							_listeners.add(listener);
						}
					}
					return listener;
				}

				/*
				 * @see org.essentialplatform.session.IDomainObject.IObjectReference#removeListener(org.essentialplatform.session.IDomainObjectReferenceListener)
				 */
				public void removeListener(IDomainObjectReferenceListener listener) {
					synchronized (_listeners) {
						_listeners.remove(listener);
					}
				}

				/*
				 * @see org.essentialplatform.session.IDomainObject.IObjectOneToOneReference#notifyListeners(java.lang.Object)
				 */
				public void set(Object referencedObject) {
					DomainObjectReferenceEvent event = new DomainObjectReferenceEvent(reference, referencedObject);
					for (IDomainObjectReferenceListener listener : _listeners) {
						listener.referenceChanged(event);
					}
				}

				
			};
			return new OneToOneReferenceClientBinding();
		}

	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeClientCollectionReferenceBinding extends
			AbstractRuntimeCollectionReferenceBinding implements
			ICollectionReferenceClientBinding {

		private final DelegateRuntimeClientReferenceBinding _delegateBinding;

		public RuntimeClientCollectionReferenceBinding(
				IDomainClass.ICollectionReference collectionReference) {
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
		 * @see org.essentialplatform.core.deployment.Binding.ICollectionReferenceBinding#mutatorPrerequisitesFor(java.lang.Object,
		 *      java.lang.Object, boolean)
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object pojo,
				final Object candidateValue, final boolean beingAdded) {
			Method mutatorPre = beingAdded ? getAddToPre() : getRemoveFromPre();
			return invokePrerequisites(mutatorPre, pojo,
					new Object[] { candidateValue }, "mutator");
		}

		public IObjectCollectionReferenceClientBinding getObjectBinding(
				final IObjectCollectionReference objectReference) {
			class CollectionReferenceClientBinding extends
					AbstractObjectReferenceClientBinding implements
					IObjectCollectionReferenceClientBinding {
				
				CollectionReferenceClientBinding() {
					super(objectReference, RuntimeClientCollectionReferenceBinding.this);
				}

				private ICollectionReferenceClientBinding getCollectionRuntimeClassBinding() {
					return (ICollectionReferenceClientBinding)_runtimeClassBinding;
				}
				

				//////////////////////////////////////////////////////////////////////////
				// xxxPrerequisitesFor
				//////////////////////////////////////////////////////////////////////////

				public IPrerequisites mutatorPrerequisitesFor(Object candidateValue, boolean beingAdded) {
					return getCollectionRuntimeClassBinding().mutatorPrerequisitesFor(objectReference.getDomainObject().getPojo(), candidateValue, beingAdded);
				}
				
				public IPrerequisites prerequisitesFor(Object candidateValue,
						boolean beingAdded) {
					return accessorPrerequisitesFor().andRequire(
							authorizationPrerequisitesFor()).andRequire(
							mutatorPrerequisitesFor(candidateValue, beingAdded));
				}

				
				//////////////////////////////////////////////////////////////////////////
				// Listeners
				//////////////////////////////////////////////////////////////////////////

				/*
				 * @see org.essentialplatform.session.IDomainObject.IObjectCollectionReference#notifyListeners(java.lang.Object,
				 *      boolean)
				 */
				public void notifyListeners(Object referencedObject, boolean beingAdded) {
					DomainObjectReferenceEvent event = 
						new DomainObjectReferenceEvent(objectReference, referencedObject);
					for (IDomainObjectReferenceListener listener : _listeners) {
						if (beingAdded) {
							listener.collectionAddedTo(event);
						} else {
							listener.collectionRemovedFrom(event);
						}
					}
				}

				/*
				 * @see org.essentialplatform.runtime.client.domain.bindings.IObjectCollectionReferenceClientBinding#addedToCollection()
				 */
				public void addedToCollection() {
					// TODO: ideally the notifyListeners aspect should do this for us?
					// notify _domainObjectListeners
					DomainObjectReferenceEvent event = 
						new DomainObjectReferenceEvent(objectReference, objectReference.getDomainObject().getPojo());
					for (IDomainObjectReferenceListener listener : _listeners) {
						listener.collectionAddedTo(event);
					}
				}

				/*
				 * @see org.essentialplatform.runtime.client.domain.bindings.IObjectCollectionReferenceClientBinding#removedFromCollection()
				 */
				public void removedFromCollection() {
					// TODO: ideally the notifyListeners aspect should do this for us?
					// notify _domainObjectListeners
					DomainObjectReferenceEvent event = 
						new DomainObjectReferenceEvent(objectReference, objectReference.getDomainObject().getPojo());
					for (IDomainObjectReferenceListener listener : _listeners) {
						listener.collectionRemovedFrom(event);
					}
				}

			}
			return new CollectionReferenceClientBinding();
		}

	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeClientOperationBinding extends
			AbstractRuntimeOperationBinding implements IOperationClientBinding {

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

		/**
		 * TODO: move down into instance-level binding.
		 */
		public Object invokeOperation(final Object pojo, final Object[] args) {
			return invokeInvoker(getOperationInvoker(), pojo, args, "operation");
		}

		public IPrerequisites authorizationPrerequisites() {
			IDomain domain = _operation.getDomainClass().getDomain();
			RuntimeClientDomainBinding domainBinding = (RuntimeClientDomainBinding) domain
					.getBinding();
			return domainBinding.getAuthorizationManager().preconditionsFor(
					_operation.getFeatureId());
		}

		public IPrerequisites prerequisitesFor(final Object pojo, Object[] args) {
			Method invokePre = __extendedSerializer
					.getOperationPreMethod(_eOperation);
			return invokePrerequisites(invokePre, pojo, args, "operation");
		}

		public Object[] reset(final Object pojo, final Object[] args,
				Map<Integer, Object> argsByPosition) {
			Method invokeDefaults = __extendedSerializer
					.getOperationDefaultsMethod(_eOperation);
			if (invokeDefaults == null) {
				return args;
			}

			try {
				// create an array of 1-element arrays; the defaulter method
				// will expect this structure and will populate the element
				// (simulates passing by reference)
				Object[] argDefaultArrays = new Object[_parameterTypes.length];
				for (int i = 0; i < argDefaultArrays.length; i++) {
					argDefaultArrays[i] = Array.newInstance(_parameterTypes[i],
							1);
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
		 * @see org.essentialplatform.runtime.client.domain.bindings.IOperationClientBinding#getArgs(java.util.Map)
		 */
		public Object[] getArgs(final Map<Integer, Object> argumentsByPosition) {
			Object[] args = new Object[_eOperation.getEParameters().size()];
			for (int i = 0; i < args.length; i++) {
				args[i] = getArg(i, argumentsByPosition);
			}
			return args;
		}

		// helpers

		private Object getArg(final int position,
				Map<Integer, Object> argsByPosition) {
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

		public boolean isAssignableFromIncludingAutoboxing(
				Class<?> requiredType, Class<? extends Object> candidateType) {
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

		public IObjectOperationClientBinding getObjectBinding(
				final IObjectOperation objectOperation) {
			return new IObjectOperationClientBinding() {

				private RuntimeClientOperationBinding _runtimeClassBinding = RuntimeClientOperationBinding.this;

				// ////////////////////////////////////////////////////////////////////////
				// getArgs(), setArg(), reset(), invokeOperation()
				// ////////////////////////////////////////////////////////////////////////

				// TODO: make private
				final Map<Integer, Object> _argsByPosition = new HashMap<Integer, Object>();

				public Object[] getArgs() {
					return _runtimeClassBinding.getArgs(_argsByPosition);
				}

				public void setArg(int position, Object arg) {
					_runtimeClassBinding.assertIsValid(position, arg);
					_argsByPosition.put(position, arg);
				}

				public Object[] reset() {
					_argsByPosition.clear();
					return _runtimeClassBinding.reset(objectOperation
							.getDomainObject().getPojo(), getArgs(),
							_argsByPosition);
				}

				public Object invokeOperation() {
					return _runtimeClassBinding.invokeOperation(objectOperation
							.getDomainObject().getPojo(), getArgs());
				}

				// ////////////////////////////////////////////////////////////////////////
				// xxxPrerequisitesFor
				// ////////////////////////////////////////////////////////////////////////

				/**
				 * Holds onto the current (invoker) prerequisites, if known.
				 * 
				 * <p>
				 * Used to determine whether listeners should be notified (see
				 * {@link #notifyOperationListeners(IPrerequisites)}) whenever
				 * there is some external state change ({@link #externalStateChanged()}).
				 */
				private IPrerequisites _currentPrerequisites;

				/*
				 * @see org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOperation#prerequisitesFor()
				 */
				public IPrerequisites prerequisitesFor() {
					return _runtimeClassBinding.prerequisitesFor(
							objectOperation.getDomainObject().getPojo(),
							getArgs());
				}

				/*
				 * @see org.essentialplatform.runtime.client.domain.bindings.IObjectOperationClientBinding#authorizationPrerequisitesFor()
				 */
				public IPrerequisites authorizationPrerequisitesFor() {
					return _runtimeClassBinding.authorizationPrerequisites();
				}

				// ////////////////////////////////////////////////////////////////////////
				// Listeners
				// ////////////////////////////////////////////////////////////////////////

				private final List<IDomainObjectOperationListener> _listeners = new ArrayList<IDomainObjectOperationListener>();

				/**
				 * Returns listener only because it simplifies test
				 * implementation to do so.
				 */
				public <T extends IDomainObjectOperationListener> T addListener(
						T listener) {
					synchronized (_listeners) {
						if (!_listeners.contains(listener)) {
							_listeners.add(listener);
						}
					}
					return listener;
				}

				/*
				 * @see org.essentialplatform.session.IDomainObject.IObjectOperation#removeListener(org.essentialplatform.session.IDomainObjectOperationListener)
				 */
				public void removeListener(
						IDomainObjectOperationListener listener) {
					synchronized (_listeners) {
						_listeners.remove(listener);
					}
				}

				/**
				 * public so that it can be invoked by NotifyListenersAspect.
				 * 
				 * @param attribute
				 * @param newPrerequisites
				 */
				public void notifyOperationListeners(
						IPrerequisites newPrerequisites) {
					ExtendedDomainObjectOperationEvent event = new ExtendedDomainObjectOperationEvent(
							objectOperation, newPrerequisites);
					for (IDomainObjectOperationListener listener : _listeners) {
						listener.operationPrerequisitesChanged(event);
					}
				}

				// ////////////////////////////////////////////////////////////////////////
				// Session#addObservedFeature, externalStateChanged
				// ////////////////////////////////////////////////////////////////////////

				/*
				 * @see org.essentialplatform.runtime.client.domain.bindings.IObjectOperationClientBinding#gotOperation()
				 */
				public void gotOperation() {
					final IDomainObject<?> domainObject = objectOperation.getDomainObject();
					IDomainObjectClientBinding<?> objBinding = (IDomainObjectClientBinding)domainObject.getBinding();
					IClientSession session = objBinding.getSession();
					session.addObservedFeature(this);
				}


				/*
				 * @see org.essentialplatform.runtime.client.domain.IObservedFeature#externalStateChanged()
				 */
				public void externalStateChanged() {
					IPrerequisites prerequisitesPreviously = _currentPrerequisites;
					IPrerequisites prerequisitesNow = prerequisitesFor();

					boolean notifyListeners = prerequisitesPreviously == null
							|| !prerequisitesPreviously
									.equals(prerequisitesNow);

					_currentPrerequisites = prerequisitesNow;
					if (notifyListeners) {
						notifyOperationListeners(_currentPrerequisites);
					}
				}

				// ////////////////////////////////////////////////////////////////////////
				// toString
				// ////////////////////////////////////////////////////////////////////////

				@Override
				public String toString() {
					return _eOperation.toString() + ", " + _listeners.size()
							+ " listeners" + ", args=" + getArgs()
							+ ", prereqs=" + _currentPrerequisites;
				}

			};
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
	private static Object invokeInvoker(Method method, Object pojo,
			Object[] args, String contextDescription) {
		if (method == null) {
			String formattedMessage = buildErrorMessage(null,
					contextDescription, "invoker", method);
			_logger.error(formattedMessage);
			throw new UnsupportedOperationException(formattedMessage);
		}
		try {
			return method.invoke(pojo, args);
		} catch (IllegalArgumentException ex) {
			String formattedMessage = buildErrorMessage(ex, contextDescription,
					"invoker", method);
			_logger.error(formattedMessage, ex);
			throw ex;
		} catch (IllegalAccessException ex) {
			String formattedMessage = buildErrorMessage(ex, contextDescription,
					"invoker", method);
			_logger.error(formattedMessage, ex);
			throw new UnsupportedOperationException(formattedMessage);
		} catch (InvocationTargetException ex) {
			String formattedMessage = buildErrorMessage(
					ex.getTargetException(), contextDescription, "invoker",
					method);
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
	private static IPrerequisites invokePrerequisites(Method method,
			Object pojo, Object[] args, String contextDescription) {
		if (method == null) {
			return Prerequisites.none();
		}
		try {
			return (IPrerequisites) method.invoke(pojo, args);
		} catch (IllegalArgumentException ex) {
			String formattedMessage = buildErrorMessage(ex, contextDescription,
					"prerequisites", method);
			_logger.error(formattedMessage, ex);
			throw ex;
		} catch (IllegalAccessException ex) {
			String formattedMessage = buildErrorMessage(ex, contextDescription,
					"prerequisites", method);
			_logger.error(formattedMessage, ex);
			throw new UnsupportedOperationException(formattedMessage);
		} catch (InvocationTargetException ex) {
			String formattedMessage = buildErrorMessage(
					ex.getTargetException(), contextDescription,
					"prerequisites", method);
			_logger.error(formattedMessage, ex);
			throw new UnsupportedOperationException(formattedMessage);
		}
	}

	/**
	 * Attribute and reference bindings install an instance of this simple
	 * class as a listener to notify their respective attribute/reference when 
	 * the value contained within has changed.
	 */
	class CachesToStringNotifier implements IDomainObjectReferenceListener, IDomainObjectAttributeListener {
		private final ICachesToString _cachesToString;
		CachesToStringNotifier(final ICachesToString cachesToString) {
			_cachesToString = cachesToString;
		}
		public void collectionAddedTo(DomainObjectReferenceEvent event) {
			_cachesToString.updateCachedToString();
		}
		public void collectionRemovedFrom(DomainObjectReferenceEvent event) {
			_cachesToString.updateCachedToString();
		}
		public void referenceChanged(DomainObjectReferenceEvent event) {
			_cachesToString.updateCachedToString();
		}
		public void referencePrerequisitesChanged(ExtendedDomainObjectReferenceEvent event) {
			_cachesToString.updateCachedToString();
		}
		public void attributeChanged(DomainObjectAttributeEvent event) {
			_cachesToString.updateCachedToString();
		}
		public void attributePrerequisitesChanged(ExtendedDomainObjectAttributeEvent event) {
			_cachesToString.updateCachedToString();
		}
	}

}
