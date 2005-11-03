package org.essentialplatform.runtime.session;

import org.essentialplatform.runtime.session.IPersistable;

/**
 * Feature (class, 1:! reference or collection) that are obtained from the 
 * resolved from the persistent object store.
 * 
 * <p>
 * The state of attributes of an object is not tracked separately; it is
 * effectively the same as that of the class.
 * 
 * <p>
 * There is a close relationship between this interface and 
 * {@link IPersistable}; only for objects that are persisted does the
 * resolved state make sense.  (Put another way; transient objects can be
 * considered as being resolved).
 * 
 * <p>
 * TODO: currently implemented directly by IDomainObject etc, but there
 * should really be a ResolvingAspect that would declare parents etc etc.
 */
public interface IResolvable {

	/**
	 * Whether this feature (class, 1:1 reference or collection) has been 
	 * resolved from the persistent object store.
	 * 
	 * <p>
	 * The state of attributes of an object is not tracked separately; it is
	 * effectively the same as that of the class.
	 * 
	 * <p>
	 * TODO: this seems to have metamorphosised into tracking the lifecycle of
	 * the pojo itself; NEW, TRANSIENT and DELETED should be moved out, methinks.
	 */
	public enum ResolveState {
		
		/**
		 * State of a feature is unknown; this state only applies while an
		 * object is initially being created; it is used by transaction
		 * aspects to ensure that objects being created are ignored. 
		 */
		UNKNOWN("Unknown"),
		/**
		 * State of a feature that has previously been persisted but not yet 
		 * been retrieved; this feature cannot be accessed. 
		 */
		UNRESOLVED("Unresolved"),
		/**
		 * State of a feature that has previously been persisted and has now 
		 * been retrieved; this feature can be accessed. 
		 */
		RESOLVED("Resolved"),
		;
		private final String _name;
		
		private ResolveState(final String name) {
			_name = name;
		}
		
		public String getName() {
			return _name;
		}
		
		/**
		 * Whether this state represents an {@link IResolvable} whose
		 * resolve state is unknown or at least not yet specified.
		 * 
		 * @return
		 */
		public boolean isUnknown() {
			return this == UNKNOWN;
		}
		
		/**
		 * Whether this state represents an {@link IResolvable} that has not
		 * been resolved.
		 * 
		 * @return
		 */
		public boolean isUnresolved() {
			return this == UNRESOLVED;
		}
		
		/**
		 * Whether this state represents an {@link IResolvable} that has 
		 * been resolved.
		 * 
		 * @return
		 */
		public boolean isResolved() {
			return this == RESOLVED;
		}
		

		@Override
		public String toString() {
			return getName();
		}
	}
	
	/**
	 * The current {@link ResolveState} of this {@link IResolvable}.
	 * 
	 * <p>
	 * The state of 1:1 references and collections are tracked independently.
	 * The resolve state of attributes is effectively the same as that of
	 * the object: if the object is resolved then its attributes may be read.
	 * 
	 * @return
	 * @see #resolved()
	 */
	public ResolveState getResolveState();
	
	/**
	 * Informs this object that it has been resolved.
	 * 
	 * <p>
	 * Preconditions:
	 * <ul>
	 * <li> state of UNRESOLVED
	 * </ul>
	 * 
	 * <p>
	 * Postconditions:
	 * <ul>
	 * <li> state of RESOLVED
	 * </ul>
	 */
	public void nowResolved();

}
