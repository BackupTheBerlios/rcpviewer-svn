package de.berlios.rcpviewer.session;

import de.berlios.rcpviewer.session.IResolvable;

/**
 * Represents the capability for a domain object to be persisted.
 * 
 * <p>
 * There is a close relationship between this interface and {@link Resolvable};
 * only persisted objects can be resolved.
 * 
 */
public interface IPersistable {

	/**
	 * The state of this object with respect. 
	 */
	public enum PersistState {
		
		/**
		 * State of a feature is unknown; this state only applies while an
		 * object is initially being created; it is used by transaction
		 * aspects to ensure that objects being created are ignored. 
		 */
		UNKNOWN("Unknown"),
		/**
		 * Not persisted, either because it has just been created or 
		 * alternatively was persisted but has now been deleted.
		 * 
		 * <p>
		 * In other words, this object has no equivalent representation in the
		 * persistent object store (isn't a row in a database table, for
		 * example).
		 */
		TRANSIENT("Transient"),
		/**
		 * Persisted in the persistent object store.
		 */
		PERSISTED("Persisted"),
		;

		private final String _name;
		
		private PersistState(final String name) {
			_name = name;
		}
		
		public String getName() {
			return _name;
		}
		
		/**
		 * Whether this state represents an {@link IPersistable} whose
		 * persistence state is unknown or at least not yet specified.
		 * 
		 * @return
		 */
		public boolean isUnknown() {
			return this == UNKNOWN;
		}
		
		/**
		 * Whether this state represents an {@link IPersistable} that has been
		 * persisted.
		 * 
		 * @return
		 */
		public boolean isPersistent() {
			return this == PERSISTED;
		}
		
		/**
		 * Whether this state represents an {@link IPersistable} that has not
		 * been persisted or that has been deleted having once been persisted.
		 * 
		 * @return
		 */
		public boolean isTransient() {
			return this == TRANSIENT;
		}
		
		
		
		@Override
		public String toString() {
			return getName();
		}
	}
	
	/**
	 * The current {@link PersistState} of this {@link IPersistable}.
	 * 
	 * @return
	 * @see #resolved()
	 */
	public PersistState getPersistState();
	
	/**
	 * Informs this object that it has been persisted.
	 * 
	 * <p>
	 * Preconditions:
	 * <ul>
	 * <li> state of TRANSIENT
	 * </ul>
	 * 
	 * <p>
	 * Postconditions:
	 * <ul>
	 * <li> state of PERSISTENT
	 * </ul>
	 */
	public void nowPersisted();

	
	/**
	 * Informs this object that it is transient.
	 * 
	 * <p>
	 * Preconditions:
	 * <ul>
	 * <li> state of PERSISTENT
	 * </ul>
	 * 
	 * <p>
	 * Postconditions:
	 * <ul>
	 * <li> state of TRANSIENT
	 * </ul>
	 */
	public void nowTransient();

}
