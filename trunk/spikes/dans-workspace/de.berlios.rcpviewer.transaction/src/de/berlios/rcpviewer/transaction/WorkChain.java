package de.berlios.rcpviewer.transaction;
import java.util.List;

import de.berlios.rcpviewer.transaction.internal.atoms.WorkAtomAdapter;



/**
 * A chain or sequence of {@link WorkAtom}s.
 */
public final class WorkChain extends WorkAtomAdapter {

	private final IWorkAtom[] atoms;

	public WorkChain(final List list) {
		atoms = (IWorkAtom[])list.toArray(new IWorkAtom[]{}); 
	}
	public void execute() {
		for (int i = 0; i < atoms.length; i++) {
			atoms[i].execute();
		}
	}
	public void undo() {
		for (int i = 0; i < atoms.length; i++) {
			atoms[atoms.length-i-1].undo();
		}
	}
	public int size() {
		return atoms.length;
	}
	
	public IWorkAtom get(final int i) {
		if (i < 0 || i >= size()) {
			throw new IllegalArgumentException("0 < i < size()");
		}
		return atoms[i];
	}

	public int hashCode() {
		// TODO: hash on first 10 in chain...
		return 0;
	}

	public boolean equals(Object other) {
		return sameClass(other) && equals((WorkChain)other);
	}

	public boolean equals(final WorkChain other) {
		if (atoms.length != other.atoms.length) {
			return false;
		}
		for (int i = 0; i < atoms.length; i++) {
			if (!atoms[i].equals(other.atoms[i])) {
				return false;
			}
		}
		return true;
	}
}
