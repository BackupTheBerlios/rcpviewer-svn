package org.essentialplatform.runtime.client.transaction;

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
import org.essentialplatform.runtime.client.transaction.changes.ChangeSet;
import org.essentialplatform.runtime.client.transaction.changes.IChange;
import org.essentialplatform.runtime.client.transaction.changes.InstantiationChange;
import org.essentialplatform.runtime.client.transaction.changes.Interaction;
import org.essentialplatform.runtime.client.transaction.event.ITransactionListener;
import org.essentialplatform.runtime.client.transaction.event.TransactionEvent;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.domain.IPojo;

/**
 * Implementation of {@link Transaction} as used by {@link org.essentialplatform.transaction.TransactionManager}
 */
public final class Transaction extends AbstractTransaction {

	protected Logger getLogger() { 
		return Logger.getLogger(Transaction.class);
	}



	///////////////////////////////////////////////////////////////
    

	public Transaction(final TransactionManager transactionManager) {
		super(transactionManager);
	}


}
