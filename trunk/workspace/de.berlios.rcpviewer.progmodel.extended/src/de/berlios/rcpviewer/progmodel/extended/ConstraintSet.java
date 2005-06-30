package de.berlios.rcpviewer.progmodel.extended;

public final class ConstraintSet implements IConstraintSet {

	public static ConstraintSet create() {
		return new ConstraintSet();
	}

	private ConstraintSet() {}
	
	/**
	 * 
	 * @return
	 */
	public ConstraintSet invisibleIf(boolean whetherApplies) {
		ConstraintSet set = create();
		set.invisible().and(Constraint.create(whetherApplies, "(invisible)" ));
		set.unusable().and(this.unusable()); // preserve current
		return set;
	}
	
	public ConstraintSet unusableIf(boolean whetherApplies, String message) {
		ConstraintSet set = create();
		set.unusable().and(Constraint.create(whetherApplies, message));
		set.invisible().and(this.invisible()); // preserve current
		return set;
	}

	private IConstraint invisibleConstraint = Constraint.noop();
	public IConstraint invisible() {
		return invisibleConstraint;
	}

	private IConstraint unusableConstraint = Constraint.noop();
	public IConstraint unusable() {
		return unusableConstraint;
	}

	
}
