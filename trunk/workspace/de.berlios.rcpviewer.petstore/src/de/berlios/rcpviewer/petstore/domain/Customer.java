package de.berlios.rcpviewer.petstore.domain;

import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.require;
import de.berlios.rcpviewer.progmodel.extended.BusinessKey;
import de.berlios.rcpviewer.progmodel.extended.IAppContainer;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Optional;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.extended.Lookup;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.extended.SaveOperation;
import de.berlios.rcpviewer.progmodel.standard.ContainerOf;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;


/**
 * Customer of the petstore, holding account details, address details and
 * records previous {@link CustomerOrder}s placed by this customer.
 * 
 * <p>
 * TODOs
 * <ul>
 * <li>TODO: Hibernate mapping to T_CUSTOMER
 * </ul>
 * 
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * @author Dan Haywood
 */
@InDomain
@Lifecycle(searchable=true,instantiable=true,saveable=true)
public class Customer {

	/**
	 * Constructor.
	 *  
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> No-arg constructor required by platform.  The Customer's state is
     *      empty, but the various constraints on the attributes of Customer,
     *      as well as the {@link #save()} operation itself, will mean that
     *      the object cannot be saved until sufficient state has been 
     *      entered by the user.
     * <li> Sets up wholly owned components ({@link Account}, {@link Address}
     *      and {@link CreditCard}.  Like the Customer itself, their state
     *      will initially be empty and  
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
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
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
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @Order(2)
    public Account getAccount() {
        return _account;
    }
    private void setAccount(final Account account) {
    	_account = account;
    }
    private Account _account;

    
    /**
     * Family or last name for this customer.
     * 
     * <p>
     * In US and Western Europe the family name is written after the given
     * name.
     * 
     * <p>
     * Note that <code>(firstname, lastname)</code> is <i>not</i> considered to 
     * be an business key.
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
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
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
     * <li> The {@link BusinessKey} annotation indicates that the attribute 
     *      can be used (possibly in conjunction with other attributes) as a 
     *      unique identifier of the object instance with respect to other 
     *      object instances.
     * <li> The {@link #setEmailPre(String)} method allows the proposed value 
     *      for the email to be validated and potentially vetoed.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </i>
     * 
     * @return
     */
    @Order(5)
    @BusinessKey(name="email")
    @MaxLengthOf(60)
    public String getEmail() { 
        return _email;
    }
    public void setEmail(final String email) {
        _email = email;
    }
    /**
     * The proposed email must be valid; for this demo, simply requires that 
     * the email contains an '@'.
     * 
     * @param email - new value proposed for this attribute (as typed in by user).
     * @return
     */
    public IPrerequisites setEmailPre(final String email) {
        return require(isValidEmailFormat(email), "Invalid email");
    }
    private boolean isValidEmailFormat(final String email) {
		return email != null && email.contains("@");
	}
	private String _email;



	/**
	 * Preferred language for this customer.
	 *  
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Since the referenced object is a {@link Lookup}, the reference 
     *      is represented in the UI as a drop-down.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
	 */
    @Order(6)
    @DescribedAs("The preferred language for this customer")
    public Language getLanguage() {
        return _language;
    }
    public void setLanguage(final Language language) {
        _language = language;
    }
    private Language _language;



