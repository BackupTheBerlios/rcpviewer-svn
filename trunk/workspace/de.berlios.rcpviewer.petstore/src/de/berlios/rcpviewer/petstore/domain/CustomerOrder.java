/*
 * Created on Feb 22, 2003
 */
package de.berlios.rcpviewer.petstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.berlios.rcpviewer.progmodel.standard.Derived;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.OppositeOf;
import de.berlios.rcpviewer.progmodel.extended.Named;
import de.berlios.rcpviewer.progmodel.standard.TypeOf;
import de.berlios.rcpviewer.progmodel.extended.AppContainer;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.ImmutableOncePersisted;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.standard.ContainerOf;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.*;


/**
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * <p>
 * <i>
 * Programming Model notes: the <code>@Named</code> annotation is used to
 * effectively rename the class to "Order" rather than its actual name of
 * "CustomerOrder".
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
 * @author Dan Haywood
 */
@InDomain
@Named("Order")
public class CustomerOrder {

	/**
	 * No-arg constructor required by framework.
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     * 
	 */
    public CustomerOrder() {
    }

    
    /**
     * Initialize the order for this {@link Customer}.
     *  
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     * 
     * @param customer
     */
    void init(final Customer customer) {
        _customer   = customer;

        // copy Address from Customer
        _address = getAppContainer().createTransient(Address.class);
    	_address.init(customer.getAddress());
    	
        // copy CreditCard from Customer
    	_creditCard = getAppContainer().createTransient(CreditCard.class);
    	_creditCard.init(getCreditCard());
    }

    
    /**
     * Allocated by the persistence layer.
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
    @DescribedAs("Unique identifier for this order.  Orders are numbered uniquely across Customers, rather than per Customer")
    public long getOrderId() { 
        return _orderId;
    }
    /**
     * Immutable; mutator provided for persistence layer only.
     * 
     * <p>
     * <i>
     * Programming Mode notes: 
     * <ul>
     * <li> the <code>private</code> visibility indicates that the attribute 
     *      cannot be changed in the UI.
     * </ul>
     * </i>
     */
    private void setOrderId(final long orderId) {
        _orderId = orderId;
    }
    private long _orderId;



    /**
     * 
     * <p>
     * <i>
     * Programming Mode notes: 
     * <ul>
     * <li> the <code>private</code> visibility indicates that the attribute 
     *      cannot be changed in the UI.
     * <li> the <code>@ImmutableOncePersisted</code> annotation will prevent
     *      this attribute from being edited through the UI once the credit card
     *      object has been saved.  (It isn't appropriate to put this annotation
     *      on the entire class since some attributes can be modified even
     *      after the class has been persisted).
     * <li> Mandatory (since no <code>Optional</code> annotation).
     * </i>
     */
    @Order(2)
    @DescribedAs("Date that this order was placed")
    @ImmutableOncePersisted
    public Date getOrderDate() {
        return _orderDate;
    }
    /**
     * TODO: should round the date to the nearest fifteen minutes, say.
     * 
     * @param orderDate
     */
    public void setOrderDate(final Date orderDate) {
        _orderDate = orderDate;
    }
    private Date       _orderDate;

    

