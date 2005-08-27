package de.berlios.rcpviewer.session;

/**
 * Feature (class, 1:! reference or collection) that are obtained from the 
 * resolved from the persistent object store.
 * 
 * <p>
 * The state of attributes of an object is not tracked separately; it is
 * effectively the same as that of the class.
 * 
 *  <p>
 *  TODO: currently implemented directly by IDomainObject etc, but there
 *  should really be a ResolvingAspect that would declare parents etc etc.
 */
public interface IResolvable {

	/**
	 * Whether this feature (class, 1:! reference or collection) has been 
	 * resolved from the persistent object store.
	 * 
	 * <p>
	 * The state of attributes of an object is not tracked separately; it is
	 * effectively the same as that of the class. 
	 */
	public enum ResolveState {
		
		/**
		 * In the process of being initialized. 
		 */
		NEW("New"),
		/**
		 * Fully created but not yet persisted.
		 */
		TRANSIENT("Transient"),
		/**
		 * State of a feature that has previously been persisted but not yet 
		 * been retrieved; this feature cannot be accessed. 
		 */
		UNRESOLVED("Unresolved"),
		/**
		 * State of a feature that has previously been persisted and has now 
		 * been retrieved; this feature can be accessed. 
		 */
		RESOLVED("Resolved");

		private final String _name;
		
		private ResolveState(String name) {
			_name = name;
		}
		
		public String getName() {
			return _name;
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
	 * <li> state of TRANSIENT, UNRESOLVED
	 * </ul>
	 * 
	 * <p>
	 * Postconditions:
	 * <ul>
	 * <li> state of RESOLVED
	 * </ul>
	 */
	public void nowResolved();

	
	/**
	 * Informs this object that it has been fully created (but has not yet
	 * been persisted).
	 * 
	 * <p>
	 * Preconditions:
	 * <ul>
	 * <li> state of NEW
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