    /**
     * Telephone number to contact this customer.
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
    @Order(7)
    @MaxLengthOf(10)
    @Optional
    @DescribedAs("A telephone number to contact this customer.")
    public String getTelephone() {
        return _telephone;
    }
    public void setTelephone(String telephone) {
        _telephone = telephone;
    }
    private String _telephone;



    /**
     * Immutable reference to the {@link Address} related to this Customer;
     * the {@link Address} itself is mutable, however.
     * 
     * <p>
     * Each Address is wholly-owned by a Customer and cannot be shared between 
     * Customers.  When a Customer is instantiated it sets up its Address
     * object.
     * 
     * <p>
     * An Address can also be associated with a {@link CustomerOrder}.  When
     * such an order is created it a copy of the Address associated with the
     * Customer placing the order.  This saves us having to version the
     * Address object of the Customer.  In other words, we denormalize this
     * information within the order.
     * 
     * <p>
     * Implementation notes: modeled as a Component in Hibernate.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link ContainerOf} annotation indicates that the referenced
     *      object is wholly owned by this object.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @Order(8)
    @ContainerOf
    public Address getAddress() {
        return _address;
    }
    private void setAddress(final Address address) {
    	_address = address;
    }
    private Address _address;


    
    /**
     * Immutable reference to the {@link CreditCard} related to this Customer;
     * the {@link CreditCard} itself is mutable, however.
     * 
     * <p>
     * Each CreditCard is wholly-owned by a Customer and cannot be shared between 
     * Customers.  When a Customer is instantiated it sets up its CreditCard
     * object.
     * 
     * <p>
     * An CreditCard can also be associated with a {@link CustomerOrder}.  When
     * such an order is created it a copy of the Address associated with the
     * Customer placing the order.  This saves us having to version the
     * CreditCard object of the Customer.  In other words, we denormalize this
     * information within the order.
     * 
     * <p>
     * Implementation notes: modeled as a Component in Hibernate.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link ContainerOf} annotation indicates that the referenced
     *      object is wholly owned by this object.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @Order(9)
    @ContainerOf
    public CreditCard getCreditCard() {
        return _creditCard;
    }
    private void setCreditCard(final CreditCard creditCard) {
    	_creditCard = creditCard;
    }
    private CreditCard _creditCard;


    /**
     * Creates a new order ({@link CustomerOrder}) for this Customer.
     * 
     * <p>
     * Initially the order will have no order lines and will not have been
     * persisted.
     *   
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link #placeOrderPre()} method defines prerequisites for
     *      invoking this operation.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
     */
    @Order(1)
    @DescribedAs("Creates a new order for this Customer.  Initially the order will have no order lines")
    public CustomerOrder placeOrder() {
    	CustomerOrder order = 
    		getAppContainer().createTransient(CustomerOrder.class);
    	order.init(this);
    	return order;
    }
    /**
     * The customer must have been persisted before being able to place orders.
     * 
     * @return
     */
    public IPrerequisites placeOrderPre() {
    	return Prerequisites.require(isPersistent(), "Must be persistent");
    }
    
    

    /**
     * Invoked when the customer is first saved, or at any time subsequently.
     *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link SaveOperation} annotation indicates that it is
     *      this method that is the save method.  By convention the method is
     *      called <code>save</code>, but it must be <code>public</code>, take 
     *      no arguments and return <code>void</code>.
     * <li> Although the implementations of both {@link #save()} and 
     *      {@link #savePre()} are essentially no-ops, nevertheless the 
     *      platform does enforce a number of prerequisites implied by the
     *      annotations on the various constraints.
     * <li> For example, the customer cannot be saved until the mandatory  
     *      <code>firstName</code> attribute has been supplied.
     * <li> Furthermore, these prerequisites are transitive.  Customer has a
     *      mandatory reference to both an {@link Address} and a
     *      {@link CreditCard}; these are created in the constructor and will
     *      initially be transient also.  When the Customer is persisted then 
     *      the Address and the CreditCard will also be persisted - so-called
     *      <i>persistence by reachability</i>.  However, the prerequisites
     *      for persisting these component objects must also be honoured.  This
     *      is all taken care of transparently by the platform.
     * <li> No {@link Order} annotation is required for the save operation.
     * </ul>
     * </i>
     * 
     */
    @SaveOperation
    public void save() {
    	// in fact, nothing to do.  
    }
    /**
     * No constraints.  
     * 
     * <p>
     * However, any save constraints of component objects that are persisted by
     * reachability (namely, {@link Account}, {@link Address} and 
     * {@link CreditCard} will be persisted.  This include constraints inferred
     * from annotations on attributes - eg non-{@link Optional} attributes - and
     * constraints explicitly specified on a <code>savePre()</code> method.
     * 
     * @return
     */
    public IPrerequisites savePre() {
    	return Prerequisites.none();
    }



    /**
     * Helper method to allow this pojo to know where it is in its lifecycle
     * (not yet persisted or persisted).
     * 
     * @see #getAppContainer()
     * @return
     */
    private boolean isPersistent() {
		return getAppContainer().isPersistent(this);
	}

    
    /**
     * Application container that controls lifecycle of this pojo.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Injected by the platform automatically.
     * </ul>
     * </i>
     */
	public IAppContainer getAppContainer() {
		return _appContainer;
	}
	/**
	 * Allows platform to inject application container.
	 */
	public void setAppContainer(final IAppContainer appContainer) {
		_appContainer = appContainer;
	}
	private IAppContainer _appContainer;

}
