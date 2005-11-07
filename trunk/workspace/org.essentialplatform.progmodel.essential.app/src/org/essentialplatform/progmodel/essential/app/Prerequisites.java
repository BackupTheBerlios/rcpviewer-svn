package org.essentialplatform.progmodel.essential.app;


/**
 * Final implementation of {@link IPrerequisites}.
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
	public static Prerequisites none() {
		return new Prerequisites(Requirement.none(), Requirement.none());
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
					Requirement.none());
		}
		if (constraintIfNotMet == Constraint.UNUSABLE) {
			return new Prerequisites(
					Requirement.none(),
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
		if (visibilityRequirement == null) {
			throw new IllegalArgumentException("Visibility requirement cannot be null.");
		}
		if (usabilityRequirement == null) {
			throw new IllegalArgumentException("Usability requirement cannot be null.");
		}
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

	/*
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object other) {
		if (!(other.getClass() == Prerequisites.class)) { return false; }
		return equals((Prerequisites)other);
	}

	/**
	 * Implementation relies upon value semantics of {@link Requirement}.
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(final Prerequisites other) {
		return this.getUsableRequirement().equals(other.getUsableRequirement()) &&
		       this.getVisibleRequirement().equals(other.getVisibleRequirement());
	}

	/**
	 * Uses the hashCode of {@link #toString()} - a bit cheeky, but should be
	 * good enough.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	/*
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (!this.getVisibleRequirement().isMet()) {
			return "not visible, " + getVisibleRequirement().getDescription();
		}
		if (!this.getUsableRequirement().isMet()) {
			return "not usable, " + getUsableRequirement().getDescription();
		}
		return "visible" + (getVisibleRequirement().isNoop()?" (noop) ":"")
			  +", usable" + (getUsableRequirement().isNoop()?" (noop) ":"");
	}
}
