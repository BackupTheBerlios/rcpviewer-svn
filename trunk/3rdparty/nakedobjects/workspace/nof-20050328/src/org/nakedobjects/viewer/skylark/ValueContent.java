package org.nakedobjects.viewer.skylark;

import org.nakedobjects.object.InvalidEntryException;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.security.ClientSession;
import org.nakedobjects.object.security.Session;
import org.nakedobjects.viewer.skylark.basic.AbstractContent;


public abstract class ValueContent extends AbstractContent {

    public Hint getHint() {
        //      TODO need to check whether a value can be edited
        return getValueHint(ClientSession.getSession(), "");
    }

    public abstract NakedValue getObject();

    public abstract Hint getValueHint(Session session, String entryText);

    public abstract void parseEntry(String entryText) throws TextEntryParseException, InvalidEntryException;

    public void parseTextEntry(String entryText) throws InvalidEntryException {
        Hint about = getValueHint(ClientSession.getSession(), entryText);
        if (about.isValid().isVetoed()) {
            throw new InvalidEntryException(about.isValid().getReason());
        }
        parseEntry(entryText);
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2005 Naked Objects Group
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