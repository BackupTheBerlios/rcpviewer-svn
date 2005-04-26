package de.berlios.rcpviewer.metamodel;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EAttribute;

/**
 * Encapsulates set of rules for interpreting a set of classes (M1 level
 * model) such that - for example - an EMF can be built.
 *  
 * <p>
 * Implementation note: this is a pluggable implementation because (a) it
 * it is easily tested, and (b) because it might be rather neat to be pluggable.
 * 
 * TODO: at time of writing, only the accessor stuff is in use.
 */
public interface IProgrammingModel {

	/**
	 * conforms to the signature of a getter of a value.  References to
	 * {@link DomainClass}es are treated as {@link Link}s.
	 * 
	 * @param method
	 * @return
	 */
	public boolean isAccessor(final Method method);
	
	public boolean isMutator(final Method method);

	/**
	 * assures that the supplied {@link Method} conforms to the expectations of
	 * being a getter.
	 * 
	 * @throws AssertionError otherwise
	 * @param accessor
	 */
	public void assertAccessor(final Method accessor);
	
	/**
	 * assures that the supplied Method conforms to the expectations of
	 * being a setter.
	 * @throws AssertionError otherwise
	 * @param dissociator
	 */
	public void assertMutator(final Method setter);

	/**
	 * The type of the getter (its return type).
	 * @param accessor
	 * @return
	 */
	public Class accessorType(final Method accessor);

	/**
	 * The type of the setter (the type of its first argument).
	 * @param mutator
	 * @return
	 */
	public Class mutatorType(final Method mutator);
	
	/**
	 * indicates whether supplied getter and setter are compatible, that is, that they
	 * have the same type and the same name. 
	 * @param getter
	 * @param setter
	 * @return
	 */
	public boolean isCompatible(final Method getter, final Method setter);
	
	/**
	 * Determines name of attribute from Method name (can represent either an
	 * accessor or a mutator Method).
	 * 
	 * @param accessorOrMutator
	 * @return
	 */
	public String deriveAttributeName(final Method accessorOrMutator);

	/**
	 * @see deriveAttributeName
	 * @param getterOrSetter
	 * @return
	 */
	public String deriveAttributeName(final String name);
	
	public boolean isValueType(final Class<?> clazz);
	
	public boolean isReferenceType(final Class<?> clazz);
	
	public boolean isCollectionType(final Class<?> clazz);
	
	
	/**
	 * Whether this method represents an accessor (getter) to either a simple
	 * or multiple reference of other {@link ADomainObject}s.
	 * 
	 * @param linkMethod
	 * @return
	 */
	public boolean isLink(final Method linkMethod);

	/**
	 * @param linkMethod
	 */
	public void assertLink(final Method linkMethod);
	
	/**
	 * A method which takes a single {@link ADomainObject} and returns void,
	 * and whose prefix is <code>associate</code>. 
	 * @param method
	 * @return whether this method represents an associator
	 */
	public boolean isAssociator(final Method method);
	
	/**
	 * A method which takes a single {@link ADomainObject} and returns void,
	 * and whose prefix is <code>dissociate</code>. 
	 * @param method
	 * @return whether this method represents an dissociator
	 */
	public boolean isDissociator(final Method method);
	
	public void assertAssociator(final Method associator);
	
	public void assertDissociator(final Method dissociator);
	
	/**
	 * @param associator or dissociator method
	 * @return The type of the associator or dissociator (the type of its first argument).
	 */
	public Class linkType(final Method linkMethod);
	
	/**
	 * Determines name of association from Method name (can represent any of a
	 * link method (getter), an associate method or a dissociate Method).
	 * 
	 * @param associationMethod
	 * @return
	 */
	public String deriveLinkName(final Method associationMethod);
	
	/**
	 * @see deriveLinkName
	 * @param name
	 * @return
	 */
	public String deriveLinkName(final String name);
	
	/**
	 * indicates whether supplied associator and dissociator are compatible,
	 * that is, that they have the same type and the same name. 
	 * @param associator
	 * @param dissociator
	 * @return
	 */
	public boolean isLinkPairCompatible(final Method associator, final Method dissociator);

	public boolean representsAttribute(Method method);

	public boolean isIsUnsetMethodFor(Method method, EAttribute attribute);

	public boolean isUnsetMethodFor(Method method, EAttribute attribute);
	
}
