package org.nakedobjects.object.reflect;

import org.nakedobjects.NakedObjects;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.control.DefaultHint;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.persistence.NakedObjectManager;
import org.nakedobjects.object.security.Session;


public class OneToOneAssociation extends NakedObjectAssociation {
    private final OneToOnePeer reflectiveAdapter;

    public OneToOneAssociation(String className, String fieldName, NakedObjectSpecification type, OneToOnePeer association) {
        super(fieldName, type, new MemberIdentifier(className, fieldName));
        this.reflectiveAdapter = association;
    }

    protected boolean canAccess(Session session, NakedObject object) {
        return getHint(session, object, null).canAccess().isAllowed();
    }

    protected boolean canUse(Session session, NakedObject object) {
        return getHint(session, object, null).canUse().isAllowed();
    }

    protected void clearValue(NakedObject inObject) {
        NakedValue associate = (NakedValue) get(inObject);
        if (associate != null) {
            setValue(inObject, null);
        }
    }

    protected void clearAssociation(NakedObject inObject, NakedObject associate) {
        if (associate == null) {
            throw new NullPointerException("Must specify the item to remove/dissociate");
        }
        NakedObjectManager objectManager = NakedObjects.getObjectManager();
        try{
        if (inObject.isPersistent()) {
                objectManager.startTransaction();
            }
            reflectiveAdapter.clearAssociation(inObject, associate);
            objectManager.saveChanges();
            if (inObject.isPersistent()) {
                objectManager.endTransaction();
            }
        } catch (RuntimeException e) {
            objectManager.abortTransaction();
            throw e;
        }
    }

    protected Naked get(NakedObject fromObject) {
        return reflectiveAdapter.getAssociation(fromObject);
    }

    protected Hint getHint(Session session, NakedObject object, Naked value) {
        if (hasHint()) {
            return reflectiveAdapter.getHint(session, object, value);
        } else {
            return new DefaultHint();
        }
    }

    protected String getLabel(Session session, NakedObject object) {
        Hint about = getHint(session, object, get(object));

        return getLabel(about);
    }

    public boolean hasHint() {
        return reflectiveAdapter.hasHint();
    }

    protected void setValue(NakedObject inObject, Object associate) {
        NakedObjectManager objectManager = NakedObjects.getObjectManager();
        try {
            if (inObject.isPersistent()) {
                objectManager.startTransaction();
            }
            reflectiveAdapter.setValue(inObject, associate);
            objectManager.saveChanges();
            if (inObject.isPersistent()) {
                objectManager.endTransaction();
            }
        } catch (RuntimeException e) {
            objectManager.abortTransaction();
            throw e;
        }
    }

    protected void initValue(NakedObject inObject, Object associate) {
        reflectiveAdapter.initValue(inObject, associate);
    }

    public boolean isDerived() {
        return reflectiveAdapter.isDerived();
    }

    protected void setAssociation(NakedObject inObject, NakedObject associate) {
        NakedObjectManager objectManager = NakedObjects.getObjectManager();
        try {
            if (inObject.isPersistent()) {
                objectManager.startTransaction();
            }
            reflectiveAdapter.setAssociation(inObject, associate);
            objectManager.saveChanges();
            if (inObject.isPersistent()) {
                objectManager.endTransaction();
            }
        } catch (RuntimeException e) {
            objectManager.abortTransaction();
            throw e;
        }
    }

    protected void initAssociation(NakedObject inObject, NakedObject associate) {
        reflectiveAdapter.initAssociation(inObject, associate);
    }

    public String toString() {
        return "OneToOne " + (isValue() ? "VALUE" : "OBJECT") + " [" + super.toString() + ",type="
                + getSpecification().getShortName() + "]";
    }

    public boolean isEmpty(NakedObject inObject) {
        return reflectiveAdapter.isEmpty(inObject);
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