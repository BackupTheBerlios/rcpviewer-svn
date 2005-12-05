package org.essentialplatform.runtime.domain;

import java.util.Collection;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.progmodel.essential.app.IPrerequisites;
import org.essentialplatform.runtime.authorization.IAuthorizationManager;
import org.essentialplatform.runtime.domain.event.IDomainObjectAttributeListener;
import org.essentialplatform.runtime.domain.event.IDomainObjectListener;
import org.essentialplatform.runtime.domain.event.IDomainObjectOperationListener;
import org.essentialplatform.runtime.domain.event.IDomainObjectReferenceListener;
import org.essentialplatform.runtime.persistence.IPersistable;
import org.essentialplatform.runtime.persistence.IResolvable;
import org.essentialplatform.runtime.persistence.PersistenceId;
import org.essentialplatform.runtime.session.ISession;
import org.essentialplatform.runtime.transaction.event.ITransactionListener;

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

	//////////////////////////////////////////////////////////////////////////
	// member
	
	/**
	 * Relevant to all handles to members of an instantiated object.
	 */
	public interface IObjectMember extends IObservedFeature {
		
		/**
		 * Prerequisites applicable to view/edit this attribute according to the
		 * configured {@link IAuthorizationManager}.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param eAttribute
		 * @return
		 */
		public IPrerequisites authorizationPrerequisitesFor();

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
		 * Prerequisites applicable to access this attribute.
		 * 
		 * <p>
		 * In the programming model, the prerequisites 
		 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
		 * returned by the <code>getXxxPre()</code> method.  Note there may also be
		 * prerequisites corresponding to whether the attribute can be modified
		 * (in other words validation), see {@link #mutatorPrerequisitesFor()}.
		 * In the programming model these correspond to the 
		 * <code>setXxxPre(...)</code> method.
		 * 
		 * <p>
		 * In addition, there may be authorization prerequisites, see
		 * {@link #authorizationPrerequisitesFor()}.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute
		 */
		public IPrerequisites accessorPrerequisitesFor();

		/**
		 * Convenience method that combines all prerequisites to access/modify
		 * an attribute.
		 * 
		 * <p>
		 * There are three sets of prerequisites that can apply:
		 * <ul>
		 * <li> the prerequisites to access the attribute, as defined in the 
		 *      programming model by the <code>getXxxPre()</code>; 
		 *      see {@link #accessorPrerequisitesFor()}
		 * <li> the prerequisites to modify the attribute, as defined in the
		 *      programming model by the <code>setXxxPre(...)</code>;
		 *      see {@link #mutatorPrerequisitesFor(, Object)}.
		 * <li> the prerequisites of the configured {@link IAuthorizationManager},
		 *      see {@link #authorizationPrerequisitesFor()}.
		 * </ul>
		 *  
		 * <p>
		 * If there is no mutator then the method can still be called with
		 * <code>null</code> as the candidate value.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param candidateValue
		 * @return
		 */
		public IPrerequisites prerequisitesFor(final Object candidateValue);

		/**
		 * Prerequisites applicable to modify this attribute with a specific value
		 * (in other words, validation).
		 * 
		 * <p>
		 * The prerequisites are dependent upon the candidate value for the
		 * attribute.  For example, an attribute might not accept negative values;
		 * if so then these prerequisites would effectively veto that candidate
		 * value.
		 * 
		 * <p>
		 * In the programming model, the domain object prerequisites 
		 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
		 * returned by the <code>setXxxPre(..)</code> method.  Note there will also 
		 * be prerequisites corresponding to be able to access the attribute, 
		 * see {@link #accessorPrerequisitesFor()}.  In the 
		 * programming model these correspond to the <code>getXxxPre()</code> 
		 * method.
		 * 
		 * <p>
		 * In addition, there may be authorization prerequisites, see
		 * {@link #authorizationPrerequisitesFor()}.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param candidateValue - the new value that this attribute can be allowed to take, if meets prerequisites.
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue);

		/**
		 * Register interest in changes to either the values of this attribute
		 * or whether the (accessor) prerequisites or other relevant state of 
		 * this attribute changes.
		 * 
		 * @param <T>
		 * @param listener
		 * @return
		 */
		<T extends IDomainObjectAttributeListener> T  addListener(T listener);
		
		/**
		 * Deregister interest in changes to the values or prerequisites of 
		 * this attribute.
		 * 
		 * @param listener
		 */
		void removeListener(IDomainObjectAttributeListener listener);

		/**
		 * Notify listeners that this attribute has a new value.
		 * 
		 * <p>
		 * Public so that it can be invoked by NotifyListenersAspect.
		 * 
		 * @param attribute
		 * @param newValue
		 */
		public void notifyListeners(Object newValue);


	}

	//////////////////////////////////////////////////////////////////////////
	// reference
	
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
		
		/**
		 * Register interest in changes in either the value of this reference 
		 * or in whether the (accessor) prerequisites or other relevant state 
		 * of this reference changes.
		 * 
		 * @param <T>
		 * @param listener
		 * @return
		 */
		<T extends IDomainObjectReferenceListener> T addListener(T listener);

		/**
		 * Deregister interest in changes to the value or prerequisites of 
		 * this reference.
		 * 
		 * @param listener
		 */
		void removeListener(IDomainObjectReferenceListener listener);
		
		/**
		 * Prerequisites applicable to access this reference.
		 * 
		 * <p>
		 * In the programming model, the prerequisites 
		 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
		 * returned by the <code>getXxxPre()</code> method. Note there may also be
		 * prerequisites corresponding to whether the reference can be 
		 * modified / added to / removed from.
		 * 
		 * <p>
		 * In addition, there may be authorization prerequisites, see
		 * {@link #authorizationPrerequisitesFor()}.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute
		 */
		public IPrerequisites accessorPrerequisitesFor();

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
		 * Prerequisites applicable to modify this single-valued (simple)
		 * reference with a specific reference (in other words, validation).
		 * 
		 * <p>
		 * The prerequisites are dependent upon the candidate referenced
		 * object.  For example, an reference might not accept certain objects; 
		 * if so then these prerequisites would effectively veto that candidate value.
		 * 
		 * <p>
		 * In the programming model, the domain object prerequisites 
		 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
		 * returned by the <code>setXxxPre(..)</code> method.  Note there will also 
		 * be prerequisites corresponding to be able to access the attribute, 
		 * see {@link #accessorPrerequisitesFor()}.  In the 
		 * programming model these correspond to the <code>getXxxPre()</code> 
		 * method.
		 * 
		 * <p>
		 * In addition, there may be authorization prerequisites, see
		 * {@link #authorizationPrerequisitesFor()}.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param candidateValue - the new value that this attribute can be allowed to take, if meets prerequisites.
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue);

		/**
		 * Convenience method that combines all prerequisites to access/modify
		 * a single-valued reference (that is, representing a simple reference
		 * rather than a collection).
		 * 
		 * <p>
		 * There are three sets of prerequisites that can apply to a
		 * single-valued reference:
		 * <ul>
		 * <li> the prerequisites to access the reference, as defined in the 
		 *      programming model by the <code>getXxxPre()</code>; 
		 *      see {@link #accessorPrerequisitesFor()}
		 * <li> the prerequisites to modify the reference, as defined in the
		 *      programming model by the <code>setXxxPre(...)</code>;
		 *      see {@link #mutatorPrerequisitesFor(Object)}.
		 * <li> the prerequisites of the configured {@link IAuthorizationManager},
		 *      see {@link #authorizationPrerequisitesFor()}.
		 * </ul>
		 *  
		 * <p>
		 * If there is no mutator then the method can still be called with
		 * <code>null</code> as the candidate value.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param candidateValue
		 * @return
		 */
		public IPrerequisites prerequisitesFor(final Object candidateValue);
		
		/**
		 * Notify listeners of a 1:1 reference that it the reference is now 
		 * to a new referenced object (or possibly <code>null</code>).
		 * 
		 * <p>
		 * For use by aspects, not general use.
		 * 
		 * @param newReferencedObjectOrNull
		 */
		public void notifyListeners(Object newReferencedObjectOrNull);

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
		 * Prerequisites applicable to add a reference or remove from a
		 * reference from this multi-valued reference (in other words, 
		 * validation of the contents of a collection).
		 * 
		 * <p>
		 * The prerequisites are dependent upon the candidate referenced object.
		 * For example, a collection might not accept certain objects; if so 
		 * then these prerequisites would effectively veto that candidate 
		 * reference.  Or, it might not be possible to remove an object once
		 * added.
		 * 
		 * <p>
		 * In the programming model, the domain object prerequisites 
		 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
		 * returned by the <code>addToXxxPre(..)</code> method and 
		 * <code>removeFromXxxPre(..)</code>.  Note there will also 
		 * be prerequisites corresponding to be able to access the attribute, 
		 * see {@link #accessorPrerequisitesFor()}.  In the 
		 * programming model these correspond to the <code>getXxxPre()</code> 
		 * method.
		 * 
		 * <p>
		 * In addition, there may be authorization prerequisites, see
		 * {@link #authorizationPrerequisitesFor()}.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param candidateValue - the new value that this attribute can be allowed to take, if meets prerequisites.
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue, boolean beingAdded);


		/**
		 * Convenience method that combines all prerequisites to access/modify
		 * a multi-valued reference representing a collection.
		 * 
		 * <p>
		 * There are three sets of prerequisites that can apply to a
		 * multi-valued reference:
		 * <ul>
		 * <li> the prerequisites to access the reference, as defined in the 
		 *      programming model by the <code>getXxxPre()</code>; 
		 *      see {@link #accessorPrerequisitesFor()}
		 * <li> the prerequisites to either add to or remove from the collection,
		 *      as defined in the programming model as either 
		 *      <code>addToXxxPre(...)</code> or <code>removeFromXxxPre(...)</code>.
		 *      The value of the <code>beingAdded</code> parameter is used to 
		 *      check the appropriate prerequisite; 
		 *      {@link #mutatorPrerequisitesFor(Object, boolean)}.
		 * <li> the prerequisites of the configured {@link IAuthorizationManager},
		 *      see {@link #authorizationPrerequisitesFor()}.
		 * </ul>
		 *  
		 * <p>
		 * If there is no mutator then the method can still be called with
		 * <code>null</code> as the <code>candidateValue</code>; the value of
		 * the <code>beingAdded</code> parameter is ignored.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param candidateValue
		 * @param beingAdded - true if being add to the collection, false if being removed. 
		 * @return
		 */
		public IPrerequisites prerequisitesFor(final Object candidateValue, boolean beingAdded);

		/**
		 * Notify listeners of a collection that it the reference has had 
		 * added to it a new object.
		 * 
		 * <p>
		 * Do not use otherwise.
		 * 
		 * @param referencedObject
		 * @param beingAdded - <code>true</code> if being added to, <code>false</code> if being removed.
		 */
		public void notifyListeners(Object referencedObject, boolean beingAdded);
		
	}
	

	//////////////////////////////////////////////////////////////////////////
	// operation

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
	 * The prerequisites support is part of the extended semantics.
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
		 * Invoke the operation, applying any preconditions before hand.
		 * 
		 * @param operation
		 * @return the return value from the operation (if not void).
		 */
		public Object invokeOperation(Object[] args);

		/**
		 * Register interest in whether this operation is invoked or in
		 * whether the prerequisites (for the currently held arguments) or 
		 * other relevant state of this operation changes.
		 * 
		 * <p>
		 * If the listener is already known, does nothing.
		 * 
		 * @param <T>
		 * @param listener
		 * @return
		 */
		<T extends IDomainObjectOperationListener> T addListener(T listener);
		
		/**
		 * Deregister interest in this operation and its prerequisite state.
		 * 
		 * @param listener
		 */
		void removeListener(IDomainObjectOperationListener listener);
		
		/**
		 * The pending arguments to be invoked for this operation and with which
		 * the prerequisites will be evaluated.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @return
		 */
		public Object[] getArgs();

		/**
		 * Specify an argument with which to invoke this operation.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param position - the 0-based position 
		 * @param args
		 */
		public void setArg(int position, Object arg);
		
		/**
		 * Prerequisites applicable to invoke this operation.
		 * 
		 * <p>
		 * In the programming model, the prerequisites 
		 * corresponds to the {@link org.essentialplatform.progmodel.essential.app.IPrerequisites} 
		 * returned by the <code>xxxPre(..)</code> method (where 
		 * <code>xxx(..)</code> is the name of the operation itself).
		 * 
		 * <p>
		 * The prerequisites will be evaluated against the set of arguments
		 * currently specified, where {@link #setArg(int, Object[])} is used
		 * to set and {@link #getArgs()} can be used to retrieve. 
		 * 
		 * <p>
		 * In addition, there may be authorization prerequisites, see
		 * {@link #authorizationPrerequisitesFor(EAttribute)}.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 */
		public IPrerequisites prerequisitesFor();

		/**
		 * Resets the argument list back to defaults.
		 * 
		 * <p>
		 * If a defaults method has been provided (<code>XxxDefaults(..)</code>),
		 * then it will be invoked.  Otherwise the built-in defaults for each
		 * type will be used (null for objects, 0 for int etc).
		 * 
		 * @return the reset arguments, same as {@link #getArgs()}.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 */
		public Object[] reset();
	}
	
	public IDomainClass getDomainClass();
	
	public T getPojo();
	
	/**
	 * The identifier by which the pojo wrapped by this domain object can be
	 * retrieved from the configured object store.
	 * 
	 * @return
	 */
	public PersistenceId getPersistenceId();

	// think this is redundant
//	/**
//	 * The current state of this domain object.
//	 * 
//	 * @return
//	 */
//	public TransactionalState getTransactionalState();
	
	/**
	 * Iff resolve state and persist state are both set and not to isUnknown.
	 * 
	 * @return
	 */
	public boolean isInitialized();
	
	/**
	 * Allows the object store to assign a persistence Id.
	 * 
	 * <p>
	 * Note that this is not configured using
	 * {@link #init(IDomainClass, ISession, PersistState, ResolveState)}.
	 * Instead, it will be derived from the values set directly by the
	 * application (application-assigned), or it will be set by the object store
	 * (objectstore-assigned).
	 * 
	 * @param persistenceId
	 */
	public void assignPersistenceId(PersistenceId persistenceId);

	
	/**
	 * Whether this object has been persisted.
	 * @return
	 */
	public boolean isPersistent();

	// commented out cos now done through transactions...
//	/**
//	 * Persist this object (for the first time).
//	 * 
//	 * <p>
//	 * Any {@link ITransactionListener}s of the object will be notified.
//	 *  
//	 * @throws IllegalStateException if already persisted.
//	 */
//	public void persist();
//	
//	/**
//	 * Save this already persisted object.
//	 * 
//	 * @throws IllegalStateException if not yet persisted.
//	 */
//	public void save();
//	
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
	public <T extends IDomainObjectListener> T addListener(T listener);

	
	/**
	 * Removes domain object listener.
	 * 
	 * <p>
	 * If the listener is unknown, does nothing.
	 * 
	 * @param listener
	 */
	public void removeListener(IDomainObjectListener listener);

	
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
	 * providing that this new session references to the same {@link Domain}.
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

	
	public void externalStateChanged();
}
