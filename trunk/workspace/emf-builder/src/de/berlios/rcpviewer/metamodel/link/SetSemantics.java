

package de.berlios.rcpviewer.metamodel.link;

import de.berlios.rcpviewer.metamodel.LinkSemantics;

/**
 * Defines the set of semantics for a set.
 * 
 * TODO: not yet in use, but part of the supporting structure for interpreting 
 * pojos.
 * TODO: convert to an enum.
 */
public class SetSemantics extends LinkSemantics {
	/**
	 * @param type
	 * @param optional
	 */
	public SetSemantics() {
		super(LinkSemanticsType.SET);
	}
}
