package de.berlios.rcpviewer.progmodel.extended;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.authorization.IAuthorizationManager;
import de.berlios.rcpviewer.domain.IDomainObjectAdapter;
import de.berlios.rcpviewer.session.IExtendedDomainObjectAttributeListener;
import de.berlios.rcpviewer.session.IExtendedDomainObjectOperationListener;
import de.berlios.rcpviewer.session.IExtendedDomainObjectReferenceListener;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IObservedFeature;

public interface IExtendedDomainObject<T> extends IDomainObjectAdapter<T> {

	public IExtendedRuntimeDomainClass<T> getExtendedRuntimeDomainClass();

	public interface IExtendedFeature extends IObservedFeature {
		
		/**
		 * Prerequisites applicable to view/edit this attribute according to the
		 * configured {@link IAuthorizationManager}.
		 * 
		 * @param eAttribute
		 * @return
		 */
		public IPrerequisites authorizationPrerequisitesFor();

	}

	/**
	 * Run-time handle to extended properties of an attribute for a specific 
	 * {@link IDomainObjectAdapter}.
	 * 
	 * @author Dan Haywood
	 */
	public interface IExtendedAttribute extends IExtendedFeature {

		/**
		 * The {@link EAttribute} that this is a runtime representation of
		 * with respect to a specific {@link IExtendedDomainObject}.
		 * 
		 * @return
		 */
		public EAttribute getEAttribute();
		
		/**
		 * Prerequisites applicable to access this attribute.
		 * 
		 * <p>
		 * In the programming model, the prerequisites 
		 * corresponds to the {@link de.berlios.rcpviewer.progmodel.extended.IPrerequisites} 
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
		 * corresponds to the {@link de.berlios.rcpviewer.progmodel.extended.IPrerequisites} 
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
		 * @param candidateValue - the new value that this attribute can be allowed to take, if meets prerequisites.
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue);

		/**
		 * Register interest in whether the (accessor) prerequisites or other 
		 * relevant state of this attribute changes.
		 * 
		 * 
		 * @param <T>
		 * @param listener
		 * @return
		 */
		public <T extends IExtendedDomainObjectAttributeListener> T addExtendedDomainObjectAttributeListener(T listener);

		/**
		 * Deregister interest in the state of this attribute.
		 * 
		 * @param listener
		 */
		public void removeExtendedDomainObjectAttributeListener(IExtendedDomainObjectAttributeListener listener);
	}

	/**
	 * Runtime handle on a single- or multi-valued reference for a specific
	 * {@link IExtendedDomainObject}.
	 * 
	 * <p>
	 * The principal functionality provided byh this extension is support for
	 * prerequisites - whether a reference can be used and/or modified.
	 * 
	 * <p>
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
	 * @author Dan Haywood
	 *
	 */
	public interface IExtendedReference extends IExtendedFeature {
		
		/**
		 * The {@link EReference} that this is a runtime representation of
		 * with respect to a specific {@link IExtendedDomainObject}.
		 * 
		 * @return
		 */
		public EReference getEReference();
		
		/**
		 * Prerequisites applicable to access this reference.
		 * 
		 * <p>
		 * In the programming model, the prerequisites 
		 * corresponds to the {@link de.berlios.rcpviewer.progmodel.extended.IPrerequisites} 
		 * returned by the <code>getXxxPre()</code> method. Note there may also be
		 * prerequisites corresponding to whether the reference can be 
		 * modified / added to / removed from.
		 * 
		 * <p>
		 * In addition, there may be authorization prerequisites, see
		 * {@link #authorizationPrerequisitesFor()}.
		 * 
		 * @param attribute
		 */
		public IPrerequisites accessorPrerequisitesFor();

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
		 * corresponds to the {@link de.berlios.rcpviewer.progmodel.extended.IPrerequisites} 
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
		 * @param candidateValue - the new value that this attribute can be allowed to take, if meets prerequisites.
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue);

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
		 * corresponds to the {@link de.berlios.rcpviewer.progmodel.extended.IPrerequisites} 
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
		 * @param candidateValue - the new value that this attribute can be allowed to take, if meets prerequisites.
		 */
		public IPrerequisites mutatorPrerequisitesFor(final Object candidateValue, boolean beingAdded);

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
		 * @param candidateValue
		 * @return
		 */
		public IPrerequisites prerequisitesFor(final Object candidateValue);

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
		 * @param candidateValue
		 * @param beingAdded - true if being add to the collection, false if being removed. 
		 * @return
		 */
		public IPrerequisites prerequisitesFor(final Object candidateValue, boolean beingAdded);

		/**
		 * Register interest in whether the (accessor) prerequisites or other 
		 * relevant state of this reference changes.
		 * 
		 * 
		 * @param <T>
		 * @param listener
		 * @return
		 */
		public <T extends IExtendedDomainObjectReferenceListener> T addExtendedDomainObjectReferenceListener(T listener);

		/**
		 * Deregister interest in the state of this reference.
		 * 
		 * @param listener
		 */
		public void removeExtendedDomainObjectReferenceListener(IExtendedDomainObjectReferenceListener listener);

	}

	public interface IExtendedOperation extends IExtendedFeature {
		
		/**
		 * The {@link EOperation} that this is a runtime representation of
		 * with respect to a specific {@link IExtendedDomainObject}.
		 * 
		 * @return
		 */
		public EOperation getEOperation();
		
		/**
		 * The pending arguments to be invoked for this operation and with which
		 * the prerequisites will be evaluated.
		 * 
		 * @return
		 */
		public Object[] getArgs();

		/**
		 * Specify an argument with which to invoke this operation.
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
		 * corresponds to the {@link de.berlios.rcpviewer.progmodel.extended.IPrerequisites} 
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
		 */
		public Object[] reset();

		/**
		 * Register interest in whether the prerequisites (for the currently
		 * held arguments) or other relevant state of this operation changes.
		 * 
		 * 
		 * @param <T>
		 * @param listener
		 * @return
		 */
		public <T extends IExtendedDomainObjectOperationListener> T addExtendedDomainObjectOperationListener(T listener);

		/**
		 * Deregister interest in the state of this operation.
		 * 
		 * @param listener
		 */
		public void removeExtendedDomainObjectOperationListener(IExtendedDomainObjectOperationListener listener);
	}

	/**
	 * Returns the extended attribute represented by this EMF attribute, 
	 * ensuring that the {@link ISession} containing the domain object is 
	 * notified of this attribute (as an {@link IObservedFeature}).
	 *  
	 * @param eAttribute
	 * @return
	 */
	public IExtendedAttribute getAttribute(EAttribute eAttribute);

	/**
	 * Returns the extended reference represented by this EMF reference, 
	 * ensuring that the {@link ISession} containing the domain object is 
	 * notified of this reference (as an {@link IObservedFeature}).
	 *  
	 * @param eAttribute
	 * @return
	 */
	public IExtendedReference getReference(EReference eReference);
	
	/**
	 * Returns the extended operation represented by this EMF operation, 
	 * ensuring that the {@link ISession} containing the domain object is 
	 * notified of this operation (as an {@link IObservedFeature}).
	 *  
	 * @param eAttribute
	 * @return
	 */
	public IExtendedOperation getOperation(EOperation eOperation);
	
	
}