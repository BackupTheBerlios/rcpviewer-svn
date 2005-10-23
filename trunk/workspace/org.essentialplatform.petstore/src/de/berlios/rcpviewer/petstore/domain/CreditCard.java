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
import de.berlios.rcpviewer.progmodel.extended.Optional;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.extended.SaveOperation;


/**
 * Details of a credit card account held by a {@link Customer}.
 * 
 * <p>
 * Credit card details are also held by {@link CustomerOrder}, when the order 
 * is placed.
 * 
 * <p>
 * TODOs
 * <ul>
 * <li>TODO: Hibernate mapping to T_CREDIT_CARD
 * </ul>
 * 
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * @author Dan Haywood
 */
@InDomain
@DescribedAs("Details of a credit card account held by a Customer.")
public class CreditCard {

	/**
	 * Constructor.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> No-arg constructor required by platform.
     * <li> Normally the state for CreditCard is built up by the user (through 
     *      the UI) as part of building up the state for the {@link Customer}.  
     *      In this case there is no additional initialization to be performed. 
     * <li> When a {@link CustomerOrder} is created though, then a copy of the
     *      Customer's current CreditCard must be taken.  The 
     *      {@link #init(CreditCard)} method allows the Customer object to do 
     *      this - in effect it acts like a copy constructor.
     * </ul>
     * </i>
     * 
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
     * an {@link CustomerOrder}.
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
     * @param creditCard
     */
    void init(final CreditCard creditCard) {
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
     * <li> The {@link ImmutableOncePersisted} annotation will prevent
     *      this attribute from being edited through the UI once the credit card
     *      object has been saved.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </i>
     */
    @Order(1)
    @MaxLengthOf(10)
    @ImmutableOncePersisted
    @DescribedAs("Unique identifier for this card (for this type of card).")
    public String getNumber() {
        return _number;
    }
    public void setNumber(final String number) {
    	_number = number;
    }
    
    private String _number;

    

    /**
     * The clearing organization that operates this type of credit card.
     *
     * <p>
     * Note that although NONE is the default, there is a prerequisite on the 
     * save operation (@link #savePre()}) which requires that some other value 
     * for this enumerated type must have been selected before the object can 
     * be saved.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Using an <code>enum</code> means that the attribute will be 
     *      rendered as a drop-down (or possibly radio buttons) in the UI.  
     *      Using an enum makes the attribute implicitly mandatory.
     * <li> the {@link #setTypeDefaults(Type[])} sets the {@link Type#NONE} as
     *      the default.
     * <li> the {@link ImmutableOncePersisted} annotation will prevent
     *      this attribute from being edited through the UI once the credit card
     *      object has been saved.
     * <li> the use of NONE as the default value is slightly contrived; it has
     *      been chosen primarily to demonstrate the use of prerequisites on
     *      the save operation ({@link #savePre()}).  A simpler alternative 
     *      would have been to not have a NONE value and dispense with the
     *      <code>savePre()</code> method entirely.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(2)
    @ImmutableOncePersisted
    @DescribedAs("The clearing organization that operates this type of credit card.")
    public Type getType() {
        return _type;
    }
    public void setType(final Type type) {
    	_type = type;
    }
    public void setTypeDefaults(final Type[] type) {
    	type[0] = Type.NONE;
    }
    private Type _type;
    public static enum Type {
    	NONE,VISA,MASTERCARD,AMEX
    }
    

    
    /**
     * Expiry date, as written on this credit card
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link Mask} annotation uses set of conventions compatible with
     *      <code>java.util.DateFormat</code>.   The minimum, maximum and field
     *      lengths are all inferred from the length of the mask string.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @Order(3)
    @Mask("MM-yyyy")
    @DescribedAs("Expiry date, as written on this credit card")
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
     * Standard operation for initially persisting or updating this object;
     * unusable for this class of object, see reason below.
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
     * <li> However, even though save cannot be invoked directly on this object,
     *      the save operation must still be specified.  This is so that any
     *      prerequisites that it specifies (in the {@link #savePre()}
     *      method) can be identified.  These prerequisites will be enforced
     *      when this object is saved via its owning Customer (persistence
     *      through reachability).
     * <li> No {@link Order} annotation is required for the save operation.
     * </ul>
     */
    @SaveOperation(unusableReason="Save owning Customer.")
    public void save() {
    	// nothing to do since unusable, see Pre() method  
    }
    /**
     * Valid credit card type must be selected (cannot be {@link Type#NONE}). 
     * 
     * @return
     */
    public IPrerequisites savePre() {
    	return Prerequisites.require(
    			getType() != Type.NONE, "Must select credit card type.");
    }

}
