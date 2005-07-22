package de.berlios.rcpviewer.petstore.domain;

import de.berlios.rcpviewer.progmodel.extended.FieldLengthOf;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Invisible;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Optional;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.*;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.LowerBoundOf;



/**
 * Represents a Customer's Address.
 * 
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * <p>
 * <i>
 * Programming Model notes:
 * <ul>
 * <li> ...
 * </ul>
 * </i>
 * 
 * @author Dan Haywood
 */
@InDomain
@DescribedAs("Postal address, held for both billing and shipping purposes")
public class Address {
	
	/**
	 * No-arg constructor, required for persistence layer.
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
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
     * <li> ...
     * </ul>
     * </i>
     *
     * @param address
     */
    public void init(final Address address) {
    	setCity(address.getCity());
    	setCountry(address.getCountry());
    	setState(address.getState());
    	setStreet1(address.getStreet1());
    	setStreet2(address.getStreet2());
    	setZipcode(address.getZipcode());
    	
    	setMutable(false);
    }
    
    /**
     * The first line of the address (typically the street name).
     * 
     * <p>
     * <i>
     * Programming Notes: mandatory since there is no <code>@Optional</code> 
     * attribute.
     * </i>
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(1)
    @DescribedAs("First line of the address (typically street name")
    @FieldLengthOf(30)
    @MaxLengthOf(50)
    public String getStreet1() {
        return _street1;
    }
    public void setStreet1(final String street1) {
        _street1 = street1;
    }
    private String _street1;



    /**
     * 
     * @return
     */
    @Order(2)
    @DescribedAs("Secondary street name")
    @Optional
    @FieldLengthOf(30)
    @MaxLengthOf(50)
    public String getStreet2() {
        return _street2;
    }
    public void setStreet2(final String street2) {
        _street2 = street2;
    }
    private String _street2;



    /**
     * 
     * <p>
     * <i>
     * Programming Notes: absence of <code>@FieldLength</code> means that
     * it is defaulted to that of <code>@MaxLength</code>.
     * </i>
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
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
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     */
    @Order(4)
    @DescribedAs("State (if US) or region (non US)")
    @MaxLengthOf(20)
    public String getState(  ) {
        return _state;
    }
    public void setState( String state ) {
        _state = state;
    }
    private String _state;
    

    /**
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     */
    @Order(5)
    @DescribedAs("Zip code (if US) or postal code (non US)")
    @MaxLengthOf(11)
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
     * <li> ...
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
     * <li> The <code>Invisible</code> annotation indicates that the attribute
     *      should not be displayed in the UI.  (A more verbose way would to
     *      have returned a <code>Prerequisites.invisible()</code> from a 
     *      <code>...Pre()</code> method).
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
    
}
