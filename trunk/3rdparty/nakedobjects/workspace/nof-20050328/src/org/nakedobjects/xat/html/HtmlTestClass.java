package org.nakedobjects.xat.html;

import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.xat.TestClass;
import org.nakedobjects.xat.TestClassDecorator;
import org.nakedobjects.xat.TestCollection;
import org.nakedobjects.xat.TestObject;


public class HtmlTestClass extends TestClassDecorator {
    private HtmlDocumentor doc;

    public HtmlTestClass(TestClass wrappedObject, HtmlDocumentor documentor) {
        super(wrappedObject);
        this.doc = documentor;
    }

    public TestObject findInstance(String title) {
        NakedObject object = (NakedObject) getForNaked();
        String shortName = object.getSpecification().getSingularName();
        TestObject result = super.findInstance(title);
        doc.doc("Find the <b>" + shortName + "</b> instance " + doc.simpleObjectString(object) + ". ");
        return result;
    }

    public TestCollection instances() {
        NakedClass nakedClass = (NakedClass) getForNaked();
        String className = nakedClass.getFullName();
        String shortName = className.substring(className.lastIndexOf(".") + 1);

        doc.doc("Get the instances of the " + shortName + " (<img width=\"16\" height=\"16\" align=\"Center\" src=\"images/"
                + shortName + "16.gif\">) " + " class");

        TestCollection instances = super.instances();
        doc.docln(", which returns " + doc.objectString(instances.getForNaked()) + ". ");
        return instances;
    }

    public TestObject newInstance() {
        String className = ((NakedClass) getForNaked()).getPluralName();
        doc.doc("Create a new " + className + " instance: ");
        TestObject instance = super.newInstance();
        NakedObject object = (NakedObject) instance.getForNaked();
        doc.doc(doc.simpleObjectString(object));
        doc.docln(object.titleString().equals("") ? ", which is untitled. " : ". ");
        return instance;
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