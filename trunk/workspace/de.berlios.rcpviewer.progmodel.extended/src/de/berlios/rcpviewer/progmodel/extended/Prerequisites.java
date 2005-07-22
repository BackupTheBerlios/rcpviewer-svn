package de.berlios.rcpviewer.progmodel.extended;


/**
 * Final implementation of {@link IPrerequisites}
 * 
 * @author Dan Haywood
 */
public final class Prerequisites implements IPrerequisites {

	/**
	 * Factory method that creates a constraint set with no constraints.
	 * 
	 * <p>
	 * That is, it references no-op {@link IRequirement}s.
	 * 
	 * @return
	 */
	public static Prerequisites noop() {
		return new Prerequisites(Requirement.noop(), Requirement.noop());
	}


	/**
	 * Factory method that creates prerequisites with no constraints on 
	 * visibility but with usability potentially constrained according to the 
	 * supplied argument, using a default description.
	 */
	public static IPrerequisites require(boolean isRequirementMet) {
		return require(isRequirementMet, "Requirements not met");
	}

	/**
	 * Factory method that creates prerequisites with no constraints on 
	 * visibility but with usability potentially constrained according to the 
	 * supplied arguments.
	 */
	public static IPrerequisites require(boolean isRequirementMet, String requirementDescription) {
		return require(isRequirementMet, requirementDescription, Constraint.UNUSABLE);
	}


	/**
	 * Factory method that creates prerequisites with either visibility or
	 * usability potentially constrained according to the last argument.
	 * 
	 * <p>
	 * If the constraint is set merely to the feature being unusable, then
	 * a default message will be used.
	 */
	public static IPrerequisites require(
			boolean isRequirementMet, Constraint constraintIfNotMet) {
		return require(isRequirementMet, "Requirements not met", constraintIfNotMet);
	}


	/**
	 * Factory method that creates prerequisites with usability constrained.
	 */
	public static IPrerequisites unusable() {
		return unusable("Unusable");
	}


	
	/**
	 * Factory method that creates prerequisites with usability constrained.
	 */
	public static IPrerequisites unusable(final String description) {
		return require(false, description);
	}


	/**
	 * Factory method that creates prerequisites with visibility constrained.
	 */
	public static IPrerequisites invisible() {
		return require(false, Constraint.INVISIBLE);
	}




	/**
	 * Factory method that creates prerequisites with either visibility or
	 * usability potentially constrained according to the last argument.
	 */
	public static IPrerequisites require(
			boolean isRequirementMet, String requirementDescription, 
			Constraint constraintIfNotMet) {

		IRequirement requirement = 
			Requirement.create(isRequirementMet, requirementDescription); 

		if (constraintIfNotMet == Constraint.NONE) {
			throw new IllegalArgumentException(
			"Constraint must be INVISIBLE or UNUSABLE");
		}
		if (constraintIfNotMet == Constraint.INVISIBLE) {
			return new Prerequisites(
					requirement,
					Requirement.noop());
		}
		if (constraintIfNotMet == Constraint.UNUSABLE) {
			return new Prerequisites(
					Requirement.noop(),
					requirement);
		}
		throw new IllegalArgumentException(
				"Constraint '" + constraintIfNotMet + "' is unknown");
	}


	private final IRequirement _visibilityRequirement;
	private final IRequirement _usabilityRequirement;


	/**
	 * Private constructor used by factory methods.
	 * 
	 * @param visibilityRequirement
	 * @param usabilityRequirement
	 */
	private Prerequisites(IRequirement visibilityRequirement, IRequirement usabilityRequirement) {
		_visibilityRequirement = visibilityRequirement;
		_usabilityRequirement = usabilityRequirement; 
	}

	/**
	 * See interface definition.
	 */
	public IRequirement getVisibleRequirement() {
		return _visibilityRequirement;
	}
	/**
	 * See interface definition.
	 */
	public IRequirement getUsableRequirement() {
		return _usabilityRequirement;
	}
	
	/**
	 * See interface definition.
	 */
	public IPrerequisites.Constraint getConstraint() {
		if (!getVisibleRequirement().isMet()) {
			return Constraint.INVISIBLE;
		}
		if (!getUsableRequirement().isMet()) {
			return Constraint.UNUSABLE;
		}
		return Constraint.NONE;
	}

	/**
	 * See interface definition.
	 */
	public String getDescription() {
		if (!getVisibleRequirement().isMet()) {
			return getVisibleRequirement().getDescription();
		}
		if (!getUsableRequirement().isMet()) {
			return getUsableRequirement().getDescription();
		}
		return null;
	}
	

	/**
	 * See interface definition.
	 */
	public IPrerequisites andRequire(IPrerequisites prerequisites) {
		return new Prerequisites(
					this.getVisibleRequirement().and(
							prerequisites.getVisibleRequirement()),
					this.getUsableRequirement().and(
							prerequisites.getUsableRequirement())
				);
	}

	/**
	 * See interface definition.
	 */
	public IPrerequisites andRequire(boolean isRequirementMet) {
		return andRequire(Prerequisites.require(isRequirementMet));
	}
	



	/**
	 * See interface definition.
	 */
	public IPrerequisites andRequire(boolean isRequirementMet, String requirementDescription) {
		return andRequire(Prerequisites.require(isRequirementMet, requirementDescription));
	}

	
	/**
	 * See interface definition.
	 */
	public IPrerequisites andRequire(boolean isRequirementMet, Constraint constraintIfNotMet) {
		return andRequire(Prerequisites.require(isRequirementMet, constraintIfNotMet));
	}


	/**
	 * See interface definition.
	 */
	public IPrerequisites andRequire(boolean isRequirementMet, String requirementDescription, Constraint constraintIfNotMet) {
		return andRequire(Prerequisites.require(isRequirementMet, requirementDescription, constraintIfNotMet));
	}


	/**
	 * See interface definition.
	 */
	public IPrerequisites orRequire(IPrerequisites prerequisites) {
		return new Prerequisites(
				this.getVisibleRequirement().or(
						prerequisites.getVisibleRequirement()),
				this.getUsableRequirement().or(
						prerequisites.getUsableRequirement())
			);
	}


	/**
	 * See interface definition.
	 */
	public IPrerequisites orRequire(boolean isRequirementMet) {
		return orRequire(Prerequisites.require(isRequirementMet));
	}


	/**
	 * See interface definition.
	 */
	public IPrerequisites orRequire(boolean isRequirementMet, String requirementDescription) {
		return orRequire(Prerequisites.require(isRequirementMet, requirementDescription));
	}

	/**
	 * See interface definition.
	 */
	public IPrerequisites orRequire(boolean isRequirementMet, Constraint constraintIfNotMet) {
		return orRequire(Prerequisites.require(isRequirementMet, constraintIfNotMet));
	}


	/**
	 * See interface definition.
	 */
	public IPrerequisites orRequire(boolean isRequirementMet, String requirementDescription, Constraint constraintIfNotMet) {
		return orRequire(Prerequisites.require(isRequirementMet, requirementDescription, constraintIfNotMet));
	}


	
}
