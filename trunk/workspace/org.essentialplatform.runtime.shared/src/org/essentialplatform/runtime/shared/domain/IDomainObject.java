package org.essentialplatform.runtime.shared.domain;

import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainClassRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectAttributeRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectCollectionReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOneToOneReferenceRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IDomainObjectRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.bindings.IObjectOperationRuntimeBinding;
import org.essentialplatform.runtime.shared.persistence.IPersistable;
import org.essentialplatform.runtime.shared.persistence.IResolvable;
import org.essentialplatform.runtime.shared.persistence.IPersistable.PersistState;
import org.essentialplatform.runtime.shared.persistence.IResolvable.ResolveState;
import org.essentialplatform.runtime.shared.session.SessionBinding;
import org.essentialplatform.runtime.shared.transaction.event.ITransactionListener;

/**
 * A wrapper around a pojo, allowing reflective and generic access to that
 * pojo in the context of a describing meta-model, usually attached to an
 * owning session.
 * 
 * <p>
 * The UI layer interacts with the rest of the framework through the
 * IDomainObject: conceptually a single IDomainObject backs every editor 
 * shown in the UI.  Although this interface exposes the underlying pojo, the 
 * UI layer will not interact with the pojo directly because it necessarily 
 * must be generic (not coupled to any particular class of pojo).  Instead, 
 * this interface exposes methods such as {@link #get(EAttribute)} and
 * {@link #set(EAttribute, Object)} to allow reflective interactions.
 * 
 * <p>
 * Moreover, the IDomainObject also plays the role of <i>model</i> within the 
 * MVC pattern.  Again, the UI layer is the archetypal <i>view</i>, being
 * notified of changes through the {@link ITransactionListener} interface. 
 * 
 * <p>
 * TODO: should validate the EOperations and EAttributes, just as we do
 * for IDomainClass.
 * 
 * @author Dan Haywood
 */
public interface IDomainObject<T> extends IResolvable, IPersistable {

