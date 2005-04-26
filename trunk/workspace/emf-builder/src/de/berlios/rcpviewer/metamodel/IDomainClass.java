package de.berlios.rcpviewer.metamodel;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;

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
public interface IDomainClass<T> {

	public Class<T> getJavaClass();
	
	public EClass getEClass();

	public int getNumberOfAttributes();

	/**
	 * Convenience method returning a type-safe iterable List over all 
	 * EAttributes known to  the underlying EClass.
	 * 
	 * @return
	 */
	public List<EAttribute> allAttributes();
	
	/**
	 * Convenience method returning a type-safe iterable List over EAttributes 
	 * defined within the underlying EClass.
	 * 
	 * <p>
	 * Inherited attributes are not included in the iteration.
	 * 
	 * @return
	 */
	public List<EAttribute> attributes();

	/**
	 * Since EClass (as of EMF 2.0) doesn't expose, we provide as a convenience.
	 * @param attributeName
	 * @return
	 */
	public EAttribute getEAttributeNamed(String attributeName);

	/**
	 * Whether the attribute is changeable.
	 * 
	 * <p>
	 * This is a convenience function that should return the same as
	 * <tt>eAttribute.isChangeable()</tt>.
	 *
	 * <p>
     * The definition of <i>changeable</i>, according to the EMF documentation, 
     * is as follows:
     * <p>
     * <i>A feature that is not changeable will not include a generated set 
     * method, and the reflective <tt>eSet()</tt> method will throw an 
     * exception if you try to set it. Declaring one end of a bi-directional 
     * relationship to be not changeable is a good way to force clients to 
     * always set the reference from the other end, but still provide 
     * convenient navigation methods from either end. Declaring one-way 
     * references or attributes to be not changeable usually implies that the 
     * feature will be set or changed by some other (user-written) code.</i>
     * 
	 * @param eAttribute
	 * @return whether attribute is changeable.
	 */
	public boolean isChangeable(EAttribute eAttribute);

	/**
	 * Whether the attribute is write only (has a mutator but no accessor).
	 * 
	 * <p>
	 * Since EMF does not support this flag directly, implementations are 
	 * expected to use EAnnotations to store this information on the 
	 * eAttribute.
	 * 
	 * <p>
	 * Note that there is no <tt>isReadOnly</tt> flag; it is called
	 * {@link #isChangeable(EAttribute)}.
	 * 
	 * @param attribute
	 * @return whether attribute is write only
	 */
	public boolean isWriteOnly(EAttribute eAttribute);

	/**
	 * Whether the attribute is derived (and also volatile and transient).
	 * 
	 * <p>
	 * This is a convenience function that should return the same as
	 * <tt>eAttribute.isDerived()</tt> (also <tt>eAttribute.isVolatile()</tt>
	 * and <tt>eAttribute.isTransient()</tt>).
	 *
	 * <p>
	 * In EMF, an EAttribute can be any of <i>derived</i>, <i>volatile</i> or 
	 * <i>transient</i>, the definitions of which are as follows:
	 * 
	 * <p>
     * <i>A feature that is declared volatile is generated without storage fields 
     * and with empty implementation method bodies, which you are required to 
     * fill in. Volatile is commonly used for a feature whose value is derived 
     * from some other feature, or for a feature that is to be implemented by 
     * hand using a different storage and implementation pattern.
     * 
     * <p>
     * The value of a derived feature is computed from other features, so it 
     * doesn't represent any additional object state. Framework classes, such 
     * as <tt>EcoreUtil.Copier</tt>, that copy model objects will not attempt 
     * to copy such features. The generated code is unaffected by the value of 
     * the derived flag, except for the package implementation class, which 
     * initializes the metadata for the model. Derived features are typically 
     * also marked volatile and transient.
     * 
     * <p>
     * Transient features are used to declare (modeled) data whose lifetime 
     * never spans application invocations and therefore doesn't need to be 
     * persisted. The (default XMI) serializer will not save features that are 
     * declared to be transient. Like derived, transient's only effect on the
     * generated code is the metadata initialization in the package 
     * implementation class.</i>
     * 
     * <p>
     * In reading the above, ignore the references to generated code: we are
     * not using EMF that way.  In fact, we are going the other direction, from
     * a POJO model to an EMF meta-model.  So the above definitions guide the
     * implementation of a programming model.  We've taken the view that it
     * would never make sense for these three flags to take different values.
     *
	 * @param eAttribute
	 * @return whether attribute is derived.
	 */
	public boolean isDerived(EAttribute eAttribute);

