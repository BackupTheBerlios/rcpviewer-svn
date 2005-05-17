package de.berlios.rcpviewer.metamodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EReference;

/**
 * Powertype describing the different sorts of {@link de.berlios.rcpviewer.metamodel.LinkSemantics}.
 * 
 * TODO: make into an enum
 * 
 * @author Dan Haywood
 */
public final class LinkSemanticsType {
	
	/**
	 * Note that the java type is Object since any object could be a reference.
	 * 
	 * <p>
	 * However, there are additional constraints - it must have @InDomain, cannot
	 * have @Value.
	 */
	public final static LinkSemanticsType SIMPLE_REF =
		new LinkSemanticsType("SimpleRef", Boolean.FALSE, Object.class,
				new Constraint[] {} );
	
	public final static LinkSemanticsType LIST =
		new LinkSemanticsType("List", Boolean.TRUE, java.util.List.class,
				new Constraint[] { Constraint.ORDERED } );
	
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
	
	private final static LinkSemanticsType[] semanticTypes = 
		new LinkSemanticsType[] {
			LinkSemanticsType.LIST,
			LinkSemanticsType.SET,
			LinkSemanticsType.SORTED_SET,
			LinkSemanticsType.MAP,
			LinkSemanticsType.SORTED_MAP,
			LinkSemanticsType.UNKNOWN,
			LinkSemanticsType.SIMPLE_REF,
		};

	/**
	 * Will never map SIMPLE_REF.
	 * 
	 * @param javaType
	 * @return
	 */
	public static LinkSemanticsType lookupBy(final Class javaType) {
		if (javaType == null) { // TODO: replace with an aspect + @Required
			throw new IllegalArgumentException("Java Class cannot be null");
		}
		for(LinkSemanticsType lst: semanticTypes) {
			if (javaType.equals(lst.getJavaType())) {
				return lst;
			}
		}
		return null;
	}

	private LinkSemanticsType(final String name, final Boolean multiple, final Class<?> javaType, final Constraint[] constraints) {
		this.name = name;
		this.multiple = multiple;
		this.javaType = javaType;
		this.constraints.addAll(Arrays.asList(constraints));
	}
	
	private final String name;
	public String getName() {
		return name;
	}
	
	private final Boolean multiple;
	public Boolean isMultiple() {
		return multiple;
	}
	
	private final Class<?> javaType;
	public Class<?> getJavaType() {
		return javaType;
	}

	/**
	 * Immutable set of applicable constraints for these semantics.
	 * TODO: <T>
	 * @return
	 */
	private final Set<Constraint> constraints = new HashSet<Constraint>();
	public Set<Constraint> getConstraints() {
		return Collections.unmodifiableSet(constraints);
	}
	
	public String toString() {
		return getName();
	}

	public void setOrderingUniquenessAndMultiplicity(EReference eReference) {
		eReference.setOrdered(constraints.contains(Constraint.ORDERED));
		eReference.setUnique(constraints.contains(Constraint.UNIQUE));
		eReference.setUpperBound(multiple? -1 : 1);
	}
	
}
