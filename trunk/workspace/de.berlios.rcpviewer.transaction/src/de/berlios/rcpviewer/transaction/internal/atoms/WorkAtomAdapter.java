package de.berlios.rcpviewer.transaction.internal.atoms;

import de.berlios.rcpviewer.transaction.IWorkAtom;

/**
 * Convenience adapter to simplify the implementation of {@link WorkAtom}s.
 */
public abstract class WorkAtomAdapter implements IWorkAtom {

	// force subclasses to implement
	public abstract int hashCode();
	
	// force subclasses to implement
	public abstract boolean equals(final Object o);
	
	protected final boolean sameClass(final Object other) {
		return getClass().equals(other.getClass());
	}
}
