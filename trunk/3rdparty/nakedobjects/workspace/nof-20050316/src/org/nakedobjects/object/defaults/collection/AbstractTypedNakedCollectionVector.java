package org.nakedobjects.object.defaults.collection;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjectSpecificationLoader;
import org.nakedobjects.object.TypedNakedCollection;
import org.nakedobjects.object.control.Allow;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.object.control.Veto;


public abstract class AbstractTypedNakedCollectionVector extends AbstractNakedCollectionVector implements TypedNakedCollection {
    private NakedObjectSpecification specification;

    public AbstractTypedNakedCollectionVector(Class type) {
        this(NakedObjectSpecificationLoader.getInstance().loadSpecification(type));
    }

    public AbstractTypedNakedCollectionVector(NakedObjectSpecification specification) {
        this.specification = specification;
    }

    public AbstractTypedNakedCollectionVector(String typeName) {
        this(NakedObjectSpecificationLoader.getInstance().loadSpecification(typeName));
    }

    /**
     * Determines whether the specified object can be added to this collection.
     * Object are allowed if they: are of the type specified; and are not
     * already part of the collection.
     */
    public Consent canAdd(NakedObject object) {
        if (contains(object)) {
            return new Veto("Collection cannot have duplicate objects");
        }

        if (object.getSpecification().isOfType(getElementSpecification())) {
            return Allow.DEFAULT;
        } else {
            return new Veto("Object cannot be added because it is not of the type " + getElementSpecification().getFullName());
        }
    }

    /**
     * Determines whether the specified object can be removed from this
     * collection. Object are allowed to be removed if they are part of the
     * collection.
     */
    public Consent canRemove(NakedObject object) {
        if (contains(object)) {
            return Allow.DEFAULT;
        }

        return Veto.DEFAULT;
    }

    /**
     * The type of object that can be stored in this collection.
     */
    public NakedObjectSpecification getElementSpecification() {
        return specification;
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
