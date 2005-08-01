package de.berlios.rcpviewer.progmodel.extended;


/**
 * Immutable implementation of {@link IRequirement}.
 * 
 * @author Dan Haywood
 */
public final class Requirement implements IRequirement {

	/**
	 * Factory method for creating a requirement.
	 * 
	 * @param met
	 * @param description of this requirement.  Should be a positive statement, 
	 *        eg "the quantity must be between 1 and 10.
	 * @return an immutable object implementing {@link IRequirement}.
	 */
	public static IRequirement create(boolean met, String description) {
		return new Requirement(met, description, false);
	}
	

	/**
	 * Factory method that ANDs this requirement with another (or others).
	 * 
	 * <p>
	 * The resultant requirement will only be met if all of the requirements
	 * have been met.
	 */
	public static IRequirement and(
			IRequirement requirement1, IRequirement... requirementOthers) {

		IRequirement runningRequirement = requirement1;
		for(IRequirement requirement: requirementOthers) {
			runningRequirement = runningRequirement.and(requirement);
		}
		return runningRequirement;
	}

	/**
	 * Factory method that ORs this requirement with another (or others).
	 */
	public static IRequirement or(
			IRequirement requirement1, IRequirement... requirementOthers) {
		
		IRequirement runningRequirement = requirement1;
		for(IRequirement requirement: requirementOthers) {
			runningRequirement = runningRequirement.or(requirement);
		}
		return runningRequirement; 
	}


	private final boolean _met;
	private final String _description;
	private final boolean _noop;

	/**
	 * Noop requirement (Null Object pattern, cf <code>/dev/null</code>). 
	 * 
	 * <p>
	 * An alternative design is to use a static constant, however had problems
	 * in correctly initializing. 
	 */
	public static IRequirement none() {
		return new Requirement(false, null, true);
	}

	/**
	 * Constructor for factory method.
	 * 
	 * @param whetherMet
	 * @param message
	 */
	private Requirement(boolean whetherMet, String message, boolean noop) {
		_met = whetherMet;
		_description = message;
		_noop = noop;
	}
	

	/**
	 * See interface definition.
	 */
	public boolean isMet() {
		return _met;
	}
	
	/**
	 * See interface definition.
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * See interface definition.
	 */
	public final boolean isNoop() {
		return _noop;
	}

	/**
	 * See interface definition.
	 */
	public IRequirement and(IRequirement otherRequirement) {
		if (otherRequirement.isNoop()) {
			return this;
		}
		// if noop, then assume value of other
		if (isNoop()) {
			return otherRequirement;
		}
		
		return Requirement.create(
				this.isMet() && otherRequirement.isMet(), 
				andDescriptions(getDescription(), otherRequirement.getDescription()));
	}
	
	private String andDescriptions(String description1, String description2) {
		if (description1 == null) {
			return description2;
		}
		if (description2 == null) {
			return description1;
		}
		return description1 + " AND " + description2;
	}

	/**
	 * See interface definition.
	 * 
	 * @param otherRequirement
	 * @return
	 */
	public IRequirement or(IRequirement otherRequirement) {
		if (otherRequirement.isNoop()) {
			return this;
		}
		// if noop, then assume value of other
		if (isNoop()) {
			return otherRequirement;
		}
		return Requirement.create(
				this.isMet() || otherRequirement.isMet(), 
				orDescriptions(this.getDescription(), otherRequirement.getDescription()));
	}
	
	private String orDescriptions(String description1, String description2) {
		if (description1 == null) {
			return description2;
		}
		if (description2 == null) {
			return description1;
		}
		return description1 + " UNLESS " + description2;
	}



}
