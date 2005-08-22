package de.berlios.rcpviewer.transaction.internal;

import de.berlios.rcpviewer.transaction.ITransactable;
import de.berlios.rcpviewer.transaction.IChange;


/**
 * Convenience adapter to simplify the implementation of {@link IChange}s.
 */
public abstract class AbstractChange implements IChange {

	private static final Object[] __EMPTY_OBJECT_ARRAY = new Object[]{};
	
	private final String _description;
	private final Object[] _extendedInfo;
	private final boolean _irreversible;

	protected AbstractChange(final String description, final Object[] extendedInfo, final boolean irreversible) {
		_description = description;
		_extendedInfo = extendedInfo == null? __EMPTY_OBJECT_ARRAY: extendedInfo;
		_irreversible = irreversible;
	}
	
	public String getDescription() {
		return _description;
	}
	public Object[] getExtendedInfo() {
		return _extendedInfo;
	}

	public boolean isIrreversible() {
		return _irreversible;
	}

	// force subclasses to implement
	public abstract int hashCode();
	
	// force subclasses to implement
	public abstract boolean equals(final Object o);
	
	protected final boolean sameClass(final Object other) {
		return getClass().equals(other.getClass());
	}


}
