package org.nakedobjects.utility;


public final class ToString {
    private final StringBuffer string;
    private boolean addComma = false;

    public ToString(final Object forObject) {
        string = new StringBuffer();
        String name = forObject.getClass().getName();
        string.append(name.substring(name.lastIndexOf('.') + 1));
    //    string.append("@");
     //   string.append(Integer.toHexString(forObject.hashCode()));
        string.append(" [");
    }

    public String toString() {
        string.append(']');
        return string.toString();
    }

    public void append(final String name, final Object object) {
        if(addComma) {
            string.append(',');
        } else {
            addComma = true;
        }
        string.append(name);
        string.append('=');
        string.append(object);
    }

    public  void append(final String text) {
        string.append(text);
    }
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