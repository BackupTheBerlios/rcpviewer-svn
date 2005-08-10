package de.berlios.rcpviewer.session;

import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.RuntimeDomain;

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
 * notified of changes through the {@link IDomainObjectListener} interface. 
 * 
 * <p>
 * TODO: should validate the EOperations and EAttributes, just as we do
 * for IDomainClass.
 * 
 * @author Dan Haywood
 */
public interface IDomainObject<T> {

	/**
	 * Provides access or other interactions with the current value and other
	 * state of an attribute of an instantiated {@link IDomainObject}.
	 * 
	 * <p>
	 * The "other state" includes whether this attribute's prerequisites have 
	 * been met such that it can be used (ie edited).
	 */
	public interface IAttribute {

		/**
		 * The {@link IDomainObject} for which represents the state of one of 
		 * its attributes.
		 * 
		 * @param <T>
		 * @return
		 */
		public <T> IDomainObject<T> getDomainObject();
		
		/**
		 * The underlying {@link EAttribute} in EMF to which this relates.
		 * @return
		 */
		public EAttribute getEAttribute();
		
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
		 * Used internally and by aspects.
		 * 
		 * <p>
		 * Do not use otherwise.
		 * 
		 * @param attribute
		 * @param newValue
		 */
		public void notifyAttributeListeners(Object newValue);

		<T extends IDomainObjectAttributeListener> T  addDomainObjectAttributeListener(T listener);
		void removeDomainObjectAttributeListener(IDomainObjectAttributeListener listener);
		
	}
	
	/**
	 * Provides access or other interactions with the current value and other
	 * state of a reference (or collection) of an instantiated 
	 * {@link IDomainObject}.
	 * 
	 * <p>
	 * The "other state" includes whether this reference's prerequisites have 
	 * been met such that it can be used (ie edited).
	 */
	public interface IReference {

		/**
		 * Returns an immutable collection,
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
		 * Any {@link IDomainObjectListener}s will be notified.
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
		 * Any {@link IDomainObjectListener}s will be notified.
		 * 
		 * @param collection
		 * @param domainObject
		 */
		public <Q> void removeFromCollection(IDomainObject<Q> domainObject);

		<T extends IDomainObjectReferenceListener> T addDomainObjectReferenceListener(T listener);
		void removeDomainObjectReferenceListener(IDomainObjectReferenceListener listener);

	}
	
	/**
	 * Provides access or other interactions with the current state
	 * of an invokation of an operation of an instantiated 
	 * {@link IDomainObject}.
	 * 
	 * <p>
	 * By state, we mean that an operation may be values for its parameters,  
	 * and whether its prerequisites have been met such that it can be used
	 * (ie invoked).
	 * 
	 */
	public interface IOperation {
		/**
		 * The {@link IDomainObject} for which represents the state of one of 
		 * its operations.
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
		public EOperation getEOperation();
		

		/**
		 * Invoke the operation, applying any preconditions before hand.
		 * 
		 * @param operation
		 * @return the return value from the operation (if not void).
		 */
		public Object invokeOperation(Object[] args);
		
		<T extends IDomainObjectOperationListener> T addDomainObjectOperationListener(T listener);
		void removeDomainObjectOperationListener(IDomainObjectOperationListener listener);
	}
	
	public IRuntimeDomainClass<T> getDomainClass();
	
	public T getPojo();
	
	/**
	 * Whether this object has been persisted.
	 * @return
	 */
	public boolean isPersistent();
	
	/**
	 * Persist this object (for the first time).
	 * 
	 * <p>
	 * Any {@link IDomainObjectListener}s of the object will be notified.
	 *  
	 * @throws IllegalStateException if already persisted.
	 */
	public void persist();
	
	/**
	 * Save this already persisted object.
	 * 
	 * @throws IllegalStateException if not yet persisted.
	 */
	public void save();
	
	/**
	 * Distinguishable representation of the domain object in the UI.
	 * 
	 * <p>
	 * TODO: should this be required to be unique.  If not, how
	 * 
	 * @return
	 */
	public String title();

