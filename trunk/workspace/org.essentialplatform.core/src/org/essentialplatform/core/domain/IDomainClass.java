package org.essentialplatform.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.deployment.IAttributeBinding;
import org.essentialplatform.core.deployment.IBinding;
import org.essentialplatform.core.deployment.IDomainClassBinding;
import org.essentialplatform.core.deployment.IOperationBinding;
import org.essentialplatform.core.deployment.IReferenceBinding;
import org.essentialplatform.core.domain.adapters.IAdapterFactory;
import org.essentialplatform.core.domain.adapters.IDomainClassAdapter;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.core.domain.filters.IFilter;
import org.essentialplatform.core.domain.validators.IValidator;
import org.essentialplatform.core.features.IFeatureId;
import org.essentialplatform.core.i18n.II18nData;
import org.essentialplatform.progmodel.essential.app.AssignmentType;
import org.essentialplatform.progmodel.essential.app.BusinessKey;
import org.essentialplatform.progmodel.essential.app.FieldLengthOf;
import org.essentialplatform.progmodel.essential.app.Id;
import org.essentialplatform.progmodel.essential.app.ImmutableOncePersisted;
import org.essentialplatform.progmodel.essential.app.Invisible;
import org.essentialplatform.progmodel.essential.app.Mask;
import org.essentialplatform.progmodel.essential.app.MaxLengthOf;
import org.essentialplatform.progmodel.essential.app.MinLengthOf;
import org.essentialplatform.progmodel.essential.app.MultiLine;
import org.essentialplatform.progmodel.essential.app.Named;
import org.essentialplatform.progmodel.essential.app.Optional;
import org.essentialplatform.progmodel.essential.app.Regex;


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
public interface IDomainClass {


	/////////////////////////////////////////////////////////////////////
	
	/**
	 * Represents a feature of this class.
	 * 
	 * @author Dan Haywood
	 *
	 */
	public interface IMember {
		
		/**
		 * The containing {@link IDomainClass} of this member.
		 * 
		 * @return
		 */
		IDomainClass getDomainClass();


		/**
		 * The name of this member.
		 * 
		 * @return
		 */
		String getName();

		
		/**
		 * Returns a feature identifier for the supplied member.
		 * 
		 * @return
		 */
		public IFeatureId getFeatureId();
		
	}

	/**
	 * The {@link Domain} that (conceptually) owns this domain class.
	 * 
	 * <p>
	 * There could potentially be several {@link Domain}s instantiated at
	 * a time.  For example, one might model the main business object domain,
	 * while another might model a set of user preferences, and a third might
	 * model the user's filesystem.
	 * 
	 * @return the owning domain for this domain class.
	 */
	public IDomain getDomain();
	
	/**
	 * Returns either the name of the class according to the {@link @Named}
	 * annotation, or just the name of the underlying {@link EClass} if no
	 * such annotation exists.
	 *
	 * <p>
	 * Any renaming of the class, eg by the <tt>Named</tt> annotation of the
	 * extended programming model, is ignored.  
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Underlying EMF {@link EClass} that holds the structural features and
	 * information about this class.
	 * 
	 * @return
	 */
	public EClass getEClass();


	/**
	 * Abbreviation for this class.
	 * 
	 * <p>
	 * In the standard programming model, provided by 
	 * <tt>@InDomain(abbreviation)</tt>.
	 * 
	 * @return
	 */
	public String getAbbreviation();


	/**
	 * The (binding-specific) representation of this class.
	 * 
	 * <p>
	 * For example, in a runtime-context would be a <tt>java.lang.Class</tt>.
	 * In a compile-time context, would be an AST node.
	 *
	 * <p>
	 * If the binding context is known (which it usually is), then downcasting
	 * this getter is usually more convenient than going to the binding for
	 * a type-safe equivalent.
	 * 
	 * <p>
	 * <b>Implementations should hold this reference using a thread-static,
	 * because different bindings may be in place on different threads</b>. 
	 * 
	 * @return
	 */
	public Object getClassRepresentation();


	/**
	 * The name of the underlying POJO class.
	 * 
	 * <p>
	 * Typically the same as {~link #Name()}, unless the class has been 
	 * explicitly renamed with the {@link Named} annotation.
	 *  
	 * @return
	 */
	public String getEClassName();

