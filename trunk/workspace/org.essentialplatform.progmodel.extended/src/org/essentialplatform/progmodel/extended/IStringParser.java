package org.essentialplatform.progmodel.extended;

import java.io.File;

/**
 * Value objects (annotated with <code>@Value</code>) whose state can be 
 * interpreted from a directly entered string should implement this interface.
 * 
 * <p>
 * The platform can use this information to optionally allow the end user to
 * directly type in the value of attributes or operation parameters declared to
 * be of this value type.
 *  
 * @author Dan Haywood
 *
 */
public interface IStringParser {

	/**
	 * Whether the provided string is in a valid format such that it can be
	 * used to set the state of the value type implementing this interface.
	 * 
	 * @param input
	 * @return
	 */
	public boolean isValidString(final String input);

	/**
	 * Parse the provided input to set the state of the value type 
	 * implementing this interface.
	 * 
	 * @param string
	 * @throws IllegalArgumentException iff <code>isValidString</code> would return <code>false</code>
	 */
	public void parseString(final String input);
}