	/**
	 * Whether the attribute has a lower bound.
	 * 
	 * <p>
	 * This is a convenience function that should return the same as
	 * <tt>eAttribute.getLowerBound()</tt>.
	 *
	 * <p>
	 * The lower bound represents a minimum cardinality.  The value of
	 * lower bound, along with {@link #getUpperBound(EAttribute)}, is used to
	 * determine {@link #isRequired(EAttribute)} and 
	 * {@link #isMany(EAttribute)}.
	 *  
	 * @param attribute
	 * @return lower bound associated with attribute
	 */
	public int getLowerBound(EAttribute attribute);


	/**
	 * Whether the attribute has a upper bound.
	 * 
	 * <p>
	 * This is a convenience function that should return the same as
	 * <tt>eAttribute.getUpperBound()</tt>.  A value of -1 represents unbounded.
	 *
	 * <p>
	 * The lower bound represents a minimum cardinality.  The value of
	 * lower bound, along with {@link #getUpperBound(EAttribute)}, is used to
	 * determine {@link #isRequired(EAttribute)} and 
	 * {@link #isMany(EAttribute)}.
	 *  
	 * @param attribute
	 * @return upper bound of attribute
	 */
	public int getUpperBound(EAttribute attribute);

	/**
	 * Whether the attribute is required.
	 * 
	 * <p>
	 * This is a convenience function that should return the same as
	 * <tt>eAttribute.isRequired()</tt>.
	 *
	 * <p>
	 * Whether an attribute is required depends upon the values of its
	 * lower and upper bounds (see {@link #getLowerBound(EAttribute)} and
	 * {@link #getUpperBound(EAttribute)}, namely that both must be > 0.
	 *   
	 * @param attribute
	 * @return
	 */
	public boolean isRequired(EAttribute attribute);


	/**
	 * Whether the attribute is many (in other words, multi-valued).
	 * 
	 * <p>
	 * This is a convenience function that should return the same as
	 * <tt>eAttribute.isMany()</tt>.
	 *
	 * <p>
	 * Whether an attribute is required depends upon the values of its
	 * lower and upper bounds (see {@link #getLowerBound(EAttribute)} and
	 * {@link #getUpperBound(EAttribute)}, namely that both must be > 1.
	 *   
	 * @param attribute
	 * @return
	 */
	public boolean isMany(EAttribute attribute);

	/**
	 * Whether the EClass wrapped by this DomainClass contains the 
	 * EAttribute.
	 * 
	 * @param eAttribute
	 * @return
	 */
	public boolean containsAttribute(EAttribute eAttribute);

	/**
	 * Whether the attribute is unique; meaningful only for many-valued
	 * attributes.
	 * 
	 * <p>
	 * This is a convenience function that should return the same as
	 * <tt>eAttribute.isUnique()</tt>.
	 *
	 * <p>
     * The definition of <i>unique</i>, according to the EMF documentation, 
     * is as follows:
     * <p>
     * <i>
     * Unique only applies to multiplicity-many attributes, indicating that 
     * such an attribute may not contain multiple equal objects. References are 
     * always treated as unique.
     * </i>
     * 
	 * @param eAttribute
	 * @return whether this (multiplicity-many) attribute is unique
	 */
	public boolean isUnique(EAttribute eAttribute);


	/**
	 * Whether the attribute is ordered; meaningful only for many-valed
	 * attributes.
	 * 
	 * <p>
	 * This is a convenience function that should return the same as
	 * <tt>eAttribute.isOrdered()</tt>.
	 *
	 * <p>
     * (Haven't yet found a good definition of <i>ordered</i> in the EMF 
     * documentation, but the meaning is reasonably intuitive, I guess). 
     * 
	 * @param eAttribute
	 * @return whether this (multiplicity-many) attribute is ordered
	 */
	public boolean isOrdered(EAttribute eAttribute);

	/**
	 * Creates a still-to-be-persisted instance of a {@link IDomainObject}
	 * wrapping a pojo of the type represented by this domain class.
	 * 
	 * @return
	 */
	public IDomainObject<T> createTransient();
}
