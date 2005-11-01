package org.essentialplatform.progmodel.extended;

/**
 * Represents a requirement that must be met (in order for the UI to interact
 * with an {@link IDomainObject}.
 * 
 * <p>
 * Requirements are combined into {@link IPrerequisites} that indicate whether
 * a feature (attribute or operation) is usable or even visible.
 * 
 * @author Dan Haywood
 */
public interface IRequirement {

	/**
	 * Whether this requirement has been met.
	 * 
	 * <p>
	 * If the requirement is no-op, then always met.
	 * 
	 * @return whether the requirement has been met.
	 */
	boolean isMet();
	
	/**
	 * A description of the requirement, stated in positive terms.
	 * 
	 * <p>
	 * Examples of good descriptions are:
	 * <ul>
	 * <li><i>"The quantity must be between 1 and 10"</i>
	 * <li><i>"The order must not have been shipped"</i>
	 * <li><i>"Customer must not be on the blacklist"</i> 
	 * </ul>
	 * 
	 * @return the requirement's description
	 */
	String getDescription();

	/**
	 * The description if the requirement has not been met.
	 * 
	 * <p>
	 * Is a view of what the requirement represents at a point in time.  If the
	 * requirement is met, just returns an empty string. 
	 * 
	 * @return the requirement's description
	 */
	String getDescriptionIfNotMet();

	/**
	 * ANDs the current requirement with another.
	 * 
	 * <p>
	 * Implementations should create a new description based upon the 
	 * descriptions of both requirement objects.  For example, if this 
	 * requirement has a description <i>"The order must not have been shipped"</i>
	 * and the supplied requirement has a description <i>"The customer must
	 * not be on the blacklist"</i>, then a possible description would be
	 * <i>"The order must not have been shipped AND ALSO the customer must
	 * not be on the blacklist".</i>
	 * 
	 * @param requirement
	 * @return a requirement representing both requirements applied. 
	 */
	IRequirement and(IRequirement requirement);

	/**
	 * ORs the current requirement with another.
	 * 
	 * <p>
	 * Implementations should create a new description based upon the 
	 * descriptions of both requirement objects.  For example, if this 
	 * requirement has a description <i>"The order must not have been shipped"</i>
	 * and the supplied requirement has a description <i>"The customer holds
	 * gold premium advantage status"</i>, then a possible description would be
	 * <i>"The order must not have been shipped UNLESS the customer holds
	 * gold premium advantage status".</i>
	 * 
	 * @param requirement
	 * @return a requirement representing both requirements applied. 
	 */
	IRequirement or(IRequirement constraint);

	/**
	 * An object representing a lack of requirements.
	 * 
	 * <p>
	 * Useful for cases where a requirement object is required but no
	 * constraints need to be applied.  If a noop requirement is ANDed or ORed
	 * to some other requirement, then the result should be simply be the
	 * other requirement.
	 * 
	 * @return
	 */
	boolean isNoop();
	
}