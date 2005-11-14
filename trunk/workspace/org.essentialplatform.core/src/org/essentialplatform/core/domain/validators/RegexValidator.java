package org.essentialplatform.core.domain.validators;

import org.essentialplatform.core.domain.validators.IValidator;
import org.essentialplatform.progmodel.essential.app.Regex;


/**
 * Determining whether the candidate value is accepted by a regex pattern 
 * associated with an attribute.
 * 
 * @param attribute
 * @param candidateValue
 * @return
 */
public final class RegexValidator implements IValidator {

	private final String _pattern;
	
	public RegexValidator(Regex regex) {
		_pattern = regex.value();
	}
	
	public String getPattern() {
		return _pattern;
	}

	public boolean isValid(String candidate) {
		return candidate.matches(getPattern());
	}

}
