package org.nakedobjects.distribution;

import org.nakedobjects.object.LoadedObjects;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.reflect.AbstractActionPeer;
import org.nakedobjects.object.reflect.ActionPeer;
import org.nakedobjects.object.reflect.MemberIdentifier;
import org.nakedobjects.object.reflect.ReflectriveActionException;
import org.nakedobjects.object.security.ClientSession;
import org.nakedobjects.object.security.Session;

import org.apache.log4j.Logger;


public final class ProxyAction extends AbstractActionPeer {
    final static Logger LOG = Logger.getLogger(ProxyAction.class);
    private ClientDistribution connection;
    private boolean fullProxy = false;
    private final LoadedObjects loadedObjects;
    private final ObjectDataFactory objectDataFactory;

    public ProxyAction(final ActionPeer local, final ClientDistribution connection, final LoadedObjects loadedObjects,
            ObjectDataFactory objectDataFactory) {
        super(local);
        this.connection = connection;
        this.loadedObjects = loadedObjects;
        this.objectDataFactory = objectDataFactory;
    }

    public Naked execute(MemberIdentifier identifier, NakedObject target, Naked[] parameters) throws ReflectriveActionException {
        if (isPersistent(target)) {
            String[] parameterTypes = pararmeterTypes();
            ObjectData[] parameterObjectData = parameterValues(parameters);
            ObjectData targetObjectData = connection.executeAction(ClientSession.getSession(), getType().getName(), getName(),
                    parameterTypes, target.getOid(), target.getSpecification().getFullName(), parameterObjectData);
            NakedObject returnedObject;
            returnedObject = targetObjectData == null ? null : ObjectDataHelper.recreate(loadedObjects, targetObjectData);
            return returnedObject;
        } else {
            return super.execute(identifier, target, parameters);
        }
    }

    private ObjectData[] parameterValues(Naked[] parameters) {
        ObjectData parameterObjectData[] = new ObjectData[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i] != null) {
                parameterObjectData[i] = objectDataFactory.createObjectData((NakedObject) parameters[i], 0);
            }
        }
        return parameterObjectData;
    }

    private String[] pararmeterTypes() {
        NakedObjectSpecification[] parameterTypes = parameterTypes();
        String[] parameterTypeNames = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypeNames.length; i++) {
            parameterTypeNames[i] = parameterTypes[i].getFullName();
        }
        return parameterTypeNames;
    }

    public Hint getHint(MemberIdentifier identifier, Session session, NakedObject object, Naked[] parameters) {
        if (isPersistent(object) && fullProxy) {
            String[] parameterTypes = pararmeterTypes();
            ObjectData[] parameterObjectData = parameterValues(parameters);
            return connection.getActionHint(session, getType().getName(), getName(), parameterTypes, object.getOid(), object
                    .getSpecification().getFullName(), parameterObjectData);
        } else {
            return super.getHint(identifier, session, object, parameters);
        }
    }

    private boolean isPersistent(NakedObject object) {
        return object.getOid() != null;
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