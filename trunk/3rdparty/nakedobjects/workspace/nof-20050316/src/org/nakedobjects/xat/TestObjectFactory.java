package org.nakedobjects.xat;

import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedCollection;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.security.Session;

import java.util.Hashtable;

public interface TestObjectFactory {

    TestValue createParamerTestValue(Object value);
    
    TestClass createTestClass(Session session, NakedClass cls);

    TestCollection createTestCollection(Session session, NakedCollection instances);
    
    TestObject createTestObject(Session session, NakedObject object);

    TestObject createTestObject(Session session, NakedObject field, Hashtable viewCache);

    TestValue createTestValue(Session session, NakedValue object);

    Documentor getDocumentor();

    void testEnding();
    
    void testStarting(String className, String methodName);
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