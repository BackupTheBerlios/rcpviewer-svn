package org.nakedobjects.reflector.java.value;

import org.nakedobjects.application.ValueParseException;
import org.nakedobjects.application.value.IntegerNumber;
import org.nakedobjects.object.InvalidEntryException;
import org.nakedobjects.object.reflect.valueadapter.AbstractNakedValue;
import org.nakedobjects.object.value.IntegerValue;

import java.text.NumberFormat;
import java.text.ParseException;


public class IntegerNumberAdapter extends AbstractNakedValue implements IntegerValue {
    private static NumberFormat FORMAT = NumberFormat.getNumberInstance();

    private IntegerNumber value;

    public IntegerNumberAdapter(IntegerNumber value) {
        this.value = value;
    }
    
    public byte[] asEncodedString() {
        return new Integer(value.intValue()).toString().getBytes();
    }

    public int integerValue() {
        return value.intValue();
    }
    
    public String getIconName() {
        return "Number";
    }

    public Object getObject() {
        return value;
    }

    public void parseTextEntry(String entry) throws InvalidEntryException {
        if (entry == null || entry.trim().equals("")) {
            throw new InvalidEntryException();
        } else {
            try {
                value = new IntegerNumber(FORMAT.parse(entry).intValue());
            } catch (ParseException e) {
                throw new ValueParseException("Invalid number", e);
            }
        }
    }

    public void restoreFromEncodedString(byte[] data) {
        String number = new String(data);
        int i = Integer.valueOf(number).intValue();
        value = new IntegerNumber(i);
    }

    public void setValue(int value) {
        this.value = new IntegerNumber(value);
    }

    public String titleString() {
        return FORMAT.format(value.intValue());
    }

    public String getValueClass() {
        return IntegerNumber.class.getName();
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