package de.berlios.rcpviewer.metamodel;

import de.berlios.rcpviewer.metamodel.link.LinkSemanticsType;

/**
 * Enumeration of different constraints applicable to {@link LinkSemanticsType}s.
 * 
 * TODO: this stuff not yet being used, to be used for derivation of links
 *
 * @author Dan Haywood
 */
public enum Constraint {
	INDEXED,    // cf java.util.List 
	UNIQUE,     // cf java.util.Set
	QUALIFIED,  // cf java.util.Map
	SORTED      // cf java.util.SortedSet or java.util.SortedMap
}
