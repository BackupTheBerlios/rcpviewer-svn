package org.essentialplatform.runtime.server.domain.bindings;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.essentialplatform.core.deployment.IDomainBinding;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.runtime.shared.AbstractRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.Handle;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectCollectionReference;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOneToOneReference;
import org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOperation;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectAttributeRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectCollectionReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOneToOneReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOperationRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner;
import org.essentialplatform.runtime.shared.domain.handle.IdSemanticsHandleAssigner;
import org.osgi.framework.Bundle;

/**
 * A binding of {@link IDomainClass} for the server environment.
 * 
 * @author Dan Haywood
 */
public final class RuntimeServerBinding extends AbstractRuntimeBinding {
	
	private static Logger _logger = Logger.getLogger(RuntimeServerBinding.class);
	protected Logger getLogger() {
		return _logger;
	}



	//////////////////////////////////////////////////////////////////////

	@Override
	public Bundle getBundle() {
		return Platform.getBundle("org.essentialplatform.domain.runtime");
	}


	/**
	 * Saves the primary builder, and sets up a sequential handle assigner.
	 *
	 * @param primaryBuilder
	 * @throws RuntimeException if a binding has already been set.
	 */
	public RuntimeServerBinding(IDomainBuilder primaryBuilder) {
		super(primaryBuilder);
	}


	@Override
	public IDomainBinding bindingFor(IDomain domain) {
		return new RuntimeServerDomainBinding(domain);
	}
	@Override
	public IDomainClassServerBinding bind(IDomainClass domainClass, Object classRepresentation) {
		IDomainClassServerBinding binding = bind(domainClass, (Class<?>)classRepresentation);
		return binding;
	}
	private <V> IDomainClassServerBinding<V> bind(IDomainClass domainClass, Class<V> javaClass) {
		return new RuntimeServerClassBinding<V>(domainClass, javaClass, _sequentialHandleAssigner);
	}
	
	@Override
	public IAttributeServerBinding bindingFor(IDomainClass.IAttribute attribute) {
		return new RuntimeServerAttributeBinding(attribute);
	}
	@Override
	public IOneToOneReferenceServerBinding bindingFor(IDomainClass.IOneToOneReference oneToOneReference) {
		return new RuntimeServerOneToOneReferenceBinding(oneToOneReference);
	}
	@Override
	public ICollectionReferenceServerBinding bindingFor(IDomainClass.ICollectionReference collectionReference) {
		return new RuntimeServerCollectionReferenceBinding(collectionReference);
	}
	@Override
	public IOperationServerBinding bindingFor(IDomainClass.IOperation operation) {
		return new RuntimeServerOperationBinding(operation);
	}

	//////////////////////////////////////////////////////////////////////

	/**
	 * Injected in server-side.
	 */
	private IHandleAssigner _sequentialHandleAssigner; 
	
	/**
	 * Injected.
	 * 
	 * @param assigner
	 */
	public void setHandleAssigner(IHandleAssigner assigner) {
		_sequentialHandleAssigner = assigner;
	}
	
	//////////////////////////////////////////////////////////////////////
	

	public final class RuntimeServerDomainBinding extends AbstractRuntimeDomainBinding implements IDomainBinding {

		
		public RuntimeServerDomainBinding(IDomain domain) {
			super(domain);
		}
		


	}
	
	public final class RuntimeServerClassBinding<T> extends AbstractRuntimeClassBinding<T> implements IDomainClassServerBinding<T> {

		
		/**
		 * Delegates either to a composite handle assigner or a
		 * sequential handle assigner dependent on the semantics of the
		 * <tt>AssignmentType</tt> of the domain class.
		 * 
		 * @param domainClass
		 * @param javaClass
		 * @param delegateHandleAssigner
		 */
		public RuntimeServerClassBinding(IDomainClass domainClass, Class<T> javaClass, IHandleAssigner delegateHandleAssigner) {
			super(domainClass, javaClass);
			_handleAssigner = new IdSemanticsHandleAssigner(domainClass, delegateHandleAssigner);
		}

		private final IHandleAssigner _handleAssigner; 

