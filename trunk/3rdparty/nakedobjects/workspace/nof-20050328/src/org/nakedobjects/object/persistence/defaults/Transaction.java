package org.nakedobjects.object.persistence.defaults;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.UpdateNotifier;
import org.nakedobjects.object.persistence.NakedObjectStore;
import org.nakedobjects.object.persistence.ObjectStoreException;
import org.nakedobjects.object.persistence.PersistenceCommand;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;


public class Transaction {
    private static final Logger LOG = Logger.getLogger(Transaction.class);
    private final Vector toNotify = new Vector();
    private final Vector transactionCommands = new Vector();
    private boolean complete;

    public Transaction() {
        LOG.debug("new transaction" + this);
    }
    
    public void abort() {
        LOG.debug("abort transaction" + this);
        if(complete) {
            throw new TransactionException("Transaction already complete; cannot abort");
        }
        complete = true;
    }

    void addCommand(PersistenceCommand command) {
        LOG.debug("add command " + command);
        transactionCommands.addElement(command);
    }

    void addNotify(NakedObject object) {
        LOG.debug("add notification for " + object);
        toNotify.addElement(object);
    }

    public void commit(NakedObjectStore objectStore, UpdateNotifier notifier) throws ObjectStoreException {
        LOG.debug("commit transaction" + this);
        if(complete) {
            throw new TransactionException("Transaction already complete; cannot commit");
        }
        complete = true;
        
        PersistenceCommand[] commands = new PersistenceCommand[transactionCommands.size()];
        transactionCommands.copyInto(commands);
        if(commands.length > 0) {
	        objectStore.startTransaction();
	        objectStore.runTransaction(commands);
	        objectStore.endTransaction();
        }
        
        NakedObject object;
        for (Enumeration e = toNotify.elements(); e.hasMoreElements();) {
            object = (NakedObject) e.nextElement();
            LOG.debug("broadcastObjectUpdate " + object);
            notifier.broadcastObjectChanged(object);
        }
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2004 Naked Objects Group
 * Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address
 * of Naked Objects Group is Kingsway House, 123 Goldworth Road, Woking GU21
 * 1NR, UK).
 */