	/**
	 * Returns the binding of this class to {@link Binding}.
	 * 
	 * JAVA5_FIXME: fix return type.
	 * 
	 * <p>
	 * <b>Implementations should hold this reference using a thread-static,
	 * because different bindings may be in place on different threads</b>. 
	 * 
	 * @return
	 */
	public <V extends IDomainClassBinding> V getBinding();


	/**
	 * To be called by the platform only.
	 * 
	 * @return
	 */
	public <V extends IDomainClassBinding> void setBinding( V binding);

	/**
	 * Whether this class overall is immutable, meaning that non of its
	 * attributes may be edited.
	 * 
	 * <p>
	 * Operations may still be invoked, however.
	 * 
	 * @return
	 */
	public boolean isChangeable();

	
	/**
	 * Returns the description of this class (if any).
	 * 
	 * <p>
	 * The standard programming model uses the <tt>DescribedAs</tt> annotation.
	 * 
	 * @return
	 */
	public String getDescription();


	/**
	 * Returns the internalizationed data of this class.
	 * 
	 * <p>
	 * Provides access to the key for the internalization name, description and
	 * any other internationalizatrion information.
	 * 
	 * @return
	 */
	public II18nData getI18nData();



	/**
	 * Whether this domain class cannot be persisted.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @return
	 */
	public boolean isTransientOnly();

	/**
	 * Returns a map keyed by name of each business key, whose value is a list
	 * of the attribute(s) that make up that business key, in the order that
	 * they were specified.
	 * 
	 * <p>
	 * The {@link BusinessKey} annotation is used to indicate which attributes
	 * are in a business key.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @return
	 */
	public Map<String, List<IDomainClass.IAttribute>> businessKeys();

	/**
	 * Whether the identifier for this class consists of a single attribute.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @return
	 */
	public boolean isSimpleId();
	
	/**
	 * Whether the identifier for this class consists of multiple attributes.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @return
	 */
	public boolean isCompositeId();

	/**
	 * The assignment type (application or objectstore etc) in effect for
	 * this domain class.
	 * 
	 * <p>
	 * Dependent upon whether the id is composite or simple, if integral, and
	 * finally on the <tt>@Id</tt> annotation's <tt>assignedBy</tt> property. 
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @see Id
	 * @return
	 */
	public AssignmentType getIdAssignmentType();
	
	/**
	 * Whether this class may be searched for by the UI in some generic
	 * search mechanism, eg Search.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @return
	 */
	public boolean isSearchable();

	/**
	 * Whether this class can be instantiated generically, eg File>New.
	 * 
	 * <p>
	 * The majority of domain classes are expected to be instantiable.
	 * 
	 * <p>
	 * In programming model: <code>@InDomain(instantiable=false)</code>.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @return
	 */
	public boolean isInstantiable();

	/**
	 * Whether instances of this class can be persisted, eg File>Save.
	 * 
	 * <p>
	 * The majority of domain classes are expected to not be directly 
	 * persistable.
	 * 
	 * <p>
	 * In programming model: <code>@InDomain(nonPersistable=false)</code>.
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @return
	 */
	public boolean isSaveable();

	
	/**
	 * Whether all attributes in the class are by default immutable once 
	 * persisted.
	 * 
	 * <p>
	 * Note that even if this method returns <code>true</code>, it is still
	 * possible for an individual attribute to be mutable if it opts out.  To
	 * determine whether any given attribute is immutable once persisted, use
	 * {@link #isImmutableOncePersisted(EAttribute)}. 
	 * 
	 * <p>
	 * Extended semantics. 
	 * 
	 * @return true if all attributes for the class should be taken to be 
	 *         immutable once persisted unless they explicitly have opted out.
	 */
	public boolean isImmutableOncePersisted();

	///////////////////////////////////////////////////////////////
	// attribute
	

	/**
	 * Returns all the {@link IDomainClass.IAttribute}s of the class, 
	 * including inherited attributes; the order is indeterminate.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IAttribute> iAttributes();
	

	/**
	 * Returns all the {@link IDomainClass.IAttribute}s of the class, 
	 * including inherited attributes, in the order specified by the supplied
	 * {@link Comparator}.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IAttribute> iAttributes(final Comparator<IAttribute> comparator);

	
	/**
	 * Returns the {@link IDomainClass.IAttribute}s of the class that are
	 * satisfied by the supplied filter.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IAttribute> iAttributes(final IFilter<IAttribute> filter);

	
	/**
	 * Returns the {@link IDomainClass.IAttribute}s of the class that are
	 * satisfied by the supplied filter; the order is indeterminate.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IAttribute> iAttributes(final IFilter<IAttribute> filter, final Comparator<IAttribute> comparator);

	
	/**
	 * Returns the attribute with given name, or <tt>nothing</tt> if unknown.
	 * 
	 * @param attributeName
	 * @return
	 */
	public IAttribute getIAttributeNamed(String attributeName);

