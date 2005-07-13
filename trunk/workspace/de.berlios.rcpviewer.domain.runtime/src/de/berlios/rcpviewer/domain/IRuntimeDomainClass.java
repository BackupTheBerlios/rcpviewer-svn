package de.berlios.rcpviewer.domain;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;

/**
 * Represents a class in the meta model, akin to {@link java.lang.Class} and
 * wrapping an underlying EMF EClass.
 * 
 * <p>
 * There are a number of responsibilities that (objects implementing) this 
 * interface and the related {@link IDomainObject} interface provide over and
 * above EMF.  Specifically, IDomainClass/IDomainObject are responsible for
 * defining the choreography of interactions between the UI layer and the
 * pojos: a bunch of "know-how-tos".  The EMF meta-model mostly provides the
 * "know-whats" of the structure of classes and their attributes.
 * 
 * @author Dan Haywood
 */
public interface IRuntimeDomainClass<T> extends IDomainClass<T> {

	/**
	 * Creates a still-to-be-persisted instance of a {@link IDomainObject}
	 * wrapping a pojo of the type represented by this domain class.
	 * 
	 * <p>
	 * The object will not be attached to any {@link ISession}.  Since created
	 * objects normally should be attached, typically 
	 * {@link ISession#createTransient(IDomainClass)} (which does attach the
	 * resultant object to the session) should be used instead. 
	 * 
	 * @return
	 */
	public <T> IDomainObject<T> createTransient();

	public Class<T> getJavaClass();

	/**
	 * Obtain an arbitrary extension for this domain class, as providing by
	 * the {@link IDomainBuilder} of some programming model.
	 *
	 * <p>
	 * This method mirrors the one on {@link IDomainClass#getAdapter(Class)}.
	 * However, note that the supplied class will be an XxxDomainObject.class,
	 * rather than an XxxDomainClass.class.
	 * 
	 * <p>
	 * This is an instance of the Extension Object pattern, used widely
	 * throughout the Eclipse Platform under the name of an "adapter" (hence
	 * our choice of name).
	 * 
	 * <p>
	 * Usage:
	 * <code>
	 * SuperDuperDomainObject sddo = 
	 *     (SuperDuperDomainObject).someDomainClass.getObjectAdapterFor(someDomainObject, SuperDuperDomainObject.class);
	 * </code>
	 * 
	 * <p>
	 * The supplied domain object should have been instantiated via the domain 
	 * class upon which the method is invoked. 
	 * 
	 * @param domainObject - object for which the adapter is required
	 * @param objectAdapterClass - class of the adapter that is required. 
	 * @return object that implements said class.
	 */
	public <V> V getObjectAdapterFor(IDomainObject<T> domainObject, Class<V> objectAdapterClass);

	/**
	 * Returns the method to access the value in the underlying object. 
	 * 
	 * <p>
	 * May be null if this is a write-only attribute.
	 * 
	 * @see #getMutatorFor()
	 * @see #getAccessorOrMutatorFor()
	 * 
	 * @return
	 */
	public Method getAccessorFor(EAttribute eAttribute);


	/**
	 * Returns the method to modify the value in the underlying object. 
	 * 
	 * <p>
	 * May be null if this is a read-only attribute.
	 * 
	 * @see #getAccessorFor()
	 * @see #getAccessorOrMutatorFor()
	 * 
	 * @return
	 */
	public Method getMutatorFor(EAttribute eAttribute);


	/**
	 * Returns the method to either access or mutate the value in the 
	 * underlying object.
	 * 
	 * <p>
	 * If the accessor is present, then it will be returned; if this is a
	 * write-only attribute then the mutator will be returned.  Will never
	 * be null.
	 * 
	 * @see #getAccessorFor()
	 * @see #getMutatorFor()
	 * 
	 * @return
	 */
	public Method getAccessorOrMutatorFor(EAttribute eAttribute);

	/**
	 * Returns the method used to invoke an operation. 
	 * 
	 * @see #getAccessorFor()
	 * @see #getAccessorOrMutatorFor()
	 * 
	 * @return
	 */
	public Method getInvokerFor(EOperation operation);


	/**
	 * Returns the accessor for a reference representing either a simple 
	 * 1:1 reference or a collection.
	 * 
	 * <p>
	 * Typically {@link #isMultiple(EReference)} should be used to determine 
	 * whether the return type of the Method is a collection or a single 
	 * reference.
	 * 
	 * @param reference
	 * @return
	 */
	public Method getAccessorFor(EReference reference);

	/**
	 * Returns the "associator" for a reference representing either a simple 
	 * 1:1 reference or a collection.
	 * 
	 * <p>
	 * If the reference represents a collection ({@link #isMultiple(EReference)}),
	 * then the <i>associator</i> is an addTo method.  If it is a single reference,
	 * then it is a method named <i>associate</i>. 
	 * 
	 * <p>
	 * Associators and dissociators are optional (the accessor is not).  If
	 * neither an associator or dissociator is present, then the reference is
	 * presumed to be immutable (non changeable). 
	 * 
	 * @param reference
	 * @return
	 */
	public Method getAssociatorFor(EReference reference);

	/**
	 * Returns the "dissociator" for a reference representing either a simple 
	 * 1:1 reference or a collection.
	 * 
	 * <p>
	 * If the reference represents a collection ({@link #isMultiple(EReference)}),
	 * then the dissociator is prefixed <i>removeFrom</i> method.  If it is a 
	 * single reference, then it is a method named <i>dissociate</i>. 
	 * 
	 * <p>
	 * Associators and dissociators are optional (the accessor is not).  If
	 * neither an associator or dissociator is present, then the reference is
	 * presumed to be immutable (non changeable). 
	 * 
	 * @param reference
	 * @return
	 */
	public Method getDissociatorFor(EReference reference);

	/**
	 * The {@link IPrerequisites} that the 
	 * {@link de.berlios.rcpviewer.authorization.IAuthorizationManager} of the 
	 * domain class' {@link de.berlios.rcpviewer.domain.runtime.IRuntimeDomain}
	 * applies to this attribute.
	 * 
	 * @param attribute
	 * @return
	 */
	public IPrerequisites authorizationConstraintFor(EAttribute attribute);

}
