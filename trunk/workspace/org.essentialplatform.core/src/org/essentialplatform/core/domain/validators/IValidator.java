package org.essentialplatform.core.domain.validators;

/**
 * Performs a validation on an domain attribute.
 * 
 * @author Dan Haywood
 */
public interface IValidator {

	/**
	 * Whether the candidate value is valid.
	 * 
	 * @param candidate
	 * @return
	 */
	public boolean isValid(final String candidate);
}