	/**
	 * Whether the EClass wrapped by this DomainClass contains the 
	 * IAttribute.
	 * 
	 * @param iAttribute
	 * @return
	 */
	public boolean containsIAttribute(IDomainClass.IAttribute iAttribute);

	/**
	 * Returns an {@link IAttribute} represented by the supplied 
	 * {@link EAttribute}; reference semantics.
	 *  
	 * @param eAttribute
	 * @return
	 */
	public IAttribute getIAttribute(EAttribute eAttribute);

	/**
	 * Encapsulates static semantics of an attribute of this class.
	 * 
	 * @author Dan Haywood
	 */
	public interface IAttribute extends IMember {
		
		/**
		 * The underlying {@link EAttribute} that this represents.
		 * 
		 * <p>
		 * For use only internally.
		 * 
		 * <p>
		 * TODO: add an aspect to enforce this.
		 * 
		 * @return
		 */
		EAttribute getEAttribute();

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
		 * @return whether attribute is changeable.
		 */
		public boolean isChangeable();

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
		 * @return whether attribute is write only
		 */
		public boolean isWriteOnly();

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
		 * @return whether attribute is derived.
		 */
		public boolean isDerived();

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
		 * @return lower bound associated with attribute
		 */
		public int getLowerBound();


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
		 * @return upper bound of attribute
		 */
		public int getUpperBound();

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
		 * @return
		 */
		public boolean isRequired();


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
		 * @return
		 */
		public boolean isMany();

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
		 * @return whether this (multiplicity-many) attribute is unique
		 */
		public boolean isUnique();


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
		 * @return whether this (multiplicity-many) attribute is ordered
		 */
		public boolean isOrdered();

		/**
		 * Whether the attribute is unsettable.
		 * 
		 * <p>
		 * <p>
		 * This is a convenience function that should return the same as
		 * <tt>eAttribute.isUnsettable()</tt>.
		 * 
		 * <p>
	     * The definition of <i>changeable</i>, according to the EMF documentation, 
	     * is as follows:
	     * <p>
	     * <i>
	     * A feature that is declared to be unsettable has a notion of an explicit 
	     * unset or no-value state. For example, a boolean attribute that is not 
	     * unsettable can take on one of two values: true or false. If, instead, 
	     * the attribute is declared to be unsettable, it can then have any of 
	     * three values: true, false, or unset.
	     * 
	     * <p>
	     * The get method on a feature that is not set will return its default 
	     * value, but for an unsettable feature, there is a distinction between 
	     * this state and when the feature has been explicitly set to the default 
	     * value. Since the unset state is outside of the set of allowed values, 
	     * we need to generate additional methods to put a feature in the unset 
	     * state and to determine if it is in that state. For example, if the 
	     * pages attribute in class Book is declared to be unsettable, then we'll 
	     * get two more generated methods:
	     * 
	     * <p>
	     * <code>
	     * boolean isSetPages();<br>
	     * void unsetPages();
	     * </code>
	     * 
	     * <p>
	     * in addition to the original two:
	     * <p>
	     * <code>
	     * int getPages();<br>
	     * void setPages(int value);
	     * </code>
	     * 
	     * <p>
	     * The isSet method returns true if the feature has been explicitly set. 
	     * The unset method changes an attribute that has been set back to its 
	     * unset state.
	     * 
	     * </i>
	     * 
		 * @return whether the attribute is unsettable.
		 */
		public boolean isUnsettable();


		/**
		 * Returns internalizationalized data for the specified attribute of the 
		 * domain class.
		 * 
		 * <p>
		 * The attribute must be one of those for the class. 
		 * 
		 * @return
		 */
		public II18nData getI18nData();

