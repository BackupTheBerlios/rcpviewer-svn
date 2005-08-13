package de.berlios.rcpviewer.progmodel.extended;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EOperation;

import de.berlios.rcpviewer.domain.IDomainClass;

public interface IExtendedDomainClass<T> {

	/**
	 * Returns the attributes of the extended domain class 
	 * {@link IDomainClass#attributes()} in the order defined by the
	 * {@link Order} annotation.
	 * 
	 * @return
	 */
	public List<EAttribute> orderedAttributes();

	/**
	 * Whether this class may be searched for by the UI in some generic
	 * search mechanism, eg Search.
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
	 * @return
	 */
	public boolean isSaveable();

	/**
	 * Whether the specified attribute is optional for this domain class.
	 * 
	 * <p>
	 * The {@link Optional} annotation is used to indicate whether an attribute
	 * is optional or not.
	 *  
	 * @param attribute - attribute of the domain class that this is an extension of
	 * @return
	 */
	public boolean isOptional(final EAttribute attribute);

	/**
	 * Whether the specified attribute is mandatory for this domain class.
	 * 
	 * <p>
	 * This is a convenience method; its value is defined as the logical NOT of
	 * invoking {@link #isOptional(EAttribute)}.
	 * 
	 * @param attribute - attribute of the domain class that this is an extension of
	 * @return
	 */
	public boolean isMandatory(final EAttribute attribute);

	/**
	 * Whether the specified parameter of the operation is optional for this 
	 * domain class.
	 * 
	 * <p>
	 * The {@link Optional} annotation is used to indicate whether an 
	 * operation's parameter is optional or not.
	 *  
	 * @param operation
	 * @param parameterPosition
	 * @return
	 */
	public boolean isOptional(EOperation operation, int parameterPosition);

	/**
	 * Whether the specified parameter of the operation is mandatory for this 
	 * domain class.
	 * 
	 * <p>
	 * This is a convenience method; its value is defined as the logical NOT of
	 * invoking {@link #isOptional(EOperation, int)}.
	 * 
	 * @param operation
	 * @param parameterPosition
	 * @return
	 */
	public boolean isMandatory(EOperation operation, int parameterPosition);

	/**
	 * Whether the specified attribute is invisible for this domain class.
	 * 
	 * <p>
	 * The {@link Invisible} annotation is used to indicate whether an attribute
	 * is optional or not.
	 * 
	 * @param attribute - attribute of the domain class that this is an extension of
	 * @return
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean isInvisible(EAttribute attribute);

	/**
	 * Returns a map keyed by name of each business key, whose value is a list
	 * of the attribute(s) that make up that business key, in the order that
	 * they were specified.
	 * 
	 * <p>
	 * The {@link BusinessKey} annotation is used to indicate which attributes
	 * are in a business key.
	 * 
	 * @return
	 */
	public Map<String, List<EAttribute>> businessKeys();

	/**
	 * Returns the field length (as displayed in the UI) of the specified
	 * (string) attribute.
	 * 
	 * <p>
	 * The {@link FieldLengthOf} annotation is used to indicate the field 
	 * length of attributes.
	 * 
	 * @param attribute
	 * @return
	 */
	public int getFieldLengthOf(EAttribute attribute);

	/**
	 * Returns the field length (as displayed in the UI) of the specified
	 * (string) operation parameter.
	 * 
	 * <p>
	 * The {@link FieldLengthOf} annotation is used to indicate the field 
	 * length of operation parameters.
	 * 
	 * @param operation
	 * @param parameterPosition
	 * @return
	 */
	public int getFieldLengthOf(EOperation operation,
			final int parameterPosition);

	/**
	 * Returns the max length (as persisted in the persistent data store) of 
	 * the specified (string) attribute.
	 * 
	 * <p>
	 * The {@link MaxLengthOf} annotation is used to indicate the maximum 
	 * length of attributes.
	 * 
	 * @param attribute
	 * @return
	 */
	public int getMaxLengthOf(EAttribute attribute);

	/**
	 * Returns the max length (as persisted in the persistent data store) of 
	 * the specified (string) operation parameters.
	 * 
	 * <p>
	 * The {@link MaxLengthOf} annotation is used to indicate the maximum 
	 * length of operation parameters.
	 * 
	 * @param operation
	 * @param parameterPosition
	 * @return
	 */
	public int getMaxLengthOf(EOperation operation, final int parameterPosition);

	/**
	 * Returns the min length (as required to be entered in the UI) of 
	 * the specified (string) attribute.
	 * 
	 * <p>
	 * The {@link MinLengthOf} annotation is used to indicate the minimum 
	 * length of attributes.
	 * 
	 * @param attribute
	 * @return
	 */
	public int getMinLengthOf(EAttribute attribute);

	/**
	 * Returns the min length (as required to be entered in the UI) of 
	 * the specified (string) operation parameter.
	 * 
	 * <p>
	 * The {@link MinLengthOf} annotation is used to indicate the minimum 
	 * length of operation parameters.
	 * 
	 * @param operation
	 * @param parameterPosition
	 * @return
	 */
	public int getMinLengthOf(EOperation operation, final int parameterPosition);

	/**
	 * Whether this attribute can be edited for as long as the object has not
	 * been persisted, but should be non-editable thereafter.
	 * 
	 * <p>
	 * The {@link ImmutableOncePersisted} annotation is used to indicate  
	 * whether the attribute has this semantic.
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean isImmutableOncePersisted(EAttribute attribute);

	/**
	 * Whether the content of this (string) attribute must be formatted
	 * according to a mask.
	 * 
	 * <p>
	 * The {@link Mask} annotation is used to indicate the mask string;
	 * returns null if none.
	 * 
	 * @param attribute
	 * @return
	 */
	public String getMask(EAttribute attribute);

	/**
	 * Whether the content of this (string) attribute must be formatted
	 * according to a regex.
	 * 
	 * <p>
	 * The {@link Mask} annotation is used to indicate the regex string;
	 * returns null if none.
	 * 
	 * @param attribute
	 * @return
	 */
	public String getRegex(EAttribute attribute);

	/**
	 * Convenience method for determining whether the candidate value
	 * is accepted by the regex associated with this attribute.
	 * 
	 * <p>
	 * If no regex has been specified (using {@link Regex}) then
	 * always returns true.
	 * 
	 * @param attribute
	 * @param candidateValue
	 * @return
	 */
	public boolean regexMatches(EAttribute attribute, String candidateValue);

}