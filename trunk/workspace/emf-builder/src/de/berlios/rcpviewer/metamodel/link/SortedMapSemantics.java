

package de.berlios.rcpviewer.metamodel.link;

import de.berlios.rcpviewer.metamodel.LinkSemantics;

/**
 * Defines the set of semantics for a sorted map.
 * 
 * TODO: not yet in use, but part of the supporting structure for interpreting 
 * pojos.
 * TODO: convert to an enum.
 */
public class SortedMapSemantics extends LinkSemantics {

	public SortedMapSemantics() {
		super(LinkSemanticsType.SORTED_MAP);
	}
}