		/**
		 * Whether the specified attribute is part of the handle.
		 * 
		 * <p>
		 * The {@link Id} annotation is used to indicate whether an attribute
		 * is an identifier or not.
		 *  
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute - attribute of the domain class that this is an extension of
		 * @return
		 */
		public boolean isId();
		
		/**
		 * Whether the specified attribute is optional for this domain class.
		 * 
		 * <p>
		 * The {@link Optional} annotation is used to indicate whether an attribute
		 * is optional or not.
		 *  
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute - attribute of the domain class that this is an extension of
		 * @return
		 */
		public boolean isOptional();

		/**
		 * Whether the specified attribute is mandatory for this domain class.
		 * 
		 * <p>
		 * This is a convenience method; its value is defined as the logical NOT of
		 * invoking {@link #isOptional(EAttribute)}.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute - attribute of the domain class that this is an extension of
		 * @return
		 */
		public boolean isMandatory();

		/**
		 * Whether the specified attribute is invisible for this domain class.
		 * 
		 * <p>
		 * The {@link Invisible} annotation is used to indicate whether an attribute
		 * is optional or not.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute - attribute of the domain class that this is an extension of
		 * @return
		 */
		public boolean isInvisible();

		/**
		 * Returns the field length (as displayed in the UI) of the specified
		 * (string) attribute.
		 * 
		 * <p>
		 * The {@link FieldLengthOf} annotation is used to indicate the field 
		 * length of attributes.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute
		 * @return
		 */
		public int getFieldLengthOf();

		/**
		 * Returns the max length (as persisted in the persistent data store) of 
		 * the specified (string) attribute.
		 * 
		 * <p>
		 * The {@link MaxLengthOf} annotation is used to indicate the maximum 
		 * length of attributes.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute
		 * @return
		 */
		public int getMaxLengthOf();

		/**
		 * Returns the min length (as required to be entered in the UI) of 
		 * the specified (string) attribute.
		 * 
		 * <p>
		 * The {@link MinLengthOf} annotation is used to indicate the minimum 
		 * length of attributes.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute
		 * @return
		 */
		public int getMinLengthOf();

		/**
		 * Returns the number of lines to use in a text area for a
		 * (string) attribute.
		 * 
		 * <p>
		 * The {@link MultiLine} annotation is used to indicate the field 
		 * length of attributes.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute
		 * @return
		 */
		public int getMultiLine();

		/**
		 * Whether this attribute can be edited for as long as the object has not
		 * been persisted, but should be non-editable thereafter.
		 * 
		 * <p>
		 * The {@link ImmutableOncePersisted} annotation is used to indicate  
		 * whether the attribute has this semantic.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute
		 * @return
		 */
		public boolean isImmutableOncePersisted();

		/**
		 * Whether the content of this (string) attribute must be formatted
		 * according to a mask.
		 * 
		 * <p>
		 * The {@link Mask} annotation is used to indicate the mask string;
		 * returns null if none.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute
		 * @return
		 */
		public String getMask();

		/**
		 * Whether the candidate value is valid for this attribute.
		 * 
		 * <p>
		 * Used by the equivalent IDomainObject.IObjectAttribute#isValid() to 
		 * determine if the value that it holds is valid.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @return
		 */
		public boolean isValid(final String candidate);
		

		/**
		 * All validators installed for this attribute.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @return
		 */
		public List<IValidator> validators();


		/**
		 * Returns the binding of this attribute to parameterized {@link Binding}.
		 * 
		 * <p>
		 * <b>Implementations should hold this reference using a thread-static,
		 * because different bindings may be in place on different threads</b>. 
		 * 
		 * @return
		 */
		public <V extends IAttributeBinding> V getBinding();

		/**
		 * For testing purposes only.
		 * 
		 * @param binding
		 */
		public void replaceBindings(IBinding binding);

	}

	///////////////////////////////////////////////////////////////
	// reference
	

	/**
	 * Returns {@link IDomainClass.IReference}s from this class to other 
	 * classes, including those inherited.
	 * 
	 * <p>
	 * The returned list is a copy and can be safely modified by the caller.
	 */
	public List<IReference> iReferences();

