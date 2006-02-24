package org.essentialplatform.runtime.shared.domain;

/**
 * A simple interface to indicate that the results of <tt>toString</tt> (and
 * perhaps <tt>hashCode()</tt> are cached.
 * 
 * @author Dan Haywood
 */
public interface ICachesToString {

	/**
	 * Allows a 3rd party (probably a listener) to notify this instance that
	 * its cached toString (and any other details) are out of date and so
	 * should be updated.
	 */
	public void updateCachedToString();
}
