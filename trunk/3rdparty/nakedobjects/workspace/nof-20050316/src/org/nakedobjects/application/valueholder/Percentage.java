package org.nakedobjects.application.valueholder;


import org.nakedobjects.application.Title;
import org.nakedobjects.application.ValueParseException;

import java.text.NumberFormat;
import java.text.ParseException;


/**
 * Value object representing a percentage value.
 * <p>
 * NOTE: this class currently does not support about listeners.
 * </p>
 */
public class Percentage extends Magnitude {
	private static final long serialVersionUID = 1L;
    private static final NumberFormat FORMAT = NumberFormat.getPercentInstance();
    private float value;
    private boolean isNull;
    public Percentage() {
        this(0.0f);
        isNull = false;
    }

    public Percentage(float value) {
        this.value = value;
        isNull = false;
    }

    /**
     @deprecated
     */
    public Percentage(String text) {
        try {
            parseUserEntry(text);
            isNull = false;
        } catch (ValueParseException e) {
            throw new IllegalArgumentException("Could not parse value: " + text);
        }
    }

    public Percentage(Percentage value) {
        this.isNull = value.isNull;
        this.value = value.value;
    }

    public void add(double value) {
        this.value += value;
    }

    public void clear() {
        isNull = true;
    }

    /**
     Copies the specified object's contained data to this instance.
     param object the object to copy the data from
     */
    public void copyObject(BusinessValueHolder object) {
        if (!(object instanceof Percentage)) {
            throw new IllegalArgumentException("Can only copy the value of  a Percentage object");
        }
        isNull = ((Percentage) object).isNull;
        value = ((Percentage) object).value;
    }

    public void divide(double value) {
        this.value /= value;
    }

    /**
     Returns this value as an double.
     */
    public double doubleValue() {
        return value;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Percentage)) {
            return false;
        }
        Percentage object = (Percentage) obj;
        if (object.isEmpty() && isEmpty()) {
            return true;
        }
        return object.value == value;
    }

    /**
     Returns this value as an float.
     */
    public float floatValue() {
        return (float) value;
    }

    public String getObjectHelpText() {
        return "A floating point number object.";
    }

    /**
     Returns this value as an int.
     */
    public int intValue() {
        return (int) value;
    }

    public boolean isEmpty() {
        return isNull;
    }

    /**
     */
    public boolean isEqualTo(Magnitude magnitude) {
        if (magnitude instanceof Percentage) {
            if (isNull) {
                return magnitude.isEmpty();
            }
            return ((Percentage) magnitude).value == value;
        } else {
            throw new IllegalArgumentException("Parameter must be of type WholeNumber");
        }
    }

    public boolean isLessThan(Magnitude magnitude) {
        if (magnitude instanceof Percentage) {
            return !isEmpty() && !magnitude.isEmpty() && value < ((Percentage) magnitude).value;
        } else {
            throw new IllegalArgumentException("Parameter must be of type WholeNumber");
        }
    }

    /**
     Returns this value as an long.
     */
    public long longValue() {
        return (long) value;
    }

    public void multiply(double value) {
        this.value *= value;
    }

    public void parseUserEntry(String text) throws ValueParseException {
        if (text.trim().equals("")) {
            clear();
        } else {
            try {
                value = FORMAT.parse(text).floatValue();
            } catch (ParseException e) {
                throw new ValueParseException("Invalid number", e);
            }
        }
    }

	/**
	 * Reset this percentage so it contains 0%.
	 */
	public void reset() {
        value = 0;
        isNull = false;
	}
	

    public void setValue(float value) {
        this.value = value;
        isNull = false;
    }

    public void setValue(Percentage value) {
        this.value = value.value;
        isNull = value.isNull;
    }

    /**
     Returns this value as an short.
     */
    public short shortValue() {
        return (short) value;
    }

    public void subtract(double value) {
        add(-value);
    }

    public Title title() {
        return new Title(isEmpty() ? "" : FORMAT.format(value));
    }
    
	public void restoreFromEncodedString(String data) {
	    if(data == null || data.equals("NULL")) {
	        clear();
	    } else {
	        setValue(Float.valueOf(data).floatValue());
	    }
	}

	public String asEncodedString() {
		return isEmpty() ? null : String.valueOf(floatValue());
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