	/**
	 * Returns all the {@link IDomainClass.IReference}s of the class, 
	 * including inherited references, in the order specified by the supplied
	 * {@link Comparator}.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IReference> iReferences(final Comparator<IReference> comparator);

	
	/**
	 * Returns the {@link IDomainClass.IReference}s of the class that are
	 * satisfied by the supplied filter.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IReference> iReferences(final IFilter<IReference> filter);

	
	/**
	 * Returns the {@link IDomainClass.IReference}s of the class that are
	 * satisfied by the supplied filter; the order is indeterminate.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IReference> iReferences(final IFilter<IReference> filter, final Comparator<IReference> comparator);

	


	/**
	 * Returns the reference with given name, or <tt>nothing</tt> if unknown.
	 * 
	 * @param referenceName
	 * @return
	 */
	public IDomainClass.IReference getIReferenceNamed(String referenceName);

	/**
	 * Returns an {@link IReference} represented by the supplied 
	 * {@link EReference}; reference semantics.
	 *  
	 * @param eAttribute
	 * @return
	 */
	public IReference getIReference(EReference eReference);

	
	/**
	 * Encapsulates static semantics of a reference (1:1 or collection) of 
	 * this class.
	 * 
	 * @author Dan Haywood
	 */
	public interface IReference extends IMember {
		/**
		 * The underlying {@link EReference} that this represents.
		 * @return
		 */
		EReference getEReference();
		
		public boolean isMultiple();
	
		public boolean isOrdered();
	
		public boolean isContainer();
	
		public boolean isUnique();
	
		public boolean isChangeable();
	
		public boolean isDerived();

		/**
		 * Returns the {@link IDomainClass} referenced by this reference.
		 * 
		 * <p>
		 * If the reference is a simple 1:!, then the domain class will have been
		 * determined directly.  If the reference is a 1:m, then the domain class
		 * will have been read from the @Associates annotation.
		 *   
		 * @return
		 */
		public IDomainClass getReferencedDomainClass();

		/**
		 * Returns the binding of this attribute to parameterized {@link Binding}.
		 * 
		 * JAVA5_FIXME: fix return type.
		 * 
		 * <p>
		 * <b>Implementations should hold this reference using a thread-static,
		 * because different bindings may be in place on different threads</b>. 
		 * 
		 * @return
		 */
		public <V extends IReferenceBinding> V getBinding();

		/**
		 * For testing purposes only.
		 * 
		 * @param binding
		 */
		public void replaceBindings(IBinding binding);
		
	}


	///////////////////////////////////////////////////////////////
	// 1:1 reference
	
	/**
	 * Returns an {@link IOneToOneReference} represented by the supplied 
	 * {@link EAttribute}; reference semantics.
	 *  
	 * @param eReference
	 * @return
	 */
	public IOneToOneReference getIOneToOneReference(EReference eReference);


	/**
	 * Encapsulates static semantics of a 1:1 reference of this class.
	 */
	public interface IOneToOneReference extends IReference {

	}

	///////////////////////////////////////////////////////////////
	// collection
	
	/**
	 * Returns an {@link ICollectionReference} represented by the supplied 
	 * {@link EReference}; reference semantics.
	 *  
	 * @param eReference
	 * @return
	 */
	public ICollectionReference getICollectionReference(EReference eReference);

	/**
	 * Encapsulates static semantics of a collection reference of this class.
	 */
	public interface ICollectionReference extends IReference {
	}


	///////////////////////////////////////////////////////////////
	// operations
	

	/**
	 * Returns all the {@link IDomainClass.IOperation}s (both static and 
	 * instance) of the class, including inherited operations.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IOperation> iOperations();

	/**
	 * Returns all the {@link IDomainClass.IOperation}s of the class, 
	 * including inherited operations, in the order specified by the supplied
	 * {@link Comparator}.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IOperation> iOperations(final Comparator<IOperation> comparator);

	
	/**
	 * Returns the {@link IDomainClass.IOperation}s of the class that are
	 * satisfied by the supplied filter.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IOperation> iOperations(final IFilter<IOperation> filter);

	
	/**
	 * Returns the {@link IDomainClass.IOperation}s of the class that are
	 * satisfied by the supplied filter; the order is indeterminate.
	 * 
	 * <p>
	 * The returned list is a copy and so may safely be modified by the caller
	 * with no side-effects.
	 */
	public List<IOperation> iOperations(final IFilter<IOperation> filter, final Comparator<IOperation> comparator);

	