	/**
	 * Convenience method that should return the same as the 
	 * corresponding method in {@link IDomainClass}.
	 * 
	 * @param attributeName
	 * @return
	 */
	public EAttribute getEAttributeNamed(String attributeName);

	/**
	 * Convenience method that should return the same as the 
	 * corresponding method in {@link IDomainClass}.
	 * 
	 * @param operationName
	 * @return
	 */
	public EOperation getEOperationNamed(String operationName);

	/**
	 * Convenience method that should return the same as the 
	 * corresponding method in {@link IDomainClass}.
	 * 
	 * @param operationName
	 * @return
	 */
	public EReference getEReferenceNamed(String referenceName);

	/**
	 * Adds domain object listener.
	 * 
	 * <p>
	 * If the listener is already known, does nothing.
	 * 
	 * <p>
	 * Note: we return the listener only because it slightly simplfies the
	 * implementation of tests.
	 * 
	 * @param listener
	 */
	public <T extends IDomainObjectListener> T addDomainObjectListener(T listener);

	
	/**
	 * Removes domain object listener.
	 * 
	 * <p>
	 * If the listener is unknown, does nothing.
	 * 
	 * @param listener
	 */
	public void removeDomainObjectListener(IDomainObjectListener listener);

	
	/**
	 * The id of the {@link ISession} that initially managed this session
	 * (if any).
	 * 
	 * <p>
	 * Attempting to attach a domain object to a session with a different Id
	 * will fail.
	 * 
	 * @return
	 */
	public String getSessionId();

	/**
	 * Clears the session identifier.
	 *
	 * <p>
	 * Normally the session identifier of a domain object is never changed, 
	 * representing the id of the {@link ISession} that originally managed
	 * the domain object.  Even if a domain object is detached from that 
	 * session, the session identifier is retained so that - under normal
	 * circumstances - the domain object may only be re-attached to the same
	 * session.
	 * 
	 * <p>
	 * However, if an object has been detached from a session then it is 
	 * possible using this method to clear this session id, thereby allowing
	 * the domain object to be attached to some other {@link ISession}, 
	 * providing that this new session references to the same {@link RuntimeDomain}.
	 * This capability may be useful for "what-if" analysis and the like.
	 * 
	 * @throws IllegalStateException - if currently attached.
	 */
	public void clearSessionId();

	/**
	 * Whether this domain object is currently attached to a {@link ISession}.
	 * 
	 * <p>
	 * If so, then {#getSession()} will return a non-null result.
	 *  
	 * @return
	 */
	public boolean isAttached();

	/**
	 * The {@link ISession} to which this domain object is currently attached.
	 * 
	 * @return
	 */
	public ISession getSession();
	
	/**
	 * Returns an adapter for this object with respect to the adapter of some
	 * programming model.
	 * 
	 * <p>
	 * The XxxDomainObject.class of a programming model is used to identify the
	 * adapter.
	 * 
	 * <p>
	 * For example, to obtain an IExtendedDomainObject for someDomainObject, use:
	 * <code>
	 * IDomainObject<T> dobj = ...;
	 * IExtendedDomainObject<T> edc = dobj.getAdapter(IExtendedDomainObject.class); 
	 * </code>
	 *   
	 * @param <V>
	 * @param pojoClass
	 * @return
	 */
	public <V> V getAdapter(Class<V> domainObjectClass);


	/**
	 * Returns an {@link IAttribute} such that the run-time state of this
	 * attribute of the owning {@link IDomainObject} can be interacted with.
	 * 
	 * @param eAttribute
	 * @return
	 */
	public IAttribute getAttribute(EAttribute eAttribute);

	/**
	 * Returns an {@link IReference} such that the run-time state of this
	 * reference of the owning {@link IDomainObject} can be interacted with.
	 * 
	 * @param eReference
	 * @return
	 */
	public IReference getReference(EReference eReference);

	/**
	 * Returns an {@link IOperation} such that the run-time state of this
	 * operation of the owning {@link IDomainObject} can be interacted with.
	 * 
	 * @param eOperation
	 * @return
	 */
	public IOperation getOperation(EOperation eOperation);

}
