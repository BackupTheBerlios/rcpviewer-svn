package org.nakedobjects.viewer.skylark;

import org.nakedobjects.object.InvalidEntryException;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.object.control.DefaultHint;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.control.Veto;
import org.nakedobjects.object.reflect.NakedObjectField;
import org.nakedobjects.object.reflect.OneToOneAssociation;
import org.nakedobjects.object.security.ClientSession;
import org.nakedobjects.object.security.Session;
import org.nakedobjects.utility.DebugString;


public class ValueField extends ValueContent implements FieldContent {
    private final ObjectField field;
    private NakedValue object;

    public ValueField(NakedObject parent, NakedValue object, OneToOneAssociation association) {
        field = new ObjectField(parent, association);
        this.object = object;
    }

    public void clear() {
        getParent().clearValue(getOneToOneAssociation());
    }

    public void debugDetails(DebugString debug) {
        field.debugDetails(debug);
        debug.appendln(4, "object", object);
    }

    private NakedObjectField getField() {
        return field.getFieldReflector();
    }

    public String getFieldName() {
        return field.getName();
    }

    public NakedObjectField getFieldReflector() {
        return field.getFieldReflector();
    }

    public String getIconName() {
        return object.getIconName();
    }

    public Naked getNaked() {
        return object;
    }

    public NakedValue getObject() {
        return object;
    }

    private OneToOneAssociation getOneToOneAssociation() {
        return (OneToOneAssociation) getField();
    }

    private NakedObject getParent() {
        return field.getParent();
    }

    public NakedObjectSpecification getSpecification() {
        return getOneToOneAssociation().getSpecification();
    }

    public Hint getValueHint(Session session, String entryText) {
        NakedValue example = (NakedValue) getSpecification().acquireInstance();

        if (example != null) {
	        try {
	            example.parseTextEntry(entryText);
	        } catch (final InvalidEntryException e) {
	            return new DefaultHint() {
	                public Consent isValid() {
	                    return new Veto(e.getMessage());
	                }
	            };
	        }
	        // TODO need the Value object to parse the entry string
	        return getParent().getHint(ClientSession.getSession(), (NakedObjectField) getField(), example);
        } else {
           // throw new NakedObjectRuntimeException("Can't create an instance of " + getSpecification());
            return new DefaultHint();
        }
    }

    public boolean isEmpty() {
        return getParent().isEmpty(getField());
    }

    public boolean isTransient() {
        return false;
    }

    public boolean isValue() {
        return true;
    }

    public void parseEntry(String entryText) throws TextEntryParseException, InvalidEntryException {
        if (object == null) {
            object = (NakedValue) getSpecification().acquireInstance();
        }
        object.parseTextEntry(entryText);
        getParent().setValue(getOneToOneAssociation(), object.getObject());

    }

    public String title() {
        return field.getName();
    }

    public String toString() {
        return (object == null ? "null" : object.titleString()) + "/" + getField();
    }
    
    public String windowTitle() {
        return title();
    }

    public boolean objectChanged() {
        return false;
    }

    public Naked drop(Content sourceContent) {
        return null;
    }

    public Consent canDrop(Content sourceContent) {
        return Veto.DEFAULT;
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