	/**
	 * Returns the operation with given name, or <tt>nothing</tt> if unknown.
	 * 
	 * @param operationName
	 * @return
	 */
	public IDomainClass.IOperation getIOperationNamed(String operationName);
	/**
	 * Returns an {@link IAttribute} represented by the supplied 
	 * {@link EAttribute}; reference semantics.
	 *  
	 * @param eAttribute
	 * @return
	 */
	public IOperation getIOperation(EOperation eOperation);


	/**
	 * Encapsulates static semantics of an operation of this class.
	 * 
	 * @author Dan Haywood
	 */
	public interface IOperation extends IMember {
		
		/**
		 * The underlying {@link EOperation} that this represents.
		 * @return
		 */
		EOperation getEOperation();

		
		/**
		 * Whether the operation is for an instance of this class, or for the 
		 * class itself.
		 *  
		 * <p>
		 * The operation must be one of those for the class.. 
		 * 
		 * @return false if an instance operation, true if a class operation. 
		 */
		public boolean isStatic();



		/**
		 * Returns the name of the specified parameter of the operation.
		 * 
		 * <p>
		 * All parameters will have a name, but it may (depending upon the
		 * programming model) be derived from the type.  The standard programming
		 * model will use the <tt>Named</tt> annotation if present but will 
		 * construct a name based on the type otherwise.
		 *  
		 * <p>
		 * The operation must be one of those for the class; the operation must
		 * have n parameters. 
		 * 
		 * @param parameterPosition from 0 to n-1
		 * @return
		 */
		public String getNameFor(int parameterPosition);


		/**
		 * Returns the description (if known) of the specified parameter of the 
		 * operation.
		 * 
		 * <p>
		 * The operation must be one of those for the class; the operation must
		 * have n parameters. 
		 * 
		 * @param parameterPosition from 0 to n-1
		 * @return
		 */
		public String getDescriptionFor(int parameterPosition);

		
		/**
		 * Returns internalizationalized data for the specified operation of the 
		 * domain class.
		 * 
		 * <p>
		 * The operation must be one of those for the class.. 
		 * 
		 * @return
		 */
		public II18nData getI18nData();

		

		/**
		 * Indicates whether the specified parameter of the operation (of this domain 
		 * class) is a value.
		 *  
		 * <p>
		 * If so, use {@link #getEDataTypeFor(EOperation, int)} to obtain the
		 * parameter's actual type (as a {@link EDataType}).
		 * 
		 * <p>
		 * The operation must be one of those for the class; the operation must
		 * have n parameters. 
		 * 
		 * @param parameterPosition from 0 to n-1
		 * @return
		 */
		public boolean isParameterAValue(int parameterPosition);


		/**
		 * Returns the {@link EDataType} of the specified parameter of the operation.
		 * 
		 * <p>
		 * The operation must be one of those for the class; the operation must
		 * have n parameters; the parameter must be a value 
		 * (see {@link #isParameterAValue(EOperation, int)}.
		 * 
		 * @param parameterPosition from 0 to n-1
		 * @return
		 */
		public EDataType getEDataTypeFor(int parameterPosition);


		/**
		 * Indicates whether the specified parameter of the operation (of this domain 
		 * class) is a reference to another domain object.
		 *  
		 * <p>
		 * If so, use {@link #getDomainClassFor(EOperation, int)} to obtain the
		 * parameter's actual type (as an {@link IDomainClass}).
		 * 
		 * <p>
		 * The operation must be one of those for the class; the operation must
		 * have n parameters. 
		 *
		 * @param parameterPosition from 0 to n-1
		 * @return
		 */
		public boolean isParameterADomainObject(int parameterPosition);


		/**
		 * Returns the {@link IDomainClass} of the specified parameter of the operation.
		 * 
		 * <p>
		 * The operation must be one of those for the class; the operation must
		 * have n parameters; the parameter must be a reference 
		 * (see {@link #isParameterADomainObject(EOperation, int)}.
		 * 
		 * <p>
		 * TODO: covariance of return types required for IDomainClass subtype.
		 *
		 * @param parameterPosition from 0 to n-1
		 * @return
		 */
		public IDomainClass getDomainClassFor(int parameterPosition);