    @Order(3)
    @DescribedAs("The status of this order; affects whether it can be updated.")
    public Status getStatus() {
        return _status;
    }
    public void setStatus(final Status status) {
        _status = status;
    }
    public static enum Status {
    	PENDING, DELIVERED, CANCELLED
    }
    private Status  _status = Status.PENDING;
    /**
     * Whether details of the order can be modified.
     * 
     * <p>
     * Depends upon its status.
     * 
     * @see #getStatus()
     * @return
     */
	boolean canBeModified() {
		return getStatus() == Status.PENDING;
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
    @Order(4)
    @Derived
    @DescribedAs("The total cost of all items in this order.")
    public BigDecimal getTotal() {
    	BigDecimal total = new BigDecimal(0);
        for(OrderItem item: _orderItems) {
        	total.add(item.getSubTotal());
        }
        return total;
    }



    /**
     * Copy of the {@link Address} referenced by the {@link Customer} when
     * this order was placed. 
     * 
     * <p>
     * The Address is initialized such that it cannot be modified when
     * attached to an order.
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
    public Address getAddress() {
        return _address;
    }
    /**
     * For persistence layer only; Address object associated with this order
     * cannot be changed.
     * 
     * <p>
     * <i>
     * Programming Mode notes: the private visibility indicates that the
     * attribute cannot be changed in the UI.
     * </i>
     * 
     * @param address
     */
    private void setAddress(final Address address) {
        _address = address;
    }
    private Address    _address;

    

    /**
     * Copy of the {@link CreditCard} held by the {@link Customer} when
     * this order was placed.
     * 
     * <p>
     * The CreditCard is initialized such that it cannot be modified when
     * attached to an order.
     * 
     * <p>
     * <i>
     * Programming Mode notes: 
     * <ul>
     * <li> mandatory since no <code>@Optional</code> annotation.
     * </i>
     */
    @Order(6)
    public CreditCard getCreditCard() {
        return _creditCard;
    }
    /**
     * CreditCard object associated with this order cannot be changed.
     * 
     * <p>
     * <i>
     * Programming Mode notes: 
     * <ul>
     * <li> the <code>private</code> visibility indicates that the reference 
     *      cannot be changed through the UI (though the referenced object need
     *      not itself be immutable).
     * <li> the mutator is required for the persistence layer.
     * </ul>
     * </i>
     * 
     * @param creditCard
     */
    private void setCreditCard(final CreditCard creditCard) {
        _creditCard = creditCard;
    }
    private CreditCard _creditCard;


    /**
     * Immutable reference to the {@link Customer} that placed this order.
     * 
     * <p>
     * <i>
     * Programming Mode notes: 
     * <ul>
     * <li> mandatory since no <code>@Optional</code> annotation.
     * </i>
     */
    @Order(7)
    @DescribedAs("The customer that placed this order")
    public Customer getCustomer() {
        return _customer;
    }
    /**
     * Customer object associated with this order cannot be changed.
     * 
     * <p>
     * <i>
     * Programming Mode notes: 
     * <ul>
     * <li> the <code>private</code> visibility indicates that the reference 
     *      cannot be changed through the UI (though the referenced object need
     *      not itself be immutable).
     * <li> the mutator is required for the persistence layer.
     * </ul>
     * </i>
     * 
     * @param creditCard
     */
    private void setCustomer(final Customer customer) {
        _customer = customer;
    }
    private Customer _customer;
    
    
    

    
    /**
     * The set of items that make up this order.
     * 
     * <p>
     * Note that the order aggregates {@link OrderItem}s, rather than 
     * {@link Item}s.  The former are wholly contained within their containing
     * Order, whereas the latter are effectively stock items that exist
     * independently of whether they have been purchased or not.
     * 
     * <p>
     * <i>
     * Programming Model notes: 
     * <li> the <code>@ContainerOf</code> annotation implies that 
     *      {@link OrderItem}s cannot be dragged/dropped into this collection.  
     *      Such behaviour is only appropriate for objects that can genuinely 
     *      be shared between objects.  Since we have specified 
     *      <code>@ContainerOf</code>, there is no need to explicitly constrain 
     *      the collection (using a <code>getOrderItemsPre</code> prerequisites method).
     * <li> On the other hand, <code>@ContainerOf</code> still allows objects
     *      to be removed from the collection.   These will be implicitly 
     *      deleted.
     * <li> Note the <code>OppositeOf</code> annotation which indicates a 
     *      bidirectional relationship.  This could equally have been placed on 
     *      the opposing class (or on both).
     * </i>
     * 
     * @return
     */
    @Order(8)
    @DescribedAs("The set of items purchased in this order.")
    @ContainerOf
    @TypeOf(Item.class)
    @OppositeOf("customerOrder")
    public Set<OrderItem> getOrderItems() {
        return _orderItems;
    }
    /**
     * Required for persistence layer.
     * 
     * @param orderItems
     */
    private void setOrderItems(final Set<OrderItem> orderItems) {
    	_orderItems = orderItems;
    }
    /**
     * Adds an (already created) {@link OrderItem} to this Order.
     * 
     * <p>
     * This is the suggested style for support maintaining bidirectional 
     * relationships.  In this case, since CustomerOrder is the <i>ContainerOf</i>
     * OrderItems, it will need to create them first.  This is done in the
     * {@link #add(Item, int)} method.
     *  
     * @param orderItem
     */
    private void addToOrderItems(final OrderItem orderItem) {
    	orderItem.setOrder(this);
    	this._orderItems.add(orderItem);
    }
    /**
     * Removes an {@link OrderItem) from this Order.
     * 
     * <p>
     * This is the suggested style for support maintaining bidirectional 
     * relationships.  In this case, since CustomerOrder is the <i>ContainerOf</i>
     * OrderItems, it will need to delete the OrderItem afterwards.  This is 
     * done in the {@link #add(Item, int)} method.
     *  
     * <p>
     * <i>Programming Model notes: <code>public</code> visibility so that the 
     * framework will invoke it when the user removes an object from the 
     * collection through the UI. 
     * </i>
     *  
     * @param orderItem to be removed (and thence deleted)
     */
    public void removeFromOrderItems(final OrderItem orderItem) {
    	// remove the item from the order (both ends)
    	if (!this._orderItems.remove(orderItem)) {
    		return;
    	}
    	orderItem.setOrder(null);
    	
    	// now delete the item itself.
    	getAppContainer().delete(orderItem);
    }
    /**
     * Order must be in a {@link #getStatus()} such that it can be modified.
     * 
     * <p>
     * @see #canBeModified()
     */
    public IPrerequisites getOrderItemsPre() {
    	return Prerequisites.require(canBeModified(), "Order cannot be modified.");
    }
    private Set<OrderItem> _orderItems = new HashSet<OrderItem>();

    
    
    /**
     * User operation to add some quantity of a given item.
     * 
     * <p>
     * There is no equivalent operation to remove items; instead the user can
     * drag the item out of the collection.
     *   
     * <p>
     * <i>
     * Programming Model notes: notice how there are no checks that the
     * resultant quantity will be +ve; that's because we have made these checks
     * in the <code>addPre()</code> method.
     * </i>   
     *   
     * @param item
     * @param quantity
     */
    @Order(1)
    @DescribedAs("Adds (or updates) this order to purchase an (additional) quantity of the specified item.  To remove, just drag out of the collection.")
    public void add(
    		@Named("Item")
    		@DescribedAs("The item to purchase")
    		final Item item,
    		@Named("Quantity")
    		@DescribedAs("The quantity ")
    		final int quantity) {
    	OrderItem orderItem = existingOrderItem(item);
    	if (orderItem == null) {
    		orderItem = getAppContainer().createTransient(OrderItem.class);
        	orderItem.init(this, item, 0);
        	addToOrderItems(orderItem);
    	}
    	orderItem.addQuantity(quantity);
	}
    /**
     * Must specify a +ve amount for the quantity, or if specifying a -ve
     * amount then it must be for an existing {@link OrderItem} and leave a
     * still +ve quantity overall.
     * 
     * @param item
     * @param quantity
     * @return
     */
    public IPrerequisites addPre(final Item item, final int quantity) {
    	OrderItem orderItem = existingOrderItem(item);
    	boolean existing = (orderItem != null);
    	return require(quantity > 0, 
    			   	  "Quantity must be greater than 0")
    	      .orRequire(existing && orderItem.getQuantity() + quantity > 0, 
    	    		  "Existing order line cannot have negative quantity");
    }
    /**
     * Helper method that returns an existing {@link OrderItem} that references
     * the {@link Item} (if any).
     * 
     * @param item
     * @return
     */
    private OrderItem existingOrderItem(final Item item) {
    	for(OrderItem orderItem: _orderItems) {
    		if (orderItem.getItem() == item) {
    			return orderItem;
    		}
    	}
    	return null;
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
