package de.berlios.rcpviewer.domain;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.progmodel.extended.Named;
import de.berlios.rcpviewer.progmodel.standard.IFeatureId;
import de.berlios.rcpviewer.progmodel.standard.StandardProgModelConstants;


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

	/**
	 * Represents a feature of this class.
	 * 
	 * @author Dan Haywood
	 *
	 */
	public interface IMember {
		IDomainClass getDomainClass();
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

	
	///////////////////////////////////////////////////////////////
	// attribute
	
	/**
	 * Convenience method returning a type-safe iterable List over all 
	 * EAttributes (whether inherited or not) known to the underlying EClass.
	 * 
	 * @return
	 */
	public List<EAttribute> attributes();
	
	/**
	 * Overloaded version of {@link #attributes()} to indicate whether to
	 * include inherited attributes or not.
	 * 
	 * @return
	 */
	public List<EAttribute> attributes(boolean includeInherited);


	/**
	 * Returns the attribute with given name, or <tt>nothing</tt> if unknown.
	 * 
	 * @param attributeName
	 * @return
	 */
	public EAttribute getEAttributeNamed(String attributeName);

	/**
	 * Whether the EClass wrapped by this DomainClass contains the 
	 * EAttribute.
	 * 
	 * @param eAttribute
	 * @return
	 */
	public boolean containsAttribute(EAttribute eAttribute);

	/**
	 * Returns an {@link IAttribute} represented by the supplied 
	 * {@link EAttribute}; reference semantics.
	 *  
	 * @param eAttribute
	 * @return
	 */
	public IAttribute getAttribute(EAttribute eAttribute);


	/**
	 * Encapsulates static semantics of an attribute of this class.
	 * 
	 * @author Dan Haywood
	 */
	public interface IAttribute extends IMember {
		/**
		 * The underlying {@link EAttribute} that this represents.
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
		public II18nData getI18nDataFor();

		/**
		 * Returns a feature identifier for the supplied attribute.
		 * 
		 * @param attribute
		 * @return
		 */
		public IFeatureId attributeIdFor();

	}

	///////////////////////////////////////////////////////////////
	// reference
	

	/**
	 * 
	 * @return
	 */
	public List<EReference> references();


	/**
	 * Returns the reference with given name, or <tt>nothing</tt> if unknown.
	 * 
	 * @param referenceName
	 * @return
	 */
	public EReference getEReferenceNamed(String referenceName);

	/**
	 * Returns an {@link IReference} represented by the supplied 
	 * {@link EReference}; reference semantics.
	 *  
	 * @param eAttribute
	 * @return
	 */
	public IReference getReference(EReference eReference);

	
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
		 * @param departmentRef
		 * @return
		 */
		public <V> IDomainClass<V> getReferencedClass();

		/**
		 * Returns a feature identifier for the supplied reference.
		 * 
		 * @param reference
		 * @return
		 */
		public IFeatureId referenceIdFor();

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
	public IOneToOneReference getOneToOneReference(EReference eReference);


	/**
	 * Encapsulates static semantics of a 1:1 reference of this class.
	 * 
	 * @author Dan Haywood
	 */
	public interface IOneToOneReference extends IReference {}

	///////////////////////////////////////////////////////////////
	// collection
	
	/**
	 * Returns an {@link ICollectionReference} represented by the supplied 
	 * {@link EReference}; reference semantics.
	 *  
	 * @param eReference
	 * @return
	 */
	public ICollectionReference getCollectionReference(EReference eReference);

	/**
	 * Encapsulates static semantics of a collection reference of this class.
	 * 
	 * @author Dan Haywood
	 */
	public interface ICollectionReference extends IReference {}


	///////////////////////////////////////////////////////////////
	// operations
	
	
	/**
	 * Convenience method returning a type-safe iterable List over all 
	 * EOperations (whether static or instance, and whether inherited or not)
	 * known to the underlying EClass.
	 * 
	 * @return
	 */
	public List<EOperation> operations();
	
	/**
	 * Overloaded version of {@link #operations()} to indicate whether to
	 * return instance vs static vs all operations, and whether to include
	 * inherited operations or not.
	 * 
	 * @return
	 */
	public List<EOperation> operations(OperationKind operationKind, boolean includeInherited);


	/**
	 * Returns the operation with given name, or <tt>nothing</tt> if unknown.
	 * 
	 * @param operationName
	 * @return
	 */
	public EOperation getEOperationNamed(String operationName);


	/**
	 * Returns an {@link IAttribute} represented by the supplied 
	 * {@link EAttribute}; reference semantics.
	 *  
	 * @param eAttribute
	 * @return
	 */
	public IOperation getOperation(EOperation eOperation);


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
		public II18nData getI18nDataFor();

		

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
		 * TODO: covariance of return types required for IRuntimeDomainClass subtype.
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
		 * Returns a operation identifier for the supplied operation.
		 * 
		 * @return
		 */
		public IFeatureId operationIdFor();
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


}

