package de.berlios.rcpviewer.progmodel.extended;

public class Constraint implements IConstraint {

	/**
	 * Factory method.
	 */
	public static IConstraint create(boolean whetherApplies, String message) {
		return new Constraint(whetherApplies, message);
	}
	
	/**
	 * Factory method that creates a noop constraint that never applies.
	 * 
	 * <p>
	 * Implementation of the Null Object pattern (cf /dev/null).
	 */
	public static IConstraint noop() {
		Constraint noopConstraint = new Constraint();
		noopConstraint.noop = true;
		return noopConstraint;
	}
	
	/**
	 * The noop when either add'd to or or'd ignores itself. 
	 */
	private boolean noop;
	public final boolean isNoop() {
		return noop;
	}

	/**
	 * Factory method.
	 */
	public static IConstraint or(
			String message, IConstraint constraint1, IConstraint... constraintOthers) {
		boolean whetherApplies = constraint1.applies();
		for(IConstraint constraint: constraintOthers) {
			whetherApplies = whetherApplies || constraint.applies();
		}
		return Constraint.create(whetherApplies, message);
	}

	/**
	 * Instance method.
	 * 
	 * @param otherConstraint
	 * @param message
	 * @return
	 */
	public IConstraint or(IConstraint otherConstraint, String message) {
		if (otherConstraint.isNoop()) {
			return this;
		}
		// if noop, then assume value of other
		if (isNoop()) {
			this.whetherApplies = otherConstraint.applies();
			this.message = otherConstraint.getMessage();
			this.noop = false;
			return this;
		}

		return Constraint.or(message, this, otherConstraint);
	}
	

	/**
	 * Factory method.
	 */
	public static IConstraint and(
			IConstraint constraint1, IConstraint... constraintOthers) {
		boolean whetherApplies = constraint1.applies();
		StringBuffer buf = new StringBuffer(constraint1.getMessage());
		for(IConstraint constraint: constraintOthers) {
			whetherApplies = whetherApplies && constraint.applies();
			buf.append("; also ").append(constraint.getMessage());
		}
		return Constraint.create(whetherApplies, buf.toString());
	}

	/**
	 * Instance method.
	 * 
	 * @param otherConstraint
	 * @param message
	 * @return
	 */
	public IConstraint and(IConstraint otherConstraint) {
		if (otherConstraint.isNoop()) {
			return this;
		}
		// if noop, then assume value of other
		if (isNoop()) {
			this.whetherApplies = otherConstraint.applies();
			this.message = otherConstraint.getMessage();
			this.noop = false;
			return this;
		}
		
		return Constraint.and(this, otherConstraint);
	}

	


	/**
	 * Constructor for a noop-constraint.
	 */
	private Constraint() {
		this(false, "(no restriction)");
		noop = true;
	}

	protected Constraint(boolean whetherApplies, String message) {
		this.whetherApplies = whetherApplies;
		this.message = message;
		this.noop = false;
	}
	

	private boolean whetherApplies;
	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.progmodel.extended.IConstraint#applies()
	 */
	public boolean applies() {
		return whetherApplies;
	}
	

	
	private String message;
	/* (non-Javadoc)
	 * @see de.berlios.rcpviewer.progmodel.extended.IConstraint#getMessage()
	 */
	public String getMessage() {
		return message;
	}
	
}
