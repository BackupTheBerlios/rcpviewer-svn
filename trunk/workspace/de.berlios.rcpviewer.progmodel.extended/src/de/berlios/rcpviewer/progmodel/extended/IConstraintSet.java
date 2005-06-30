package de.berlios.rcpviewer.progmodel.extended;

/**
 * The combination of two {@link IConstraint}s indicating whether a feature is
 * visible, and if so, whether it is usable.
 * 
 * @author Dan Haywood
 *
 */
public interface IConstraintSet {

	/**
	 * Whether the feature to which this applies is visible.
	 * 
	 * @return
	 */
	IConstraint invisible();

	/**
	 * Whether the feature to which this applies is usable.
	 * 
	 * <p>
	 * In the context of an attribute, this means editable.  In the context
	 * of an operation, this means invokable.
	 * 
	 * @return
	 */
	IConstraint unusable();

	/**
	 * Ands the current invisibility constraint held by this constraint set 
	 * with one inferred by the parameteres. 
	 *  
	 * @return
	 */
	public ConstraintSet invisibleIf(boolean whetherApplies);
	
	/**
	 * Ands the current unusability constraint held by this constraint set
	 * with one inferred by the parameteres. 
	 *  
	 * @return
	 */
	public ConstraintSet unusableIf(boolean whetherApplies, String message);


}