		public <V> Handle assignHandleFor(IDomainObject<V> domainObject) {
			return _handleAssigner.assignHandleFor(domainObject);
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.handle.IHandleAssigner#nextAssigner()
		 */
		public IHandleAssigner nextAssigner() {
			return null;
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding#getObjectBinding()
		 */
		public IDomainObjectRuntimeBinding<T> getObjectBinding(IDomainObject domainObject) {
			return new IDomainObjectServerBinding<T>() {

				public void externalStateChanged() {
					// does nothing
					// (required because on the client, we need to update the
					// UI.  Called by AbstractChange, shared across client/server).
				}

				/*
				 * The session binding on the server cannot be cleared at any time.
				 *
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding#assertCanClearSessionBinding()
				 */
				public void assertCanClearSessionBinding() throws IllegalStateException {
					throw new IllegalStateException("SessionBinding may not be cleared");
				}

				/*
				 * Always attached.
				 * 
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding#isAttached()
				 */
				public boolean isAttached() {
					return true;
				}
			};
		}
		

	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeServerAttributeBinding extends AbstractRuntimeAttributeBinding implements IAttributeServerBinding {

		public RuntimeServerAttributeBinding(IDomainClass.IAttribute attribute) {
			super(attribute);
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.bindings.IAttributeRuntimeBinding#getObjectBinding(org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectAttribute)
		 */
		public IObjectAttributeRuntimeBinding getObjectBinding(IObjectAttribute attribute) {
			return new IObjectAttributeServerBinding() {

				/*
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IObjectAttributeRuntimeBinding#gotAttribute()
				 */
				public void gotAttribute() {
					// does nothing
				}
				
			};
		}
		
	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class DelegateRuntimeServerReferenceBinding extends AbstractRuntimeReferenceBinding {
		
		public DelegateRuntimeServerReferenceBinding(IDomainClass.IReference reference) {
			super(reference);
		}
		

	}
	
	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeServerOneToOneReferenceBinding extends AbstractRuntimeOneToOneReferenceBinding implements IOneToOneReferenceServerBinding {

		private final DelegateRuntimeServerReferenceBinding _delegateBinding; 

		public RuntimeServerOneToOneReferenceBinding(IDomainClass.IOneToOneReference oneToOneReference) {
			super(oneToOneReference);
			_delegateBinding = new DelegateRuntimeServerReferenceBinding(oneToOneReference);
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.bindings.IOneToOneReferenceRuntimeBinding#getObjectBinding(org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOneToOneReference)
		 */
		public IObjectOneToOneReferenceRuntimeBinding getObjectBinding(IObjectOneToOneReference oneToOneReference) {
			return new IObjectOneToOneReferenceServerBinding() {

				/*
				 * 
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IObjectReferenceRuntimeBinding#gotReference()
				 */
				public void gotReference() {
					// does nothing
				}

				/*
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IObjectOneToOneReferenceRuntimeBinding#set(java.lang.Object)
				 */
				public void set(Object newReferencedObjectOrNull) {
					// does nothing
				}

			};
		}

		

	}
		
	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeServerCollectionReferenceBinding extends AbstractRuntimeCollectionReferenceBinding implements ICollectionReferenceServerBinding {

		private final DelegateRuntimeServerReferenceBinding _delegateBinding; 

		public RuntimeServerCollectionReferenceBinding(IDomainClass.ICollectionReference collectionReference) {
			super(collectionReference);
			_delegateBinding = new DelegateRuntimeServerReferenceBinding(collectionReference);
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.bindings.ICollectionReferenceRuntimeBinding#getObjectBinding(org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectCollectionReference)
		 */
		public IObjectCollectionReferenceRuntimeBinding getObjectBinding(IObjectCollectionReference reference) {
			return new IObjectCollectionReferenceServerBinding() {
				/*
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IObjectCollectionReferenceRuntimeBinding#addedToCollection()
				 */
				public void addedToCollection() {
					// does nothing
				}
				/*
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IObjectCollectionReferenceRuntimeBinding#removedFromCollection()
				 */
				public void removedFromCollection() {
					// does nothing
				}
				/*
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IObjectReferenceRuntimeBinding#gotReference()
				 */
				public void gotReference() {
					// does nothing
				}
			};
		}

	}

	/**
	 * Implementation note: methods are looked up from serializer (rather than,
	 * say, caching in constructor) because when the binding is first 
	 * instantiated the EMF meta-model may not have been fully populated.
	 */
	public final class RuntimeServerOperationBinding extends AbstractRuntimeOperationBinding implements IOperationServerBinding {

		
		public RuntimeServerOperationBinding(IDomainClass.IOperation operation) {
			super(operation);
		}

		/*
		 * @see org.essentialplatform.runtime.shared.domain.bindings.IOperationRuntimeBinding#getObjectBinding(org.essentialplatform.runtime.shared.domain.IDomainObject.IObjectOperation)
		 */
		public IObjectOperationRuntimeBinding getObjectBinding(IObjectOperation operation) {
			return new IObjectOperationServerBinding() {

				/*
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IObjectOperationRuntimeBinding#gotOperation()
				 */
				public void gotOperation() {
					// does nothing
				}

				/*
				 * @see org.essentialplatform.runtime.shared.domain.bindings.IObjectOperationRuntimeBinding#reset()
				 */
				public Object[] reset() {
					// does nothing
					return null;
				}
				
			};
		}
	}


}
