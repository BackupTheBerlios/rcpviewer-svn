package org.nakedobjects.example.ecs;

import org.nakedobjects.application.BusinessObjectContainer;
import org.nakedobjects.application.Title;
import org.nakedobjects.application.control.ActionAbout;
import org.nakedobjects.application.control.FieldAbout;
import org.nakedobjects.application.valueholder.Option;
import org.nakedobjects.application.valueholder.TextString;

import java.util.Vector;


public class Location {
    private final TextString streetAddress;
    private final TextString knownAs;
    private City city;
    private Customer customer;
    private boolean isDirty;
    private transient BusinessObjectContainer container;
    
    private Option type = new Option(new String[] {"One", "Two", "Threee"});
    
    public Option getType() {
        return type;
    }
    
    public Location() {
        streetAddress = new TextString();
        knownAs = new TextString();
    }

    public void aboutActionNewBooking(ActionAbout about, Location location) {
        about.setDescription(
                "Giving one location to another location creates a new booking going from the given location to the recieving location.");
        about.unusableOnCondition(equals(location), "Two different locations are required");

        boolean sameCity = getCity() != null && location != null && getCity().equals(location.getCity());

        about.unusableOnCondition(! sameCity, "Locations must be in the same city");
        about.changeNameIfUsable("New booking from " + location + 
                                " to " + title());
    }
    

    public void explorationActionExplorationMethod() {
        Vector instances = container.allInstances(City.class);
        if(instances.size() > 0)
        {
            city = (City) instances.elementAt(0);
        }
        instances = container.allInstances(Customer.class);
        if(instances.size() > 0)
        {
            customer = (Customer) instances.elementAt(0);
        }
    }
    
    public void debugActionDebugMethod() {
        System.out.println(this);
        System.out.println("  " + knownAs.titleString());
        System.out.println("  " + streetAddress.titleString());
        System.out.println("  " + city.title());
        System.out.println("  " + customer.title());
    }

    public Booking actionNewBooking(Location location) {
        Booking booking = (Booking) container.createTransientInstance(Booking.class);
        Customer customer = location.getCustomer();

        booking.setPickUp(location);
        booking.setDropOff(this);
        booking.setCity(location.getCity());

        container.makePersistent(booking);

        if (customer != null) {
            booking.associateCustomer(customer);
            booking.setPaymentMethod(customer.getPreferredPaymentMethod());
        }

        return booking;
    }


    public Booking actionSlowAction() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Booking booking = (Booking) container.createTransientInstance(Booking.class);
        booking.setPickUp(this);
        booking.setCity(getCity());
        container.makePersistent(booking);

        Customer customer = getCustomer();
        if (customer != null) {
            booking.associateCustomer(customer);
            booking.setPaymentMethod(customer.getPreferredPaymentMethod());
        }

        return booking;
    }

    public void aboutCity(FieldAbout  about, City city) {
        about.setName("City for this location");
   //     about.unmodifiable();
    }
    
    public void setContainer(BusinessObjectContainer container) {
        this.container = container;
    }
    
    public City getCity() {
        container.resolve(city);
        return city;
    }

    public Customer getCustomer() {
        container.resolve(customer);

        return customer;
    }

    public final TextString getKnownAs() {
        return knownAs;
    }

    public final TextString getStreetAddress() {
        return streetAddress;
    }

    public void setCity(City newCity) {
        city = newCity;
        isDirty = true;
    }

    public boolean isDirty() {
        return isDirty;
    }
    
    public void clearDirty() {
        isDirty = false;
    }
    
    public void markDirty() {
        isDirty = true;
    }
    
    public void associateCustomer(Customer newCustomer) {
        newCustomer.addToLocations(this);
    }
    
    public void dissociateCustomer(Customer newCustomer) {
        newCustomer.removeFromLocations(this);
    }
    
    public void setCustomer(Customer newCustomer) {
        customer = newCustomer;
        isDirty = true;
   }

    public Title title() {
        if (knownAs.isEmpty()) {
            return streetAddress.title().append(",", getCity());
        } else {
            return knownAs.title().append(",", getCity());
        }
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