		/**
		 * Returns internalizationalized data for the specified parameter of the 
		 * operation.
		 * 
		 * <p>
		 * The operation must be one of those for the class; the operation must
		 * have n parameters. 
		 * 
		 * @param parameterPosition
		 * @return
		 */
		public II18nData getI18nDataFor(int parameterPosition);

		
		/**
		 * Whether the specified parameter of the operation is optional for this 
		 * domain class.
		 * 
		 * <p>
		 * The {@link Optional} annotation is used to indicate whether an 
		 * operation's parameter is optional or not.
		 *  
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param parameterPosition
		 * @return
		 */
		public boolean isOptional(int parameterPosition);

		/**
		 * Whether the specified parameter of the operation is mandatory for this 
		 * domain class.
		 * 
		 * <p>
		 * This is a convenience method; its value is defined as the logical NOT of
		 * invoking {@link #isOptional(EOperation, int)}.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param parameterPosition
		 * @return
		 */
		public boolean isMandatory(int parameterPosition);

		/**
		 * Returns the number of lines to use in a text area for a
		 * (string) operation parameter.
		 * 
		 * <p>
		 * The {@link MultiLine} annotation is used to indicate the field 
		 * length of operation parameters.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param attribute
		 * @return
		 */
		public int getMultiLine(int parameterPosition);

		/**
		 * Returns the field length (as displayed in the UI) of the specified
		 * (string) operation parameter.
		 * 
		 * <p>
		 * The {@link FieldLengthOf} annotation is used to indicate the field 
		 * length of operation parameters.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param parameterPosition
		 * @return
		 */
		public int getFieldLengthOf(final int parameterPosition);

		/**
		 * Returns the max length (as persisted in the persistent data store) of 
		 * the specified (string) operation parameters.
		 * 
		 * <p>
		 * The {@link MaxLengthOf} annotation is used to indicate the maximum 
		 * length of operation parameters.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param parameterPosition
		 * @return
		 */
		public int getMaxLengthOf(final int parameterPosition);

		/**
		 * Returns the min length (as required to be entered in the UI) of 
		 * the specified (string) operation parameter.
		 * 
		 * <p>
		 * The {@link MinLengthOf} annotation is used to indicate the minimum 
		 * length of operation parameters.
		 * 
		 * <p>
		 * Extended semantics. 
		 * 
		 * @param parameterPosition
		 * @return
		 */
		public int getMinLengthOf(final int parameterPosition);

		/**
		 * Returns the binding of this attribute to parameterized {@link Binding}.
		 * 
		 * <p>
		 * <b>Implementations should hold this reference using a thread-static,
		 * because different bindings may be in place on different threads</b>. 
		 * 
		 * @return
		 */
		public <V extends IOperationBinding> V getBinding();
		

		/**
		 * For testing purposes only.
		 * 
		 * @param binding
		 */
		public void replaceBindings(IBinding binding);

	}

	
	////////////////////////////////////////////////////////////
	// adapters
	
	/**
	 * Obtain an arbitrary extension for this domain class, as providing by
	 * the {@link IDomainBuilder} of some programming model.
	 * 
	 * <p>
	 * This is an instance of the Extension Object pattern, used widely
	 * throughout the Eclipse Platform under the name of an "adapter" (hence
	 * our choice of name).
	 * 
	 * <p>
	 * Usage:
	 * <code>
	 * SuperDuperDomainClass sddc = 
	 * 	   (SuperDuperDomainClass)someDomainClass.getAdapter(SuperDuperDomainClass.class);
	 * </code>
	 * 
	 * @param adapterClass
	 * @return object that implements said class.
	 */
	public <V> V getAdapter(Class<V> adapterClass);

	/**
	 * List of all class adapters installed for this class.
	 * 
	 * @return
	 */
	public List<IDomainClassAdapter> getAdapters();


	/**
	 * Allows arbitrary extensions to be attached to this domain object.
	 * 
	 * <p>
	 * The adapters (as provided by {@link #getAdapter(Class)} are not
	 * attached directly, because we want to be able to serialize the 
	 * information within EMF.  So instead an adapter factory is supplied that
	 * can convert from the serialized information that EMF holds (basically
	 * a Map<String, String>) into an instance of an object implementing the
	 * required adapter class.
	 * 
	 * @param adapterClass
	 * @return
	 */
	public <V> void setAdapterFactory(Class<V> adapterClass, IAdapterFactory<? extends V> adapterFactory);

	
	/**
	 * For testing purposes only.
	 * 
	 * @param binding
	 */
	public void replaceBindings(IBinding binding);

	

}

