

package de.berlios.rcpviewer.metamodel.link;

import de.berlios.rcpviewer.metamodel.LinkSemantics;

/**
 * Defines the set of semantics where the semantics are - er - unknown.
 * 
 * TODO: not yet in use, but part of the supporting structure for interpreting 
 * pojos.
 * TODO: convert to an enum.
 */
public class UnknownSemantics extends LinkSemantics {

	public UnknownSemantics() {
		super(LinkSemanticsType.UNKNOWN);
	}
}
