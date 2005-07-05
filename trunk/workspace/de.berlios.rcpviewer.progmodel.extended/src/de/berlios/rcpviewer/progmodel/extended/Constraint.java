package de.berlios.rcpviewer.progmodel.extended;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		
		boolean whetherApplies = false;
		String applicableMessage = null;
		int applicableCount = 0;
		for(IConstraint constraint: asList(constraint1, constraintOthers)) {
			if (constraint.applies()) {
				whetherApplies = true;
				applicableMessage = constraint.getMessage();
				applicableCount++;
			}
		}
		switch(applicableCount) {
		case 0:
			applicableMessage = null;
			break;
		case 1:
			// already set to the message of the constraint which applied
			break;
		default:
			applicableMessage = message; // use the one provided
			break;
		}
		return Constraint.create(whetherApplies, applicableMessage); 
	}

	private static List<IConstraint> asList(IConstraint constraint1, IConstraint... constraintOthers) {
		List<IConstraint> constraints = new ArrayList<IConstraint>();
		constraints.add(constraint1);
		constraints.addAll(Arrays.asList(constraintOthers));
		return constraints;
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

		boolean whetherApplies = true;
		StringBuffer applicableMessageBuf = new StringBuffer();
		
		for(IConstraint constraint: asList(constraint1, constraintOthers)) {
			if (constraint.applies()) {
				if (applicableMessageBuf.length() > 0) {
					applicableMessageBuf.append("; ");
				}
				applicableMessageBuf.append(constraint.getMessage());	
			} else {
				whetherApplies = false;
			}
		}
		
		return Constraint.create(
				whetherApplies, 
				whetherApplies?applicableMessageBuf.toString():null);
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
		this(false, null);
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
		return applies() ? message : null;
	}


}
