package de.berlios.rcpviewer.transaction.internal.atoms;

import de.berlios.rcpviewer.transaction.ITransactable;


/**
 * Represents the instantiation of a {@link IDomainObject}.
 */
public final class InstantiationWorkAtom extends AbstractWorkAtom {

	public InstantiationWorkAtom(final ITransactable transactable) {
		super(transactable);
	}
	
	public void execute() {
		
	}
	
	public void undo() {
		
	}
	
	public int hashCode() {
		// TODO: should hash on more than this...
		return getTransactable().hashCode();
	}
	public boolean equals(final Object other) {
		return
			sameClass(other) &&
			equals((InstantiationWorkAtom)other);
	}

	public boolean equals(final InstantiationWorkAtom other) {
		return getTransactable().equals(other.getTransactable());
	}

}
