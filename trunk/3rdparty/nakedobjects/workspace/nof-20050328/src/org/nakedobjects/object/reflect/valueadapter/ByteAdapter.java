package org.nakedobjects.object.reflect.valueadapter;

import org.nakedobjects.object.InvalidEntryException;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.value.ByteValue;

import java.text.NumberFormat;
import java.text.ParseException;


public class ByteAdapter extends AbstractNakedValue implements ByteValue {
    private static NumberFormat FORMAT = NumberFormat.getNumberInstance();

    private byte value;

    public byte[] asEncodedString() {
        return null;
    }

    public byte byteValue() {
       return value;
    }

    public String getIconName() {
        return "byte";
    }

    public Object getObject() {
        return new Byte(value);
    }

    public void parseTextEntry(String entry) throws InvalidEntryException {
        if (entry == null || entry.trim().equals("")) {
            throw new InvalidEntryException();
        } else {
            try {
                value = FORMAT.parse(entry).byteValue();
            } catch (ParseException e) {
                throw new TextEntryParseException("Invalid number", e);
            }
        }
    }

    public void restoreFromEncodedString(byte[] data) {}

    public void setValue(byte value) {
        this.value = value;
    }

    public String titleString() {
        return FORMAT.format(value);
    }

    public String getValueClass() {
        return byte.class.getName();
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