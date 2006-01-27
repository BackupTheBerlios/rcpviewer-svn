package org.essentialplatform.runtime.client.transaction.changes;

import java.util.List;

import org.essentialplatform.runtime.client.transaction.ITransaction;


/**
 * A {@link Changeset} representing a discrete interaction by the user with the application.
 * 
 * 
 * <p>
 * Adds no additional behaviour, but clarifies the usage of a ChangeSet.
 */
public final class Interaction extends ChangeSet {

	/**
	 * Construct the chain from a list of {@link IChange}s.
	 * 
	 * <p>
	 * The changes in the copied out of the list; subsequent changes to the 
	 * list's contents are not reflected in this set. 
	 *  
	 * @param list
	 */
	public Interaction(final ITransaction transaction, final List<IChange> list) {
		super(transaction, list.toArray(new IChange[]{}));
	}
	public Interaction(final ITransaction transaction, final IChange[] changes) {
		super(transaction, changes);
	}

}
