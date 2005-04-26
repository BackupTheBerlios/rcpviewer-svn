package de.berlios.rcpviewer.metamodel.link;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.berlios.rcpviewer.metamodel.Constraint;
import de.berlios.rcpviewer.metamodel.IDomainObject;

/**
 * Powertype describing the different sorts of {@link de.berlios.rcpviewer.metamodel.LinkSemantics}.
 * 
 * TODO: make into an enum
 * 
 * @author Dan Haywood
 */
public final class LinkSemanticsType {
	
	public final static LinkSemanticsType SIMPLE_REF =
		new LinkSemanticsType("SimpleRef", Boolean.FALSE, IDomainObject.class,
				new Constraint[] {} );
	
	public final static LinkSemanticsType LIST =
		new LinkSemanticsType("List", Boolean.TRUE, java.util.List.class,
				new Constraint[] { Constraint.INDEXED } );
	
	public final static LinkSemanticsType SET =
		new LinkSemanticsType("Set", Boolean.TRUE, java.util.Set.class,
				new Constraint[] { Constraint.UNIQUE } );
	
	public final static LinkSemanticsType SORTED_SET =
		new LinkSemanticsType("SortedSet", Boolean.TRUE, java.util.SortedSet.class,
				new Constraint[] { Constraint.UNIQUE, Constraint.SORTED } );
	
	public final static LinkSemanticsType MAP =
		new LinkSemanticsType("Map", Boolean.TRUE, java.util.Map.class,
				new Constraint[] { Constraint.QUALIFIED } );
	
	public final static LinkSemanticsType SORTED_MAP =
		new LinkSemanticsType("SortedMap", Boolean.TRUE, java.util.SortedMap.class,
				new Constraint[] { Constraint.QUALIFIED, Constraint.SORTED } );
	
	public final static LinkSemanticsType UNKNOWN =
		new LinkSemanticsType("Unknown", null, null,
				new Constraint[] { } );
	
	private final String name;
	private final Class<?> javaType;
	private final Boolean multiple;
	private final Set<Constraint> constraints = new HashSet<Constraint>();
	
	private LinkSemanticsType(final String name, final Boolean multiple, final Class<?> javaType, final Constraint[] constraints) {
		this.name = name;
		this.multiple = multiple;
		this.javaType = javaType;
		this.constraints.addAll(Arrays.asList(constraints));
	}
	
	public String getName() {
		return name;
	}
	
	public Boolean isMultiple() {
		return multiple;
	}
	
	public Class<?> getJavaType() {
		return javaType;
	}

	/**
	 * Immutable set of applicable constraints for these semantics.
	 * TODO: <T>
	 * @return
	 */
	public Set getConstraints() {
		return Collections.unmodifiableSet(constraints);
	}
	
	public String toString() {
		return getName();
	}
	
}
