package org.nakedobjects.reflector.java;

import org.nakedobjects.application.BusinessObjectContainer;
import org.nakedobjects.application.InstancesCriteria;
import org.nakedobjects.application.UnsupportedFindException;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjectSpecificationLoader;
import org.nakedobjects.object.TypedNakedCollection;
import org.nakedobjects.object.persistence.NakedObjectManager;
import org.nakedobjects.object.reflect.PojoAdapter;
import org.nakedobjects.utility.NotImplementedException;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;


public class JavaBusinessObjectContainer implements BusinessObjectContainer {
    private static final Logger LOG = Logger.getLogger(JavaBusinessObjectContainer.class);
    private NakedObjectManager objectManger;
    private JavaObjectFactory objectFactory;

    public JavaBusinessObjectContainer() {
//        this.objectFactory = new JavaObjectFactory(this);
        LOG.info("Object container set up");
    }
    
    public void setObjectFactory(JavaObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }
   
    public JavaObjectFactory getObjectFactory() {
        return objectFactory;
    }
    
    public void setObjectManger(NakedObjectManager objectManger) {
        this.objectManger = objectManger;
    }

    public Vector allInstances(Class cls) {
        return allInstances(cls, false);
    }

    public Vector allInstances(Class cls, boolean includeSubclasses) {
        TypedNakedCollection nakedObjectInstances = objectManger.allInstances(getSpecification(cls), includeSubclasses);
        Vector objectInstances = new Vector(nakedObjectInstances.size());
        Enumeration e = nakedObjectInstances.elements();
        while (e.hasMoreElements()) {
            NakedObject instance = (NakedObject) e.nextElement();
            objectInstances.addElement(instance.getObject());
        }
        return objectInstances;
    }

    /**
     * Creates a new instance and then persists it.
     * 
     * @see JavaBusinessObjectContainer#createTransientInstance(Class) for
     *              details of object creation
     */
    public Object createInstance(Class cls) {
        Object object = createTransientInstance(cls);
        makePersistent(object);
        return object;
    }

    /**
     * Creates a new instance of the specified type, and then call the new
     * objects setContainer() and created() methods if they exist.
     */
    public Object createTransientInstance(Class cls) {
        return objectFactory.createObject(cls);
    }

    public void destroyObject(Object object) {
        objectManger.destroyObject(PojoAdapter.createNOAdapter(object));
    }

    public Vector findInstances(InstancesCriteria criteria, boolean includeSubclasses) throws UnsupportedFindException {
        throw new NotImplementedException();
    }

    private NakedObjectSpecification getSpecification(Class cls) {
        return NakedObjectSpecificationLoader.getInstance().loadSpecification(cls);
    }

    public boolean hasInstances(Class cls) {
        return objectManger.hasInstances(getSpecification(cls));
    }


    public void makePersistent(Object transientObject) {
        objectManger.makePersistent(PojoAdapter.createNOAdapter(transientObject));
    }

    public int numberOfInstances(Class cls) {
        return objectManger.numberOfInstances(getSpecification(cls));
    }

    public void resolve(Object object) {
		if(object != null) {
	        NakedObject adapter = PojoAdapter.createNOAdapter(object);
	        if(!adapter.isResolved()) {
	            objectManger.resolve(adapter);
	        }
	    }
    }

    public void save(Object object) {
        objectManger.saveChanges();
    }

    /**
     * Generates a unique serial number for the specified squence set. Each set
     * of serial numbers are a simple numerical sequence. Calling this method
     * with a unused sequence name creates a new set.
     */
    public long serialNumber(String sequence) {
        LOG.debug("serialNumber " + sequence);

        Vector instances = allInstances(Sequence.class, false);
        Sequence number;

        for (Enumeration e = instances.elements(); e.hasMoreElements();) {
            number = (Sequence) e.nextElement();
            if (number.getName().isSameAs(sequence)) {
                number.getSerialNumber().next();
                save(number);
                return number.getSerialNumber().longValue();
            }
        }

        number = new Sequence();
        //            number.setNakedClass(NakedObjectSpecificationLoader.getInstance().loadSpecification(Sequence.class));
        number.getName().setValue(sequence);
        makePersistent(number);
        return number.getSerialNumber().longValue();
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