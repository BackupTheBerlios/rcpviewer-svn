package org.nakedobjects.xat;

import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedCollection;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.security.Session;

import java.util.Hashtable;




public class NonDocumentingTestObjectFactory implements TestObjectFactory {

    public TestValue createParamerTestValue(Object value) {
        return new ParameterValueImpl(value);
    }
    public TestClass createTestClass(Session session, NakedClass cls) {
        return new TestClassImpl(session, cls, this);
    }

    public TestCollection createTestCollection(Session session, NakedCollection instances) {
        return new TestCollectionImpl(session, instances);
    }

    public TestObject createTestObject(Session session, NakedObject object) {
        return new TestObjectImpl(session, object, this);
    }

    public TestObject createTestObject(Session session, NakedObject field, Hashtable viewCache) {
        return new TestObjectImpl(session, field, viewCache, this);
    }
    
    public TestValue createTestValue(Session session, NakedValue object) {
        return new TestValueImpl(session, object);
    }

    public Documentor getDocumentor() {
        return new NullDocumentor();
    }

    public void testEnding() {
    }

    public void testStarting(String className, String methodName) {
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