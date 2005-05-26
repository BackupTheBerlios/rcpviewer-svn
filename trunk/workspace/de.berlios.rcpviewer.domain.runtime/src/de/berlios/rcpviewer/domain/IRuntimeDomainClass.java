package de.berlios.rcpviewer.domain;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

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
	 * May be null if this is a write-only attribute.
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

}
