package org.nakedobjects.object.reflect;

import org.nakedobjects.NakedObjects;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.control.DefaultHint;
import org.nakedobjects.object.persistence.NakedObjectManager;
import org.nakedobjects.object.security.Session;


public class OneToManyAssociation extends NakedObjectAssociation {
    private final OneToManyPeer reflectiveAdapter;

    public OneToManyAssociation(String className, String methodName, NakedObjectSpecification type, OneToManyPeer association) {
        super(methodName, type, new MemberIdentifier(className, methodName));
        this.reflectiveAdapter = association;
    }

    public boolean canAccess(Session session, NakedObject object) {
        return getHint(session, object, null, true).canAccess().isAllowed();
    }

    public boolean canUse(Session session, NakedObject object) {
        return getHint(session, object, null, true).canUse().isAllowed();
    }

    public void clearCollection(NakedObject inObject) {
        NakedObjectManager objectManager = NakedObjects.getObjectManager();
        try {
            if (inObject.isPersistent()) {
                objectManager.startTransaction();
            }
            reflectiveAdapter.removeAllAssociations(inObject);
            objectManager.saveChanges();
            if (inObject.isPersistent()) {
                objectManager.endTransaction();
            }
        } catch (RuntimeException e) {
            objectManager.abortTransaction();
            throw e;
        }
    }

    public void clearAssociation(NakedObject inObject, NakedObject associate) {
        if (associate == null) {
            throw new IllegalArgumentException("element should not be null");
        }

        NakedObjectManager objectManager = NakedObjects.getObjectManager();
        try {
            if (inObject.isPersistent()) {
                objectManager.startTransaction();
            }
            reflectiveAdapter.removeAssociation(inObject, associate);
            objectManager.saveChanges();
            if (inObject.isPersistent()) {
                objectManager.endTransaction();
            }
        } catch (RuntimeException e) {
            objectManager.abortTransaction();
            throw e;
        }
    }

    public Naked get(NakedObject fromObject) {
        return reflectiveAdapter.getAssociations(fromObject);
    }

    public Hint getHint(Session session, NakedObject object) {
        return getHint(session, object, null, true);
    }

    public Hint getHint(Session session, NakedObject container, NakedObject element, boolean add) {
        if (hasHint()) {
            return reflectiveAdapter.getHint(session, container, element, add);
        } else {
            return new DefaultHint();
        }

    }

    protected String getLabel(Session session, NakedObject object) {
        Hint about = getHint(session, object);

        return getLabel(about);
    }

    public boolean hasHint() {
        return reflectiveAdapter.hasHint();
    }

    public boolean isDerived() {
        return reflectiveAdapter.isDerived();
    }

    public boolean isPart() {
        return true;
    }

    public boolean isCollection() {
        return true;
    }

    public void setAssociation(NakedObject inObject, NakedObject associate) {
        if (associate == null) {
            throw new IllegalArgumentException("Can't use null to add an item to a collection");
        }

        NakedObjectManager objectManager = NakedObjects.getObjectManager(); //inObject.getContext().getObjectManager();
        try {
            if (inObject.isPersistent()) {
                objectManager.startTransaction();
            }
            reflectiveAdapter.addAssociation(inObject, associate);
            objectManager.saveChanges();
            if (inObject.isPersistent()) {
                objectManager.endTransaction();
            }
        } catch (RuntimeException e) {
            objectManager.abortTransaction();
            throw e;
        }
    }

    public void initAssociation(NakedObject inObject, NakedObject associate) {
        reflectiveAdapter.initAssociation(inObject, associate);
    }

    public String toString() {
        return "OneToManyAssociation [" + super.toString() + ",type="
                + (getSpecification() == null ? "unknown" : getSpecification().getShortName()) + "]";
    }

    public boolean isEmpty(NakedObject inObject) {
        return reflectiveAdapter.isEmpty(inObject);
    }

    public void initOneToManyAssociation(NakedObject inObject, NakedObject[] instances) {
        reflectiveAdapter.initOneToManyAssociation(inObject, instances);
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
