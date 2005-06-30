package de.berlios.rcpviewer.progmodel.extended;

public interface IConstraint {

	String getMessage();

	boolean applies();
	
	/**
	 * Combine constraints together using method chaining.
	 * 
	 * @param constraint
	 * @return
	 */
	IConstraint and(IConstraint constraint);

	/**
	 * Combine constraints together using method chaining.
	 * 
	 * @param constraint
	 * @return
	 */
	IConstraint or(IConstraint constraint, String message);

	boolean isNoop();
	
}