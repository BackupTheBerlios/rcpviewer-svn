/*
 * Created on Feb 22, 2003
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code Template
 */
package de.berlios.rcpviewer.petstore.domain;

import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.invisible;

import java.io.Serializable;

import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.ImmutableOncePersisted;
import de.berlios.rcpviewer.progmodel.extended.Invisible;
import de.berlios.rcpviewer.progmodel.extended.Mask;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Order;


/**
 * Holds a credit card details for a {@link Customer}.
 * 
 * <p>
 * Is also held on an Order when placed.
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
public class CreditCard {

	/**
	 * No-arg constructor, required for persistence layer.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 */
    public CreditCard() {
    }

    /**
     * Initialize CreditCard using another CreditCard.
     * 
     * <p>
     * The resultant object will be immutable.
     * 
     * <p>
     * Used when copying down a CreditCard from its {@link Customer} to place
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
    public void init(final CreditCard creditCard) {
    	setExpiryDate(creditCard.getExpiryDate());
    	setNumber(creditCard.getNumber());
    	setType(creditCard.getType());

    	setMutable(false);
    }
    
    
    /**
     * Unique identifier for this card (for this type of card).
     * 
     * <p>
     * <i>
     * Programming Model notes: 
     * <ul>
     * <li>the <code>@ImmutableOncePersisted</code> annotation will prevent
     *     this attribute from being edited through the UI once the credit card
     *     object has been saved.  (It would also have been possible to put this
     *     annotation on the entire class, to affect every attribute).
     * <li>the <code>@FieldLengthOf</code> annotation is not specified so 
     *     defaults from <code>@MaxLengthOf</code>.
     * </i>
     */
    @Order(1)
    @DescribedAs("Unique identifier for this card (for this type of card).")
    @MaxLengthOf(10) 
    @ImmutableOncePersisted
    public String getNumber() {
        return _number;
    }
    public void setNumber(final String number) {
    	_number = number;
    }
    
    private String _number;

    

    public static enum Type {
    	VISA,MASTERCARD,AMEX
    }
    /**
     * The clearing organization that operates this type of credit card.
     * 
     * <p>
     * <i>Programming Model notes: using an enumeration which should be
     * rendered as a drop-down or radio buttons (depending on number of 
     * elements in the enumeration.</i>
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
    @Order(2)
    @DescribedAs("The clearing organization that operates this type of credit card")
    public Type getType() {
        return _type;
    }
    public void setType(Type type) {
    	_type = type;
    }
    private Type _type;
    

    
    /**
     * Expiry date, as written on this credit card
     * 
     * <p>
     * <i>
     * Programming Model notes: <code>@Mask</code> uses the same conventions
     * as <code>java.util.DateFormat</code>.
     * </i>
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     */
    @Order(2)
    @DescribedAs("Expiry date, as written on this credit card")
    @Mask("MM-yyyy")
    public String getExpiryDate() {
        return _expiryDate;
    }
    public void setExpiryDate(final String expiryDate) {
        _expiryDate = expiryDate;
    }
    private String _expiryDate;


    /**
     * Whether this CreditCard' attributes can be modified.
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
