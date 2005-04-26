package de.berlios.rcpviewer.metamodel.link;

import de.berlios.rcpviewer.metamodel.LinkSemantics;
import de.berlios.rcpviewer.metamodel.link.LinkSemanticsType;

/**
 * Defines the set of semantics for a list.
 * 
 * TODO: not yet in use, but part of the supporting structure for interpreting 
 * pojos.
 * TODO: convert to an enum.
 * 
 * @author Dan Haywood
 */
public class ListSemantics extends LinkSemantics {
	/**
	 */
	public ListSemantics() {
		super(LinkSemanticsType.LIST);
	}
}
