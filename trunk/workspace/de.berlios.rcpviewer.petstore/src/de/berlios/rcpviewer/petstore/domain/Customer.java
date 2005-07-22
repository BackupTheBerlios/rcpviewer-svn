/*
 * Created on Feb 22, 2003
 */
package de.berlios.rcpviewer.petstore.domain;

import java.io.Serializable;

import de.berlios.rcpviewer.progmodel.extended.AppContainer;
import de.berlios.rcpviewer.progmodel.extended.BusinessKey;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.standard.ContainerOf;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.LowerBoundOf;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.*;


/**
 * Customer of the petstore, holding account details, address details and
 * records previous {@link Order}s placed by this customer.
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
public class Customer {

	/**
	 * No-arg constructor, required for persistence layer.
	 * 
	 * <p>
	 * Sets up wholly contained objects.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 */
    public Customer() {
    	_account = new Account();
    	_address = new Address();
        _creditCard = new CreditCard(  );
    }

    /**
     * Immutable user Id of the Customer, same as the Account that it 
     * references.
     * 
     * <p>
     * Automatically assigned by the persistence layer.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     */
    @Order(1)
    @DescribedAs("Unique identifier for this customer, allocated by the Petstore")
    @MaxLengthOf(10)
    public String getUserId() {
        return  _account != null
               ? _account.getUserId()
               : "";
    }
    private void setUserId(String userId) {
        _account.setUserId( userId );
    }


    /**
     * Immutable reference to the {@link Account} related to this Customer.
     * 
     * <p>
     * The Account is wholly-owned and cannot be shared between Customers.
     * 
     * <p>
     * Implementation notes: modeled as a Component in Hibernate.
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
    public Account getAccount() {
        return _account;
    }
    private final Account _account;

    
    /**
     * Family or last name for this customer.
     * 
     * <p>
     * In US and Western Europe the family name is written after the given
     * name.
     * 
     * <p>
     * Note that <code>(firstname, lastname)</code> is <i>not</i> considered to 
     * be an alternate key.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     */
    @Order(3)
    @MaxLengthOf(50)
    @DescribedAs("Given (or first) name")
    public String getFirstname() {
        return _firstname;
    }
    public void setFirstname(final String firstname) {
        _firstname = firstname;
    }
    private String _firstname;


    /**
     * Family or last name for this customer.
     * 
     * <p>
     * In US and Western Europe the family name is written after the given
     * name.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     */
    @Order(4)
    @MaxLengthOf(50)
    @DescribedAs("Family (or last) name")
    public String getLastname() {
        return _lastname;
    }
    public void setLastname(final String lastname) {
        _lastname = lastname;
    }
    private String _lastname;

    
    /**
     * <p>
     * <i>Programming model notes: 
     * <ul>
     * <li>the <code>@AlternateKey</code> annotation
     *     indicates that the attribute can be used (possibly in conjunction 
     *     with other attributes) as a unique identifier of the object instance 
     *     with respect to other object instances.
     * <li>mandatory since no <code>@Optional</code> annotation.
     * <li>field length not specified so will be defaulted from 
     *     <code>@MaxLength</code> annotation.
     * </i>
     * 
     * @return
     */
    @Order(3)
    @BusinessKey("email")
    @MaxLengthOf(60)
    public String getEmail() { 
        return _email;
    }
    public void setEmail(final String email) {
        _email = email;
    }
    /**
     * The proposed email must be valid
     * @param email - new value proposed for this attribute (as typed in by user).
     * @return
     */
    public IPrerequisites setEmailPre(final String email) {
        return require(isValidEmailFormat(email), "Invalid email");
    }
    /**
     * For this demo, simply requires that a the email contains an '@'
     * 
     * @return true if the email does contain an '@', false otherwise.
     */
    private boolean isValidEmailFormat(final String email) {
		return email != null && email.contains("@");
	}
	private String _email;



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
    @Order(4)
    @DescribedAs("The preferred language for this customer")
    public Language getLanguage() {
        return _language;
    }
    public void setLanguage(final Language language) {
        _language = language;
    }
    private Language _language;



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
    @Order(5)
    @MaxLengthOf(10)
    public String getTelephone() {
        return _telephone;
    }
    public void setTelephone(String telephone) {
        _telephone = telephone;
    }
    private String _telephone;



    /**
     * Immutable reference to the {@link Address} related to this Customer
     * (though the Address itself is mutable).
     * 
     * <p>
     * The Address is wholly-owned and cannot be shared between Customers.  
     * When an {@link Order} is created it <i>is</i> associated with an 
     * Address, however this is a copy of the Address associated with the
     * Customer placing the Order.  This saves us having to version the
     * Address object of the Customer (in other words, we denormalize this
     * information within the Order).
     * 
     * <p>
     * Implementation notes: modeled as a Component in Hibernate.
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
    @ContainerOf
    public Address getAddress() {
        return _address;
    }
    private final Address _address;


    
    /**
     * Immutable reference to the {@link CreditCard} related to this Customer
     * (though the CreditCard itself is mutable).
     * 
     * <p>
     * Implementation notes: modeled as a Component in Hibernate.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     */
    @Order(7)
    @ContainerOf
    public CreditCard getCreditCard() {
        return _creditCard;
    }
    private final CreditCard _creditCard;


    public CustomerOrder placeOrder() {
    	CustomerOrder order = 
    		getAppContainer().createTransient(CustomerOrder.class);
    	order.init(this);
    	return order;
    }


    /**
     * Application container that controls lifecycle of this pojo, injected
     * by the framework.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     */
	public AppContainer getAppContainer() {
		return _appContainer;
	}
	/**
	 * Allows framework to inject application container.
	 */
	public void setAppContainer(final AppContainer appContainer) {
		_appContainer = appContainer;
	}
	private AppContainer _appContainer;


}
