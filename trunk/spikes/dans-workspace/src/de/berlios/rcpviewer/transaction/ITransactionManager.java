package de.berlios.rcpviewer.transaction;



;

/**
 * Service managed by {@link Container}.
 */

public interface ITransactionManager {

	/**
	 * creates an IN_PROGRESS transaction which holds a null {@link WorkAtom}.
     * TODO: what if call beginTransaction twice?  If there is a transaction in flight, perhaps just return it?
	 */
	public ITransaction beginTransaction();

	/**
	 * creates an empty NOT_STARTED transaction.
	 * @return
	 */
	public ITransaction getTransaction();

	public void undo();
	public boolean canUndo();

	public void redo();
	public boolean canRedo();
	
}
