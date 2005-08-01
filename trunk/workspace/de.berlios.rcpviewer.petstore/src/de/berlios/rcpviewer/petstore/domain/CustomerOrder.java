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
import de.berlios.rcpviewer.progmodel.extended.IAppContainer;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.ImmutableOncePersisted;
import de.berlios.rcpviewer.progmodel.extended.Optional;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.extended.SaveOperation;
import de.berlios.rcpviewer.progmodel.standard.ContainerOf;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.*;


/**
 * An order placed by a customer to purchase a number of stock items.  
 * 
 * <p>
 * <i>
 * Programming Model notes:
 * <ul>
 * <li> The {@link Named} annotation renames this type in the UI as
 *       <code>Order</code> (rather than <code></i>Customer<i>Order</code>).
 * <li> There is no <code>delete()</code> method and so orders cannot be
 *      deleted once persisted.  However, they can be {@link #cancel}led, 
 *      providing that they haven't shipped. 
 * </ul>
 * </i>
 * 
 * <p>
 * TODOs
 * <ul>
 * <li>TODO: Hibernate mapping to T_ORDER
 * </ul>
 *
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * @author Dan Haywood
 */
@InDomain
@Named("Order")
@DescribedAs("An order placed by a customer to purchase a number of stock items.")
public class CustomerOrder {

	/**
	 * No-arg constructor required by framework.
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> No-arg constructor required by platform.
     * <li> The <code>CustomerOrder</code>'s state is empty, but the various 
     *      constraints on the attributes of <code>CustomerOrder</code>,
     *      as well as the {@link #save()} operation itself, will mean that
     *      the object cannot be saved until sufficient state has been 
     *      entered by the user.
     * <li> Sets up wholly owned components ({@link Account}, {@link Address}
     *      and {@link CreditCard}, copying from the details of the Customer
     *      that has placed the request stop. 
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
     * All other state is entered by the user through the UI.
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
     * Unique identifier for this stock item.
     * 
     * <p>
     * Allocated by the platform, immutable.
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
    @DescribedAs("Unique identifier for this order.  Orders are numbered uniquely across Customers, rather than per Customer")
    public long getOrderId() { 
        return _orderId;
    }
    private void setOrderId(final long orderId) {
        _orderId = orderId;
    }
    private long _orderId;



    /**
     * Date that this order was created.
     *
     * <p>
     * Automatically populated, and cannot be changed programmatically.
     * 
     * <p>
     * <i>
     * Programming Mode notes: 
     * <ul>
     * <li> The {@link ImmutableOncePersisted} annotation will prevent
     *      this attribute from being edited through the UI once the object 
     *      has been saved.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
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
    private Date _orderDate;

    

    /**
     * The status of this order; affects whether it can be updated.
     * 
     * <p>
     * <i>
     * Programming Mode notes: 
     * <ul>
     * <li> The {@link ImmutableOncePersisted} annotation will prevent
     *      this attribute from being edited through the UI once the object 
     *      has been saved.
     * <li> The {@link #setStatusDefaults(Status[])} method sets up default 
     *      parameters for any/all parameters of an operation exposed in the UI.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </i>
     * 
     * @return
     */
    @Order(3)
    @DescribedAs("The status of this order; affects whether it can be updated.")
    public Status getStatus() {
        return _status;
    }
    public void setStatus(final Status status) {
        _status = status;
    }
    public void setStatusDefaults(final Status[] status) {
        status[0] = Status.PENDING;
    }
    public static enum Status {
    	PENDING, DELIVERED, CANCELLED
    }
    private Status _status = Status.PENDING;
    /**
     * Whether the order is still pending.
     * 
     * @see #getStatus()
     * @return
     */
	boolean stillPending() {
		return getStatus() == Status.PENDING;
	}