	/**
	 * TODO: made public for packager functionality, but need to sort out the
	 * interplay between this and the initAsXxx static methods.
	 * 
	 * @param sessionBinding
	 * @param persistState
	 * @param resolveState
	 */
	public void init(final SessionBinding sessionBinding, PersistState persistState, ResolveState resolveState, IDomainClassRuntimeBinding<T> runtimeClassBinding);
		
	
	//////////////////////////////////////////////////////////////////////////
	// SessionBinding
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the (domain, objectstoreId) to which this domain object
	 * belongs.
	 * 
	 * <p>
	 * Will not normally return <tt>null</tt> unless {@link #clearSessionBinding()}
	 * has been called.  If has been cleared, then can be set again using
	 * {@link #setSessionBinding(SessionBinding)}.
	 * 
	 * @return
	 */
	public SessionBinding getSessionBinding();

	/**
	 * Set the session binding.
	 * 
	 * <p>
	 * The domain (of the session binding) must be compatible with that
	 * annotated on the domain object's {@link IDomainClass}.
	 * 
	 * <p>
	 * This method is principally required so that the domain object's runtime 
	 * binding can set the session binding.
	 * 
	 * @param sessionBinding
	 */
	public void setSessionBinding(SessionBinding sessionBinding);
	



	/**
	 * Clears the binding to the session (in particular, the object store 
	 * identifier).
	 *
	 * <p>
	 * Normally the binding for a domain object is never changed, 
	 * representing both the domain and the object store id of the 
	 * session that originally managed the domain object.  Even 
	 * if a domain object is detached from that session, the binding is 
	 * retained so that - under normal circumstances - the domain object may 
	 * only be re-attached to the same session.
	 * 
	 * <p>
	 * However, if an object has been detached from a session then it is 
	 * possible using this method to clear this binding, thereby allowing
	 * the domain object to be attached to some other session. 
	 * It is expected that the new session has a binding for the same 
	 * {@link IDomain}, though this isn't checked.  This capability may be 
	 * useful for "what-if" analysis and the like.
	 * 
	 * <p>
	 * TODO: should really check the domain reference; could ultimately 
	 * determine from own <tt>@InDomain</tt> annotation. 
	 * 
	 * @throws IllegalStateException - if currently attached.
	 */
	public void clearSessionBinding();

	

	/**
	 * Whether this object is attached to the (binding-specific) session.
	 * 
	 * @return
	 */
	public boolean isAttached();



	//////////////////////////////////////////////////////////////////////////
	// DomainClass, Pojo, Binding
	//////////////////////////////////////////////////////////////////////////

	/**
	 * The domain class that describes the structure of the pojo that this
	 * domain object represents.
	 * 
	 * @return
	 */
	public IDomainClass getDomainClass();

	/**
	 * The underlying pojo managed (wrapped) by this domain object.
	 * 
	 * <p>
	 * The link between pojo and domain objects is bidirectional.  Aspects 
	 * (both client- and server-side) introduce a reference in the pojo to the
	 * wrapping domain object.
	 * 
	 * @return
	 */
	public T getPojo();
	

	/**
	 * Binding of this domain object to a runtime environment (eg client or 
	 * server).
	 * 
	 * @return
	 */
	public IDomainObjectRuntimeBinding<T> getBinding();
	
	
	//////////////////////////////////////////////////////////////////////////
	// Handle
	//////////////////////////////////////////////////////////////////////////


	/**
	 * The identifier by which the pojo wrapped by this domain object can be
	 * retrieved from the configured object store.
	 * 
	 * @return
	 */
	public Handle getHandle();

	
	/**
	 * Note this IS shared because both client and server need to be able to
	 * assign handles.
	 * 
	 * <p>
	 * In the former case, this is a temporary handle for a newly
	 * created domain object that needs to be persisted.  When the object store
	 * eventually assigns the actual Id, then the handle is updated
	 * client-side.
	 * 
	 * @param handle
	 */
	public void assignHandle(Handle handle);

	
	/**
	 * Whether this object has been persisted.
	 * @return
	 */
	public boolean isPersistent();


	//////////////////////////////////////////////////////////////////////////
	// IObjectXxx getXxx (member hashes)
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Returns an {@link IObjectAttribute} such that the run-time state of this
	 * attribute of the owning {@link IDomainObject} can be interacted with.
	 * 
	 * @param iAttribute
	 * @return
	 */
	public IObjectAttribute getAttribute(IDomainClass.IAttribute iAttribute);

	/**
	 * Returns an {@link IObjectOneToOneReference} such that the run-time state of this
	 * reference of the owning {@link IDomainObject} can be interacted with.
	 * 
	 * @param eReference
	 * @return the reference.
	 * @throws IllegalArgumentException if the EReference represents a collection.
	 */
	public IObjectOneToOneReference getOneToOneReference(IDomainClass.IReference iReference) throws IllegalArgumentException;

	/**
	 * Returns an {@link IObjectReference} such that the run-time state of this
	 * reference of the owning {@link IDomainObject} can be interacted with.
	 * 
	 * @param eReference
	 * @return
	 * @throws IllegalArgumentException if the EReference represents a 1:1 reference.
	 */
	public IObjectCollectionReference getCollectionReference(IDomainClass.IReference iReference);

	/**
	 * Returns an {@link IObjectOperation} such that the run-time state of this
	 * operation of the owning {@link IDomainObject} can be interacted with.
	 * 
	 * @param eOperation
	 * @return
	 */
	public IObjectOperation getOperation(IDomainClass.IOperation iOperation);


	//////////////////////////////////////////////////////////////////////////
	// IXxxNamed
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Convenience method that should return the same as the 
	 * corresponding method in {@link IDomainClass}.
	 * 
	 * @param attributeName
	 * @return
	 */
	public IDomainClass.IAttribute getIAttributeNamed(String attributeName);

	/**
	 * Convenience method that should return the same as the 
	 * corresponding method in {@link IDomainClass}.
	 * 
	 * @param operationName
	 * @return
	 */
	public IDomainClass.IOperation getIOperationNamed(String operationName);

	/**
	 * Convenience method that should return the same as the 
	 * corresponding method in {@link IDomainClass}.
	 * 
	 * @param operationName
	 * @return
	 */
	public IDomainClass.IReference getIReferenceNamed(String referenceName);

	

	//////////////////////////////////////////////////////////////////////////
	// Adapters 
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Returns an adapter for this object with respect to the adapter of some
	 * programming model.
	 * 
	 * <p>
	 * The supplied domain object should have been instantiated via the domain 
	 * class upon which the method is invoked. 
	 * 
	 * <p>
	 * The <tt>IXxxDomainObject.class</tt> <i>interface</i> of a programming 
	 * model is used to identify the adapter.
	 * 
	 * <p>
	 * For example, to obtain an IExtendedDomainObject for someDomainObject, use:
	 * <pre>
	 * IDomainObject<T> dobj = ...;
	 * IExtendedDomainObject<T> edobj = dobj.getAdapter(IExtendedDomainObject.class); 
	 * </pre>
	 *   
	 * <p>
	 * This is an instance of the Extension Object pattern, used widely
	 * throughout the Eclipse Platform under the name of an "adapter" (hence
	 * our choice of name).
	 * 
	 * @param <V>
	 * @param adapterClass - class of the adapter that is required.  
	 * @return
	 */
	public <V> V getAdapter(Class<V> adapterClass);

	
	//////////////////////////////////////////////////////////////////////////
	// IObjectXxx member class definitions 
	//////////////////////////////////////////////////////////////////////////


	/**
	 * Relevant to all handles to members of an instantiated object.
	 */
	public interface IObjectMember {
		

	}
	
	//////////////////////////////////////////////////////////////////////////
	// attribute
	

	/**
	 * Provides access or other interactions with the current value 
	 * of an attribute of an instantiated {@link IDomainObject}, along with
	 * support for handling prerequisites.
	 * 
	 * <p>
	 * The "other state" includes whether this attribute's prerequisites have 
	 * been met such that it can be used (ie edited).
	 * 
	 * <h3>Extended semantics</h3> 
	 * <p>
	 * The prerequisites support is part of the extended semantics.
	 */
	public interface IObjectAttribute extends IObjectMember {

		/**
		 * The owning {@link IDomainObject} for this representation of an
		 * attribute.
		 * 
		 * @param <T>
		 * @return
`		 */
		public IDomainObject<?> getDomainObject();
		
		/**
		 * The underlying {@link IDomainClass.IAttribute} to which this relates.
		 * 
		 * <p>
		 * The EMF EAttribute can be obtained in turn from this.
		 * 
		 * @return
		 */
		public IDomainClass.IAttribute getAttribute();
		
		/**
		 * Gets the current value of this attribute.
		 * 
		 * @return
		 */
		public Object get();

		/**
		 * Sets a new value for this attribute.
		 * 
		 * @param newValue
		 */
		public void set(Object newValue);
	
		/**
		 * Returns the binding for the current runtime environment (client, 
		 * server, test etc) providing additional functionality relevant to
		 * that environment.
		 *  
		 * @return
		 */
		public IObjectAttributeRuntimeBinding getBinding();


	}

	//////////////////////////////////////////////////////////////////////////
	// reference
	//////////////////////////////////////////////////////////////////////////
	
	/**
	 * Provides access or other interactions with the current value of a 
	 * 1:1 or collection reference of an instantiated 
	 * {@link IDomainObject}m along with support for prerequisites - whether a 
	 * reference can be used and/or modified. 
	 * 
	 * <p>
	 * This interface is never instantiated directly; instead any instance 
	 * will be of the sub-interfaces {@link IObjectOneToOneReference} or 
	 * {@link IObjectCollectionReference}).
	 * 
	 * <h3>Extended semantics</h3> 
	 * <p>
	 * Prerequisites are part of the extended semantics for the reference.  
	 * Because a reference can be either simple (single-valued) or a collection
	 * (multi-valued), the prerequisite methods vary:
	 * <ul>
	 * <li> In the case of a single-valued reference, the programming model 
	 *      defines two methods, <code>getXxxPre()</code> and 
	 *      <code>setXxxPre()</code>.  These represent the accessor and mutator 
	 *      prerequisites respectively.  This is very similar to attributes.
	 *      In terms of <i>this</i> class, these are represented in turn by
	 *      {@link #accessorPrerequisitesFor()} and
	 *      {@link #mutatorPrerequisitesFor(Object)} .
	 * <li> In the case of a mult-valued reference, the programming model
	 *      defines three methods.  As before <code>getXxxPre()</code> is the
	 *      accessor prerequisite and corresponds to {@link #accessorPrerequisitesFor()}
	 *      in this class.  However, for modifying the reference (collection), 
	 *      we must distinguish between adding to and removing from.  In the
	 *      programming model these correspond to <code>addToXxxPre(..)</code>
	 *      and <code>removeFromXxxPre(..)</code>.  In terms of this class they
	 *      correspond to simply {@link #mutatorPrerequisitesFor(Object, boolean)},
	 *      where the <code>boolean</code> indicates whether the collection is
	 *      being added to (<code>true</code>) or removed from (<code>false</code>).
	 * </ul>
	 * 
	 */
	public interface IObjectReference extends IResolvable, IObjectMember {

		/**
		 * The owning {@link IDomainObject} for this representation of an
		 * reference.
		 * 
		 * @param <T>
		 * @return
		 */
		public <T> IDomainObject<T> getDomainObject();
		
		/**
		 * The underlying {@link EReference} in EMF to which this relates.
		 * 
		 * @return
		 */
		public IDomainClass.IReference getReference();

	}


	/**
	 * Provides access or other interactions with the current value and other
	 * state of a 1:1 reference of an instantiated {@link IDomainObject}.
	 */
	public interface IObjectOneToOneReference extends IObjectReference {

		/**
		 * Returns the domain object for this representation of a 1:1 reference
		 * of the domain object. 
		 */
		public <Q> IDomainObject<Q> get();

		
		/**
		 * Makes the reference be set to a new referenced object (possibly null).
		 * 
		 * <p>
		 * Will check if the value is <code>null</code> or not; if it isn't 
		 * <code>null</code>, then will invoke the associator, if it is 
		 * <code>null</code> then it will invoke the dissociator. 
		 * 
		 * <p>
		 * Any {@link IReferenceListener}s will be notified.
		 * 
		 * @param collection
		 * @param domainObject
		 */
		public <Q> void set(IDomainObject<Q> domainObject);

		
		
		/**
		 * Returns the binding for the current runtime environment (client, 
		 * server, test etc) providing additional functionality relevant to
		 * that environment.
		 *  
		 * @return
		 */
		public IObjectOneToOneReferenceRuntimeBinding getBinding();

	}
	

	/**
	 * Provides access or other interactions with the current value and other
	 * state of a collection reference of an instantiated {@link IDomainObject}.
	 */
	public interface IObjectCollectionReference extends IObjectReference {

		/**
		 * Returns the contents of this representation of a collection of the
		 * domain object.
		 * 
		 * <p>
		 * The returned collection is immutable.
		 * 
		 * @param reference
		 * @return
		 */
		public <V> Collection<IDomainObject<V>> getCollection();

		/**
		 * Adds the domain object to this reference, presumed to be a 
		 * collection.
		 * 
		 * <p>
		 * Any {@link ITransactionListener}s will be notified.
		 * 
		 * @param collection
		 * @param domainObject
		 */
		public <Q> void addToCollection(IDomainObject<Q> domainObject);

		/**
		 * Removes the domain object from this reference, presumed to be a
		 * collection.
		 * 
		 * <p>
		 * Any {@link IReferenceListener}s will be notified.
		 * 
		 * @param collection
		 * @param domainObject
		 */
		public <Q> void removeFromCollection(IDomainObject<Q> domainObject);

		
		/**
		 * Returns the binding for the current runtime environment (client, 
		 * server, test etc) providing additional functionality relevant to
		 * that environment.
		 *  
		 * @return
		 */

		public IObjectCollectionReferenceRuntimeBinding getBinding();

	}
	

	//////////////////////////////////////////////////////////////////////////
	// operation
	//////////////////////////////////////////////////////////////////////////

	/**
	 * Provides access or other interactions with the current state
	 * of an invokation of an operation of an instantiated 
	 * {@link IDomainObject}, along with support for its prerequisites.
	 * 
	 * <p>
	 * By state, we mean that an operation may be values for its parameters,  
	 * and whether its prerequisites have been met such that it can be used
	 * (ie invoked).
	 * 
	 * <h3>Extended semantics</h3> 
	 * <p>
	 * TODO: move stuff to client-side bindings
	 * 
	 */
	public interface IObjectOperation extends IObjectMember {
		
		/**
		 * The owning {@link IDomainObject} for this representation of an
		 * operation.
		 * 
		 * @param <T>
		 * @return
		 */
		public <T> IDomainObject<T> getDomainObject();
		
		/**
		 * The underlying {@link EOperation} in EMF to which this relates.
		 * 
		 * @return
		 */
		public IDomainClass.IOperation getOperation();
		

		/**
		 * Returns the binding for the current runtime environment (client, 
		 * server, test etc) providing additional functionality relevant to
		 * that environment.
		 *  
		 * @return
		 */
		public IObjectOperationRuntimeBinding getBinding();
	}


}
