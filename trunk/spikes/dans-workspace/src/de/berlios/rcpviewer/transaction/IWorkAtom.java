package de.berlios.rcpviewer.transaction;

/**
 * Represents an interaction between two domain objects.
 */
public interface IWorkAtom {
	public void execute();
	public void undo();
	
	public final static IWorkAtom NULL = NullWorkAtom.instance;
	
	/**
	 * Null object pattern applied to {@link WorkAtom}s.
	 */
	public static final class NullWorkAtom implements IWorkAtom {

		/**
		 * package level visibility for {@link WorkAtom} to access
		 */
		final static NullWorkAtom instance = new NullWorkAtom();
		
		private NullWorkAtom() {
		}
		
		public final void execute() {
			// does nothing
		}
		public final void undo() {
			// does nothing
		}
		
		/**
		 * always returns 0.  Well, it is the Null object.
		 */
		public int hashCode() {
			return 0;
		}
		
		/**
		 * equal if it is a {@link NullWorkAtom}
		 */
		public boolean equals(Object other) {
			if (!getClass().equals(other.getClass())) {
				return false;
			}
			return true;
		}
	}
	
}

