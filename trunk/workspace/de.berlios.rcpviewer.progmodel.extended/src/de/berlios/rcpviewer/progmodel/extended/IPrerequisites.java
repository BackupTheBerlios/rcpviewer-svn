package de.berlios.rcpviewer.progmodel.extended;

/**
 * The combination of two {@link IRequirement}s indicating whether a feature is
 * visible, and if so, whether it is usable.
 * 
 * @author Dan Haywood
 *
 */
public interface IPrerequisites {

	/**
	 * The nature of the constraint to enforce if this 
	 * @author Dan Haywood
	 *
	 */
	public enum Constraint {
		/**
		 * The feature should be freely usable.
		 * 
		 * <p>
		 * If all (the requirements of a) prerequisites are met, then no
		 * constraint should be applied to the feature.
		 */
		NONE,
		/**
		 * The feature should be unusable.
		 * 
		 * <p>
		 * For an attribute, this means editable.  For an operation, this means
		 * invokable.
		 */
		UNUSABLE,
		/**
		 * The feature should be hidden in the UI.
		 */
		INVISIBLE
	}
	
	public IRequirement getVisibleRequirement();
	public IRequirement getUsableRequirement();

	
	/**
	 * The nature of the constraint to apply to the feature.
	 * 
	 * <p>
	 * This is derivable from whether the {@link #getVisibleRequirement()} and
	 * the {@link #getUsableRequirement()}s are met, but is often easier to
	 * use than either.
	 * 
	 * @return
	 */
	Constraint getConstraint();
	
	/**
	 * A description of the relevant requirement that is not met.
	 * 
	 * <p>
	 * If the constraint (@link #getConstraint()}) enforced by these 
	 * prerequisites is that the feature is not visible, then the description 
	 * will be that of the visibility requirement (@link #getVisibleRequirement()}).
	 * Similarly, if the constraint (@link #getConstraint()}) enforced by these 
	 * prerequisites is that the feature is not usable, then the description 
	 * will be that of the usability requirement (@link #getUsableRequirement()}).
	 * 
	 * <p>
	 * This is derivable from the two requirement objects, but is often easier
	 * to use.  If both sets of requirements are met, then returns 
	 * <code>null</code>.
	 * 
	 * @return
	 */
	String getDescription();

	
	/**
	 * ANDs the requirements represented by these prerequisites with those
	 * of some other prerequisites.
	 * 
	 * <p>
	 * If either visibility requirement is not met, then the resultant 
	 * prerequisites are taken to be invisible ({@link #isVisible()} returns 
	 * <code>false</code>).
	 * 
	 * <p>
	 * Similarly, if either usability requirement is not met, then the 
	 * resultant prerequisites are taken to be unusable 
	 * ({@link #getConstraint()} returns {@link Constraint#UNUSABLE}). 
	 * 
	 * @param prerequisites
	 * @return
	 */
	IPrerequisites andRequire(IPrerequisites prerequisites);
	
	/**
	 * Overloaded version of {@link #andRequire(IPrerequisites)} that creates an
	 * IPrerequisite object with a <i>usability</i> requirement
	 * based upon the passed in arguments.
	 * 
	 * <p>
	 * A default description is used.
	 * 
	 * @param isRequirementMet
	 * @return
	 */
	IPrerequisites andRequire(boolean isRequirementMet);

	/**
	 * Overloaded version of {@link #andRequire(IPrerequisites)} that creates an
	 * IPrerequisite object with either an usability requirement
	 * based upon the passed in arguments.
	 * 
	 * <p>
	 * A default description is used.
	 * 
	 * @param isRequirementMet
	 * @param constraintIfNotMet
	 * @return
	 */
	IPrerequisites andRequire(boolean isRequirementMet, String requirementDescription);


	/**
	 * Overloaded version of {@link #andRequire(IPrerequisites)} that creates an
	 * IPrerequisite object with either an visibility or a usability requirement
	 * based upon the passed in arguments.
	 * 
	 * <p>
	 * A default description is used.
	 * 
	 * @param isRequirementMet
	 * @param constraintIfNotMet
	 * @return
	 */
	IPrerequisites andRequire(boolean isRequirementMet, Constraint constraintIfNotMet);


	/**
	 * Overloaded version of {@link #andRequire(IPrerequisites)} that creates an
	 * IPrerequisite object with either an visibility or a usability requirement
	 * based upon the passed in arguments.
	 * 
	 * @param isRequirementMet
	 * @param requirementDescription
	 * @param constraintIfNotMet
	 * @return
	 */
	IPrerequisites andRequire(boolean isRequirementMet, String requirementDescription, Constraint constraintIfNotMet);

	
	/**
	 * ORs the requirements represented by these prerequisites with those
	 * of some other prerequisites.
	 * 
	 * <p>
	 * If either visibility requirement is not met, then the resultant 
	 * prerequisites are taken to be invisible ({@link #getConstraint()} returns 
	 * {@link Constraint#INVISIBLE}).
	 * 
	 * <p>
	 * Similarly, if either usability requirement is not met, then the 
	 * resultant prerequisites are taken to be unusable ({@link #isUsable()} 
	 * returns <code>false</code>).
	 * 
	 * @param prerequisites
	 * @return
	 */
	IPrerequisites orRequire(IPrerequisites prerequisites);



	/**
	 * Overloaded version of {@link #orRequire(IPrerequisites)} that creates an
	 * IPrerequisite object with either an visibility or a usability requirement
	 * based upon the passed in arguments.
	 * 
	 * @param isRequirementMet
	 * @param requirementDescription
	 * @param constraintIfNotMet
	 * @return
	 */
	IPrerequisites orRequire(boolean isRequirementMet, String requirementDescription);

	/**
	 * Overloaded version of {@link #orRequire(IPrerequisites)} that creates an
	 * IPrerequisite object with a <i>usability</i> requirement
	 * based upon the passed in arguments.
	 * 
	 * <p>
	 * A default description is used.
	 * 
	 * @param isRequirementMet
	 * @return
	 */
	IPrerequisites orRequire(boolean isRequirementMet);


	/**
	 * Overloaded version of {@link #orRequire(IPrerequisites)} that creates an
	 * IPrerequisite object with either an visibility or a usability requirement
	 * based upon the passed in arguments.
	 * 
	 * <p>
	 * A default description is used.
	 * 
	 * @param isRequirementMet
	 * @param constraintIfNotMet
	 * @return
	 */
	IPrerequisites orRequire(boolean isRequirementMet, Constraint constraintIfNotMet);

	/**
	 * Overloaded version of {@link #orRequire(IPrerequisites)} that creates an
	 * IPrerequisite object with either an visibility or a usability requirement
	 * based upon the passed in arguments.
	 * 
	 * @param isRequirementMet
	 * @param requirementDescription
	 * @param constraintIfNotMet
	 * @return
	 */
	IPrerequisites orRequire(boolean isRequirementMet, String requirementDescription, Constraint constraintIfNotMet);

}