package org.nakedobjects.application.valueholder;

import org.nakedobjects.application.NakedObjectRuntimeException;
import org.nakedobjects.application.Title;
import org.nakedobjects.application.ValueParseException;
import org.nakedobjects.application.system.Clock;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Value object representing a date, time, or datestamp value.
 * <p>
 * This object <i>does </i> support value listeners.
 * </p>
 */

/*
 * other methods to implement comparision methods
 * 
 * sameDateAs() day == day & mont == month & year == year
 * 
 * withinNextDatePeriod(int days, int months, int years) withinDatePeriod(int
 * days, int months, int years) withinPreviousDatePeriod(int days, int months,
 * int years)
 */
public class Date extends Magnitude {
    private static Clock clock;
    private static final DateFormat ISO_LONG = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat ISO_SHORT = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat LONG_FORMAT = DateFormat.getDateInstance(DateFormat.LONG);
    private static final DateFormat MEDIUM_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private static final long serialVersionUID = 1L;
    private static final DateFormat SHORT_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);

    public static void setClock(Clock clock) {
        Date.clock = clock;
        
        ISO_LONG.setLenient(false);
        ISO_SHORT.setLenient(false);
        LONG_FORMAT.setLenient(false);
        MEDIUM_FORMAT.setLenient(false);
        SHORT_FORMAT.setLenient(false);
    }
    
    
    private java.util.Date date;
    private boolean isNull = true;

    /*
     * Create a Date object for storing a date. The date is set to today's date.
     */
    public Date() {
        today();
    }

    /*
     * Create a Date object for storing a date with the date set to the
     * specified date.
     */
    public Date(Date date) {
        //      this.date = date.date;
        //      isNull = date.isNull;
        setValue(date);
    }

    /*
     * Create a Date object for storing a date with the time set to the
     * specified day, month and year.
     */
    public Date(int year, int month, int day) {
        setValue(year, month, day);
        //      isNull = false; // moved to set(Calendar) - called by setDate()
    }

    /**
     * Add the specified days, years and months to this date value.
     */
    public void add(int years, int months, int days) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        cal.add(Calendar.MONTH, months);
        cal.add(Calendar.YEAR, years);
        set(cal);
    }

    public Calendar calendarValue() {
        if (isNull) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c;
    }

    private void checkDate(int year, int month, int day) {
        if ((month < 1) || (month > 12)) {
            throw new IllegalArgumentException("Month must be in the range 1 - 12 inclusive");
        }

        Calendar cal = Calendar.getInstance();

        cal.set(year, month - 1, 0);

        int lastDayOfMonth = cal.getMaximum(Calendar.DAY_OF_MONTH);

        if ((day < 1) || (day > lastDayOfMonth)) {
            throw new IllegalArgumentException("Day must be in the range 1 - " + lastDayOfMonth + " inclusive: " + day);
        }
    }

    public void clear() {
        isNull = true;
    }

    /**
     * clear all aspects of the time that are not used
     */
    private void clearDate(Calendar cal) {
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.AM_PM, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public void copyObject(BusinessValueHolder object) {
        if (!(object instanceof Date)) {
            throw new IllegalArgumentException("Can only copy the value of  a Date object");
        }
        setValue((Date) object);
    }

    public java.util.Date dateValue() {
        return isNull ? null : date;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Date) {
            Date d = (Date) obj;
            return d.date.equals(date);
        }
        return super.equals(obj);
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    public int getYear() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * Return true if the date is blank
     */
    public boolean isEmpty() {
        return isNull;
    }

    /**
     * returns true if the date of this object has the same value as the
     * specified date
     */
    public boolean isEqualTo(Magnitude date) {
        if (date instanceof Date) {
            if (isNull) {
                return date.isEmpty();
            }

            return this.date.equals(((Date) date).date);
        } else {
            throw new IllegalArgumentException("Parameter must be of type Time");
        }
    }

    /**
     * returns true if the time of this object is earlier than the specified
     * time
     */
    public boolean isLessThan(Magnitude date) {
        if (date instanceof Date) {
            return !isNull && !date.isEmpty() && this.date.before(((Date) date).date);
        } else {
            throw new IllegalArgumentException("Parameter must be of type Time");
        }
    }

    /**
     * Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT
     * represented by this date.
     */
    public long longValue() {
        return date.getTime();
    }

    public void parseUserEntry(String dateString) throws ValueParseException {
        dateString = dateString.trim();

        if (dateString.equals("")) {
            clear();
        } else {
            String str = dateString.toLowerCase();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            clearDate(cal);

            if (str.equals("today") || str.equals("now")) {
                today();
                return;
            } else if (str.startsWith("+")) {
                int days;

                // support for +1, +1d, +1w, +1m
                int unit = Calendar.DATE;
                int multiplier = 1;
                if (str.endsWith("w")) {
                    multiplier = 7;
                    str = str.substring(0, str.length() - 1);
                } else if (str.endsWith("m")) {
                    unit = Calendar.MONTH;
                    str = str.substring(0, str.length() - 1);
                }

                days = Integer.valueOf(str.substring(1)).intValue();
                cal.setTime(date);
                cal.add(unit, days * multiplier);

            } else if (str.startsWith("-")) {
                int days;

                // support for +1, +1d, +1w, +1m
                int unit = Calendar.DATE;
                int multiplier = 1;
                if (str.endsWith("w")) {
                    multiplier = 7;
                    str = str.substring(0, str.length() - 1);
                } else if (str.endsWith("m")) {
                    unit = Calendar.MONTH;
                    str = str.substring(0, str.length() - 1);
                }

                days = Integer.valueOf(str.substring(1)).intValue();
                cal.setTime(date);
                cal.add(unit, -days * multiplier);

            } else {
                DateFormat[] formats = new DateFormat[] { LONG_FORMAT, MEDIUM_FORMAT, SHORT_FORMAT, ISO_LONG, ISO_SHORT };

                for (int i = 0; i < formats.length; i++) {
                    try {
                        cal.setTime(formats[i].parse(dateString));
                        break;
                    } catch (ParseException e) {
                        if ((i + 1) == formats.length) {
                            throw new ValueParseException("Invalid date " + dateString, e);
                        }
                    }
                }

            }

            set(cal);
        }
    }

    public void readExternal(java.io.ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
        isNull = in.readBoolean();
        date.setTime(in.readLong());
    }

    /**
     * Reset this date so it contains the current date.
     * 

     */
    public void reset() {
        today();
    }

    public void restoreFromEncodedString(String data) {
        if (data.equals("NULL")) {
            clear();
        } else {
            int year = Integer.valueOf(data.substring(0, 4)).intValue();
            int month = Integer.valueOf(data.substring(4, 6)).intValue();
            int day = Integer.valueOf(data.substring(6)).intValue();
            setValue(year, month, day);
        }
    }

    private boolean sameAs(Date as, int field) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(as.date);

        return c.get(field) == c2.get(field);
    }

    public boolean sameDayAs(Date as) {
        return sameAs(as, Calendar.DAY_OF_YEAR);
    }

    public boolean sameMonthAs(Date as) {
        return sameAs(as, Calendar.MONTH);
    }

    public boolean sameWeekAs(Date as) {
        return sameAs(as, Calendar.WEEK_OF_YEAR);
    }

    public boolean sameYearAs(Date as) {
        return sameAs(as, Calendar.YEAR);
    }

    public String asEncodedString() {
        if (isEmpty()) {
            return "NULL";
        } else {
            Calendar cal = calendarValue();
            StringBuffer data = new StringBuffer(8);
            String year = String.valueOf(cal.get(Calendar.YEAR));
            data.append("0000".substring(0, 4 - year.length()));
            data.append(year);

            int month = cal.get(Calendar.MONTH) + 1;
            data.append((month <= 9) ? "0" : "");
            data.append(month);

            int day = cal.get(Calendar.DAY_OF_MONTH);
            data.append((day <= 9) ? "0" : "");
            data.append(day);

            return data.toString();
        }
    }

    private void set(Calendar cal) {
        date = cal.getTime();
        isNull = false; // moved from various other methods in
                                   // code...
    }

    public void setValue(Date date) {
        if (date == null || date.isEmpty()) {
            clear();
        } else {
            setValue(new java.util.Date(date.longValue()));
        }
    }

    /**
     * Sets this object's date to be the same as the specified day, month and
     * year.
     */
    public void setValue(int year, int month, int day) {
        checkDate(year, month, day);

        Calendar cal = Calendar.getInstance();
        clearDate(cal);
        cal.set(year, month - 1, day);
        set(cal);
    }

    public void setValue(java.util.Date date) {
        if (date == null) {
            isNull = true;
        } else {
            Calendar cal = Calendar.getInstance();

            cal.setTime(date);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            set(cal);
        }
    }

    public Title title() {
        return new Title(isNull ? "" : MEDIUM_FORMAT.format(date));
    }

    /**
     * Sets this date value to todays date
     */
    public void today() {
        Calendar cal = Calendar.getInstance();

        if (clock == null) {
            throw new NakedObjectRuntimeException("Clock not set up");
        }
        long time = clock.getTime();
        java.util.Date d = new java.util.Date(time);
        cal.setTime(d);

        clearDate(cal);
        set(cal);
    }

    public void toStartOfMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.MONTH, c.getMinimum(Calendar.MONTH));
        date = c.getTime();
    }

    public void toStartOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.DAY_OF_WEEK_IN_MONTH, c.getMinimum(Calendar.DAY_OF_WEEK_IN_MONTH));
        date = c.getTime();
    }

    public void toStartOfYear() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.set(Calendar.DAY_OF_YEAR, c.getMinimum(Calendar.DAY_OF_YEAR));
        date = c.getTime();
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