	/**
	 * The current total cost of all items in this order; for delivered
	 * orders this will be the total cost as charged to the end-user.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Immutable since {@link Derived} annotation.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
	 */
    @Order(4)
    @Derived
    @DescribedAs("The total cost of all items in this order.")
    public BigDecimal getTotal() {
    	BigDecimal total = new BigDecimal(0);
        for(OrderLine item: _orderLines) {
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
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @Order(5)
    public Address getAddress() {
        return _address;
    }
    private void setAddress(final Address address) {
        _address = address;
    }
    private Address _address;

    

    /**
     * Immutable copy of the {@link CreditCard} held by the {@link Customer} 
     * when this order was placed.
     * 
     * <p>
     * The CreditCard is initialized such that it cannot be modified when
     * attached to an order.
     * 
     * <p>
     * <i>
     * Programming Mode notes: 
     * <ul>
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </i>
     */
    @Order(6)
    public CreditCard getCreditCard() {
        return _creditCard;
    }
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
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
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
     * Note that the order aggregates {@link OrderLine}s, rather than 
     * {@link StockItem}s.  The former are wholly contained within their containing
     * order, whereas the latter are effectively stock items that exist
     * independently of whether they have been purchased or not.
     * 
     * <p>
     * <i>
     * Programming Model notes: 
     * <li> The {@link ContainerOf} annotation implies that 
     *      {@link OrderLine}s cannot be dragged/dropped into this collection.  
     *      Such behaviour is only appropriate for objects that can genuinely 
     *      be shared between objects.  Since we have specified 
     *      {@link ContainerOf}, there is no need to explicitly constrain 
     *      the collection (using a {@link #getOrderLinesPre()} 
     *      prerequisites method).
     * <li> On the other hand, {@link @ContainerOf} still allows objects
     *      to be removed from the collection.   These will be implicitly 
     *      deleted.
     * <li> Note the {@link OppositeOf} annotation which indicates a 
     *      bidirectional relationship.  This could equally have been placed on 
     *      the opposing class (or on both).
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </i>
     * 
     * @return
     */
    @Order(8)
    @ContainerOf
    @TypeOf(StockItem.class)
    @OppositeOf("customerOrder")
    @DescribedAs("The set of items purchased in this order.")
    public Set<OrderLine> getOrderLines() {
        return _orderLines;
    }
    /**
     * Required for persistence layer.
     * 
     * @param orderLines
     */
    private void setOrderLines(final Set<OrderLine> orderLines) {
    	_orderLines = orderLines;
    }
    /**
     * Adds an (already created) {@link OrderLine} to this Order.
     * 
     * <p>
     * OrderLines are created in the {@link #addOrderLine(StockItem, int)} method, which then
     * uses this method to add to the collection itself.
     *  
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Since the collection is {@link ContainerOf}, it is not possible
     *      to drag/drop objects into this collection.  (The 
     *      <code>private</code> visibility also effectively disables this).
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     *  
     * @param orderLine
     */
    private void addToOrderLines(final OrderLine orderLine) {
    	orderLine.setCustomerOrder(this);
    	this._orderLines.add(orderLine);
    }
    /**
     * Removes an {@link OrderLine} from this Order, and then deletes the
     * {@link OrderLine}.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> {@link OrderLine}s can be removed directly through the UI  
     *      since (a) the {@link ContainerOf} semantic only applies when
     *      adding objects to collections and (b) the visibility is
     *      <code>public</code>. 
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     *  
     * @param orderLine - order item to be removed (and thence deleted)
     */
    public void removeFromOrderLines(final OrderLine orderLine) {
    	// remove the item from the order (both ends)
    	if (!this._orderLines.remove(orderLine)) {
    		return;
    	}
    	orderLine.setCustomerOrder(null);
    	
    	// now delete the item itself.
    	getAppContainer().delete(orderLine);
    }
    /**
     * Order must be in a {@link #getStatus()} such that it can be modified.
     * 
     * <p>
     * @see #stillPending()
     */
    public IPrerequisites getOrderLinesPre() {
    	return Prerequisites.require(stillPending(), "Order cannot be modified.");
    }
    private Set<OrderLine> _orderLines = new HashSet<OrderLine>();

    
    
    /**
     * User operation to add some quantity of a given item.
     * 
     * <p>
     * There is no equivalent operation to remove items; instead the user can
     * drag the item out of the collection.
     *   
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Notice how there are no checks that the resultant quantity will be 
     *      +ve; that's because we have made these checks in the 
     *      {@link #addOrderLinePre(StockItem, int)} method.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     *   
     * @param item
     * @param quantity - the quantity to add.  If omitted, then defaults to 1.
     */
    @Order(1)
    @DescribedAs("Adds (or updates) this order to purchase an (additional) quantity of the specified item.  To remove, just drag out of the collection.")
    public void addOrderLine(
    		@DescribedAs("The item to purchase.")
    		final StockItem item,
    		@Named("Quantity")
    		@DescribedAs("The number of this item to purchase.")
    		@Optional
    		final int quantity) {
    	OrderLine orderLine = existingOrderLineInThisOrderFor(item);
    	if (orderLine == null) {
    		orderLine = getAppContainer().createTransient(OrderLine.class);
        	orderLine.init(this, item, 0);
        	addToOrderLines(orderLine);
    	}
    	int quantityToAdd = quantity >= 1? quantity: 1;
    	orderLine.addQuantity(quantityToAdd);
	}
    /**
     * Must specify a +ve amount for the quantity, or if specifying a -ve
     * amount then it must be for an existing {@link OrderLine} and leave a
     * still +ve quantity overall.
     * 
     * @param item
     * @param quantity
     * @return
     */
    public IPrerequisites addOrderLinePre(final StockItem item, final int quantity) {
    	OrderLine existingOrderLine = existingOrderLineInThisOrderFor(item);
    	boolean existing = (existingOrderLine != null);
    	return require(quantity > 0, 
    			   	  "Quantity must be greater than 0")
    	      .orRequire(existing && existingOrderLine.getQuantity() + quantity > 0, 
    	    		  "Existing order line cannot have negative quantity");
    }
    /**
     * Helper method that returns an existing {@link OrderLine} that references
     * the {@link StockItem} (if any).
     * 
     * @param item
     * @return
     */
    private OrderLine existingOrderLineInThisOrderFor(final StockItem item) {
    	for(OrderLine orderLine: _orderLines) {
    		if (orderLine.getItem() == item) {
    			return orderLine;
    		}
    	}
    	return null;
    }


    /**
     * Marks this order as having been delivered.
     *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     *   
     */
    @Order(2)
    @DescribedAs("Marks this order as having been delivered.")
    public void delivered() {
    	setStatus(Status.DELIVERED);
    }
    /**
     * Can only mark as delivered if the order is still pending.
     * 
     * @return
     */
    public IPrerequisites deliveredPre() {
    	return Prerequisites.require(
    			stillPending(), 
    			"Cannot mark as delivered because this order is no longer pending.");
    }

    
    /**
     * Cancel this order (provided still pending).
     *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     *   
     */
    @Order(3)
    @DescribedAs("Cancel this order (provided still pending).")
    public void cancel() {
    	setStatus(Status.CANCELLED);
    }
    /**
     * Can only cancel if the order is still pending.
     * 
     * @return
     */
    public IPrerequisites cancelPre() {
    	return Prerequisites.require(
    			stillPending(), "Cannot cancel because this order is no longer pending.");
    }

    
    

    /**
     * Invoked when the order is first saved, or at any time subsequently.
     *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link SaveOperation} annotation indicates that it is
     *      this method that is the save method.  By convention the method is
     *      called <code>save</code>, but it must be <code>public</code>, take 
     *      no arguments and return <code>void</code>.
     * <li> In fact, the save method does nothing.  However the 
     *      {@link #savePre()} does define some prerequisites that must be
     *      met for the save to be performed.
     * </ul>
     * </i>
     */
    @SaveOperation
    public void save() {
    	// nothing to do (but see the savePre method).  
    }
    /**
     * Must have placed some items into the order.
     */
    public IPrerequisites savePre() {
    	return require(
    			_orderLines.size() > 0, 
    			"No items have been added to the order");
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
	 * Allows framework to inject application container.
	 */
	public void setAppContainer(final IAppContainer appContainer) {
		_appContainer = appContainer;
	}
	private IAppContainer _appContainer;



}
