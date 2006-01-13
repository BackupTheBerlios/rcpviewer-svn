package org.essentialplatform.runtime.shared.transaction.noop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.essentialplatform.progmodel.essential.app.IAppContainer;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;
import org.essentialplatform.runtime.shared.transaction.AbstractTransaction;
import org.essentialplatform.runtime.shared.transaction.ITransactable;
import org.essentialplatform.runtime.shared.transaction.ITransaction;
import org.essentialplatform.runtime.shared.transaction.TranMgmtConstants;
import org.essentialplatform.runtime.shared.transaction.Transaction;
import org.essentialplatform.runtime.shared.transaction.ITransaction.State;
import org.essentialplatform.runtime.shared.transaction.changes.ChangeSet;
import org.essentialplatform.runtime.shared.transaction.changes.IChange;
import org.essentialplatform.runtime.shared.transaction.changes.InstantiationChange;
import org.essentialplatform.runtime.shared.transaction.changes.Interaction;
import org.essentialplatform.runtime.shared.transaction.event.ITransactionListener;
import org.essentialplatform.runtime.shared.transaction.event.TransactionEvent;

public class NoopTransaction extends AbstractTransaction {

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(NoopTransaction.class);
	}

	public NoopTransaction(TransactionManager transactionManager, IAppContainer appContainer) {
		super(transactionManager, appContainer);
	}


	/*
	 * Since this is a no-op, does nothing
	 * 
	 * <p>
	 * @see org.essentialplatform.transaction.ITransaction#addingToInteraction(org.essentialplatform.transaction.IChange)
	 */
	@Override
	public boolean addingToInteraction(final IChange change) {
		checkCurrentTransactionOfTransactionManager();

		// will be BUILDING_CHANGE if originally executing, but
		// will be IN_PROGRESS if redoing.  
		checkInState(ITransaction.State.BUILDING_CHANGE, Transaction.State.IN_PROGRESS);

		getLogger().debug("ignoring change (this is a noop xactn)");

		return true;
	}


	protected void checkCurrentTransactionOfTransactionManager() {
		// a horrible hack
	}


	///////////////////////////////////////////////////////////////
	// reverse, reapply (xactns that have been committed)
	///////////////////////////////////////////////////////////////

	/*
	 * @see org.essentialplatform.transaction.ITransaction#reverse()
	 */
	public ITransaction reverse() {
		checkInState(ITransaction.State.COMMITTED);

		getLogger().debug("ignoring reverse (this is a noop xactn)");
		
		return this;
	}

	/*
	 * @see org.essentialplatform.transaction.ITransaction#reapply()
	 */
	public ITransaction reapply() {
		checkInState(ITransaction.State.REVERSED);

		getLogger().debug("ignoring reapply (this is a noop xactn)");

		return this;
	}



	///////////////////////////////////////////////////////////////
	// toString
	///////////////////////////////////////////////////////////////

	/*
     * @see java.lang.Object#toString()
     */
	@Override
    public String toString() {
		return "Noop Xactn";
    }



}
