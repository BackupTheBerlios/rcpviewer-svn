package org.nakedobjects.distribution;

import org.nakedobjects.object.LoadedObjects;
import org.nakedobjects.object.ReflectionFactory;
import org.nakedobjects.object.reflect.ActionPeer;
import org.nakedobjects.object.reflect.Action;
import org.nakedobjects.object.reflect.NakedObjectField;
import org.nakedobjects.object.reflect.OneToManyPeer;
import org.nakedobjects.object.reflect.OneToManyAssociation;
import org.nakedobjects.object.reflect.OneToOnePeer;
import org.nakedobjects.object.reflect.OneToOneAssociation;


public class ProxyReflectionFactory implements ReflectionFactory {
    private ClientDistribution connection;
    private LoadedObjects loadedObjects;
    private ObjectDataFactory objectDataFactory;

    public ProxyReflectionFactory() {}

    public Action createAction(String className, ActionPeer localDelegate) {
        ActionPeer fullDelegate = new ProxyAction(localDelegate, connection, loadedObjects, objectDataFactory);
        return new Action(className, fullDelegate.getName(), fullDelegate);
    }

    public NakedObjectField createField(String className, OneToManyPeer local) {
        OneToManyPeer oneToManyDelegate = new ProxyOneToManyAssociation(local, connection);
        OneToManyAssociation association = new OneToManyAssociation(className, oneToManyDelegate.getName(), oneToManyDelegate.getType(),
                oneToManyDelegate);
        return association;
    }

    public NakedObjectField createField(String className, OneToOnePeer local) {
        OneToOnePeer oneToOneDelegate = new ProxyOneToOneAssociation(local, connection);
        OneToOneAssociation association = new OneToOneAssociation(className, oneToOneDelegate.getName(), oneToOneDelegate.getType(),
                oneToOneDelegate);
        return association;
    }

    /**
     * .NET property
     * 
     * @property
     */
    public void set_LoadedObjects(LoadedObjects loadedObjects) {
        this.loadedObjects = loadedObjects;
    }

    /**
     * .NET property
     * 
     * @property
     */
    public void set_ObjectDataFactory(ObjectDataFactory objectDataFactory) {
        this.objectDataFactory = objectDataFactory;
    }

    public void setConnection(ClientDistribution connection) {
        this.connection = connection;
    }

    public void setLoadedObjects(LoadedObjects loadedObjects) {
        this.loadedObjects = loadedObjects;
    }

    public void setObjectDataFactory(ObjectDataFactory objectDataFactory) {
        this.objectDataFactory = objectDataFactory;
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