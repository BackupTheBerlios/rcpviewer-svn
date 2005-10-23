package de.berlios.rcpviewer.domain;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.IPersistable;
import de.berlios.rcpviewer.session.IPersistable.PersistState;
import de.berlios.rcpviewer.session.IResolvable;
import de.berlios.rcpviewer.session.IResolvable.ResolveState;
import de.berlios.rcpviewer.transaction.internal.InstantiationChange;
import de.berlios.rcpviewer.transaction.ITransaction;

/**
 * Represents a class in the meta model, akin to {@link java.lang.Class} and
 * wrapping an underlying EMF EClass.
 * 
 * @author Dan Haywood
 */
public interface IRuntimeDomainClass<T> extends IDomainClass<T> {

	/**
	 * The (Java) class to which this is a peer.
	 * 
	 * <p>
	 * The domain class uses the Java class to actually instantiate instances
	 * of pojos; these are ultimately wrapped in {@link IDomainObject}s.
	 * 
	 * @return
	 */
	public Class<T> getJavaClass();

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

	/**
	 * The {@link IPrerequisites} that the 
	 * {@link de.berlios.rcpviewer.authorization.IAuthorizationManager} of the 
	 * domain class' {@link de.berlios.rcpviewer.domain.runtime.IRuntimeDomain}
	 * applies to this reference.
	 * 
	 * @param reference
	 * @return
	 */
	public IPrerequisites authorizationConstraintFor(EReference reference);

	/**
	 * The {@link IPrerequisites} that the 
	 * {@link de.berlios.rcpviewer.authorization.IAuthorizationManager} of the 
	 * domain class' {@link de.berlios.rcpviewer.domain.runtime.IRuntimeDomain}
	 * applies to this operation.
	 * 
	 * @param operation
	 * @return
	 */
	public IPrerequisites authorizationConstraintFor(EOperation operation);

	public Method getMutatorFor(EReference reference);

}
