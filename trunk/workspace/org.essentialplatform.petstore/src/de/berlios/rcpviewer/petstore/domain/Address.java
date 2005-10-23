package de.berlios.rcpviewer.petstore.domain;

import de.berlios.rcpviewer.progmodel.extended.FieldLengthOf;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Invisible;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Optional;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.extended.SaveOperation;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;



/**
 * Wholly owned component of {@link Customer} that represents  the customer's 
 * Address.
 * 
 * <p>
 * A mutable Address is always associated with a Customer.  When the Customer
 * places a {@link CustomerOrder} then a copy of the Address is taken and 
 * associated with the order.  This copy is configure to be immutable - it is
 * a snapshot of the Customer's Address at the time that the Order was placed.  
 * 
 * <p>
 * TODOs
 * <ul>
 * <li>TODO: Hibernate mapping to as a component in either T_CUSTOMER or T_ORDER
 * </ul>
 * 
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * @author Dan Haywood
 */
@InDomain
@DescribedAs("Postal address, held for both billing and shipping purposes")
public class Address {
	
	/**
	 * Constructor.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> No-arg constructor required by platform.
     * <li> Normally the state for Address is built up by the user (through the
     *      UI) as part of building up the state for the {@link Customer}.  In 
     *      this case there is no additional initialization to be performed. 
     * <li> When a {@link CustomerOrder} is created though, then a copy of the
     *      Customer's current Address must be taken.  The 
     *      {@link #init(Address)} method allows the Customer object to do 
     *      this - in effect it acts like a copy constructor.
     * </ul>
     * </i>
	 */
    public Address() {
    }
    

    /**
     * Initialize Address using another Address.
     * 
     * <p>
     * The resultant object will be immutable.
     * 
     * <p>
     * Used when copying down an Address from its {@link Customer} to place
     * an {@link Order}.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Package level visibility (<code>public</code> visibility would 
     *      expose this as an operation).
     * </ul>
     * </i>
     * 
     * @param address to be copied
     */
    void init(final Address address) {
    	setCity(address.getCity());
    	setCountry(address.getCountry());
    	setState(address.getState());
    	setStreet1(address.getStreet1());
    	setStreet2(address.getStreet2());
    	setZipcode(address.getZipcode());
    	
    	setMutable(false);
    }
    
    /**
     * First line of the address (typically the street name).
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(1)
    @FieldLengthOf(30)
    @MaxLengthOf(50)
    @DescribedAs("First line of the address (typically street name)")
    public String getStreet1() {
        return _street1;
    }
    public void setStreet1(final String street1) {
        _street1 = street1;
    }
    private String _street1;



    /**
     * Second line of the street address. if any.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(2)
    @Optional
    @FieldLengthOf(30)
    @MaxLengthOf(50)
    @DescribedAs("Second line of the address, if any")
    public String getStreet2() {
        return _street2;
    }
    public void setStreet2(final String street2) {
        _street2 = street2;
    }
    private String _street2;



    /**
     * The city (or closest major town) for this address.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(3)
    @DescribedAs("The city (or closest major town) for this address.")
    @MaxLengthOf(25)
    public String getCity() {
        return _city;
    }
    public void setCity(final String city) {
        _city = city;
    }
    private String _city;

    

    /**
     * State (if US) or region (non US).
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @Order(4)
    @MaxLengthOf(20)
    @DescribedAs("State (if US) or region (non US).")
    public String getState(  ) {
        return _state;
    }
    public void setState( String state ) {
        _state = state;
    }
    private String _state;
    

    /**
     * Zip code (if US) or postal code (non US).
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @Order(5)
    @MaxLengthOf(11)
    @DescribedAs("Zip code (if US) or postal code (non US).")
    public String getZipcode() {
        return _zipcode;
    }
    public void setZipcode(String zipcode) {
        _zipcode = zipcode;
    }
    public IPrerequisites setZipCode(String zipCode) {
    	return Prerequisites.require(isValidZip(zipCode), "Invalid zip code");
    }
    private String _zipcode;
    /**
     * Helper method for validating zip code.
     * 
     * <p>
     * TODO - need to complete, should be localized.
     * 
     * @param zipCode
     * @return
     */
    private boolean isValidZip(final String zipCode) {
    	return zipCode.length() > 0;
    }

    
    /**
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @Order(6)
    @DescribedAs("Country in which this address resides")
    public Country getCountry() {
        return _country;
    }
    public void setCountry(final Country country) {
        _country = country;
    }
    private Country _country;

    
    /**
     * Whether this Address' attributes can be modified.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul> 
     * <li> The {@link Invisible} annotation indicates that the attribute
     *      should not be displayed in the UI.  It will still be persisted, 
     *      however.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </i>
     */
    @Invisible
    private boolean isMutable() {
		return _mutable;
	}
    private void setMutable(boolean mutable) {
		this._mutable = mutable;
	}
    private boolean _mutable;
    

    
    /**
     * Save the object in the persistent object store (either creating if was 
     * not already persistent, or updating if was.)
     *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link SaveOperation} annotation indicates that it is
     *      this method that is the save method.  By convention the method is
     *      called <code>save</code>, but it must be <code>public</code>, take 
     *      no arguments and return <code>void</code>.
     * <li> The <code>unusableReason</code> effectively disables the save
     *      operation (this is a component object).
     * <li> No {@link Order} annotation is required for the save operation.
     * </ul>
     * </i>
     */
    @SaveOperation(unusableReason="Save owning Customer.")
    public void save() {
    	// nothing to do since unusable, see Pre() method  
    }

}
