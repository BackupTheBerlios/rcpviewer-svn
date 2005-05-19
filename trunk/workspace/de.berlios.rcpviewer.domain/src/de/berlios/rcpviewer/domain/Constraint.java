package de.berlios.rcpviewer.domain;


/**
 * Enumeration of different constraints applicable to {@link LinkSemanticsType}s.
 * 
 * @author Dan Haywood
 */
public enum Constraint {
	ORDERED,    // cf java.util.List 
	UNIQUE,     // cf java.util.Set
	QUALIFIED,  // cf java.util.Map
	SORTED      // cf java.util.SortedSet or java.util.SortedMap
}
