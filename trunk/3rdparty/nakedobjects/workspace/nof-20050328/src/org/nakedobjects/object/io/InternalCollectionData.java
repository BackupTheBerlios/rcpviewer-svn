package org.nakedobjects.object.io;

import org.nakedobjects.object.persistence.Oid;

class InternalCollectionData extends Data {
    private final static long serialVersionUID = 1L;
    final Data[] elements;

    public InternalCollectionData(Oid oid, String className, Data[] elements) {
        super(oid, className);
        this.elements = elements;
    }

    public InternalCollectionData(TransferableReader data) {
        super(data);

        int no = data.readInt();
        elements = new Data[no];
        for (int i = 0; i < no; i++) {
            elements[i] = (Data) data.readObject();
        }
    }

    public void writeData(TransferableWriter data) {
        super.writeData(data);

        int no = elements.length;
        data.writeInt(no);
        for (int i = 0; i < no; i++) {
            data.writeObject(elements[i]);
        }
    }

    public String toString() {
        StringBuffer str = new StringBuffer("(");
        for (int i = 0; i < elements.length; i++) {
            str.append((i > 0) ? "," : "");
            str.append(elements[i]);
        }
        str.append(")");
        return str.toString();
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