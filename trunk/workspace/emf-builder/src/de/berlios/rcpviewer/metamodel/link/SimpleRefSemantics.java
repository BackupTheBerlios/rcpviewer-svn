

package de.berlios.rcpviewer.metamodel.link;

import de.berlios.rcpviewer.metamodel.LinkSemantics;

/**
 * Defines the set of semantics for a simple reference.
 * 
 * TODO: not yet in use, but part of the supporting structure for interpreting 
 * pojos.
 * TODO: convert to an enum.
 */
public class SimpleRefSemantics extends LinkSemantics {

	public SimpleRefSemantics() {
		super(LinkSemanticsType.SIMPLE_REF);
	}
}
