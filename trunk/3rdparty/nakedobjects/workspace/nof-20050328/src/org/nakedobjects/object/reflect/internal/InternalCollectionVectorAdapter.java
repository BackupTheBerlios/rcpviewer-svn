package org.nakedobjects.object.reflect.internal;

import org.nakedobjects.object.InternalCollection;
import org.nakedobjects.object.InvalidEntryException;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjectSpecificationLoader;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.persistence.Oid;
import org.nakedobjects.object.reflect.Action;
import org.nakedobjects.object.reflect.ActionParameterSet;
import org.nakedobjects.object.reflect.NakedObjectAssociation;
import org.nakedobjects.object.reflect.NakedObjectField;
import org.nakedobjects.object.reflect.OneToManyAssociation;
import org.nakedobjects.object.reflect.OneToOneAssociation;
import org.nakedobjects.object.reflect.PojoAdapter;
import org.nakedobjects.object.security.Session;
import org.nakedobjects.utility.NotImplementedException;
import org.nakedobjects.utility.UnexpectedCallException;

import java.util.Enumeration;
import java.util.Vector;

public class InternalCollectionVectorAdapter implements InternalCollection {
    private NakedObjectSpecification specification;
    private Vector collection;
    private NakedObjectSpecification elementSpecification;

    public InternalCollectionVectorAdapter(Vector vector, Class type) {
        this.collection = vector;
        
        Class t = type == null ? Object.class : type;
        elementSpecification = NakedObjectSpecificationLoader.getInstance().loadSpecification(t);
    }

    public boolean contains(NakedObject object) {
        return collection.contains(object.getObject());
    }

    public NakedObject elementAt(int index) {
        Object element = collection.elementAt(index);
        return PojoAdapter.createNOAdapter(element);
    }

    public Enumeration elements() {
        final Enumeration elements = collection.elements();
        
        return new Enumeration() {
            public boolean hasMoreElements() {
                return elements.hasMoreElements();
            }

            public Object nextElement() {
                Object element = elements.nextElement();
                return element instanceof NakedObject ? element : PojoAdapter.createNOAdapter(element);
            }
        };
    }

    public boolean isEmpty(NakedObjectField field) {
        return collection.isEmpty();
    }

    public Enumeration oids() {
        throw new NotImplementedException();
    }

    public int size() {
        return collection.size();
    }

    
    
    
    
    public void created() {}

    public void deleted() {}

    public String getIconName() {
        return null;
    }

    public Object getObject() {
        return collection;
    }

    public Oid getOid() {
        return null;
    }

    public boolean isResolved() {
        return false;
    }

    public boolean isPersistent() {
        return false;
    }

    public void setOid(Oid oid) {}

    public void setResolved() {}

    public void copyObject(Naked object) {}

    public NakedObjectSpecification getSpecification() {
        if(specification == null) {
            specification = NakedObjectSpecificationLoader.getInstance().loadSpecification(getObject().getClass());
        }
        return specification;
    }

    public boolean isSameAs(Naked object) {
        return false;
    }

    public String titleString() {
        return "vector...";
    }
    
    public NakedObjectSpecification getElementSpecification() {
        return elementSpecification;
    }

    public NakedObject parent() {
        return null;
    }

    public boolean isAggregated() {
        return false;
    }

    public void clear(OneToOneAssociation specification) {}

    public NakedObject getField(NakedObjectField field) {
        return null;
    }

    public void setAssociation(NakedObjectAssociation field, NakedObject associatedObject) {}

    public void initAssociation(NakedObjectAssociation field, NakedObject associatedObject) {}
    
    public void setValue(OneToOneAssociation field, Object object) {}

    public void initValue(OneToOneAssociation field, Object object) {}
    
    public String getLabel(Session session, NakedObjectField field) {
        return null;
    }

    public String getLabel(Session session, Action action) {
        return null;
    }

    public Enumeration getFieldElements(NakedObjectAssociation oneToManyAssociation) {
        return null;
    }

    public void clear() {}
    
    public void clearAssociation(NakedObjectAssociation specification, NakedObject ref) {}

    public boolean canAccess(Session session, NakedObjectField specification) {
        return false;
    }

    public boolean canAccess(Session session, Action action) {
        return false;
    }

    public boolean canUse(Session session, NakedObjectField field) {
        return false;
    }

    public Naked execute(Action action, Naked[] parameters) {
        return null;
    }

    public Hint getHint(Session session, Action action, Naked[] parameters) {
        return null;
    }

    public Hint getHint(Session session, NakedObjectField field, Naked value) {
        return null;
    }

    public boolean isEmpty() {
        throw new NotImplementedException();
    }

    public void parseTextEntry(OneToOneAssociation specification, String text) throws TextEntryParseException, InvalidEntryException {
        throw new NotImplementedException();    
    }

    public boolean isParsable() {
        return false;
    }

    public void initOneToManyAssociation(OneToManyAssociation association, NakedObject[] instances) {}

    public void markDirty() {}

    public void clearViewDirty() {}

    public boolean isViewDirty() {
        return false;
    }


    public ActionParameterSet getParameters(Session session, Action action, NakedObjectSpecification[] parameterTypes) {
        throw new UnexpectedCallException();
    }

    public boolean isPersistDirty() {
        return false;
    }

    public void clearPersistDirty() {}

    public void sort() {}
}


/*
Naked Objects - a framework that exposes behaviourally complete
business objects directly to the user.
Copyright (C) 2000 - 2005  Naked Objects Group Ltd

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

The authors can be contacted via www.nakedobjects.org (the
registered address of Naked Objects Group is Kingsway House, 123 Goldworth
Road, Woking GU21 1NR, UK).
*/