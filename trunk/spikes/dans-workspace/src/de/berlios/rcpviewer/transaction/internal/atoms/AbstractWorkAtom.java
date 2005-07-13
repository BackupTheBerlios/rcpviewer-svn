package de.berlios.rcpviewer.transaction.internal.atoms;

import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.IWorkAtom;


/**
 * Convenience adapter to simplify the implementation of {@link IWorkAtom}s.
 */
public abstract class AbstractWorkAtom extends WorkAtomAdapter {

	private ITransactable transactable;

	
	public AbstractWorkAtom(final ITransactable transactable) {
		this.transactable = transactable;
	}

    public final ITransactable getTransactable() {
		return transactable;
	}
	
}
