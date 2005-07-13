package de.berlios.rcpviewer.transaction;



/**
 * Represents a transaction in progress or most recently completed. */
public interface ITransaction {

	/**
	 * @return the current state
	 */
	public State getState();

	
	public void startWorkGroup();
	public void completeWorkGroup() throws IllegalStateException;

	/**
	 * a COMMITTED transaction can return a {@link WorkChain} that represents
	 * the work performed in that transaction.
	 * @return
	 * @throws IllegalStateException
	 */
	public WorkChain getWorkChain() throws IllegalStateException;

	
	/**
	 * Include an work atom in this group.  If there is no bounding work group, then this tranaction will
	 * implicitly commit. 
	 * @param workAtom
	 * @throws IllegalStateException
	 */
	public void addWorkAtom(IWorkAtom workAtom) throws IllegalStateException;
	
	/**
	 * an IN_PROGRESS or NOT_STARTED transaction can be undone, irrespective of how many
	 * work groups may have been nested.
	 */
	public void rollback() throws IllegalStateException;

	/**
	 * an IN_PROGRESS or NOT_STARTED transaction can be committed, irrespective of how many
	 * work groups may have been nested.
	 */
	public void commit() throws IllegalStateException;


	/**
	 * TODO: use an enum
	 */
	public final static class State {
		/**
		 * committed, perhaps as the result of a redo; undoable
		 */
		public final static State COMMITTED = new State("COMMITTED");
		/**
		 * not yet committed, can rollback
		 */
		public final static State IN_PROGRESS = new State("IN_PROGRESS");
		/**
		 * rolled back, cannot do anything further
		 */
		public final static State ROLLED_BACK = new State("ROLLED_BACK");
		/**
		 * undone, can redo
		 */
		public final static State UNDONE = new State("UNDONE");
		/**
		 * not started; cannot 
		 */
		public final static State NOT_STARTED = new State("NOT_STARTED");
		private final static State[] states = { COMMITTED, IN_PROGRESS, ROLLED_BACK, UNDONE, NOT_STARTED };
		public static State lookup(final String name) {
			for (int i = 0; i < states.length; i++) {
				if (states[i].name.equals(name))
					return states[i];
			}
			return null;
		}
		private String name;
		private State(final String name) {
			this.name = name;
		}
		public String toString() {
			return name;
		}
	}
	

}
