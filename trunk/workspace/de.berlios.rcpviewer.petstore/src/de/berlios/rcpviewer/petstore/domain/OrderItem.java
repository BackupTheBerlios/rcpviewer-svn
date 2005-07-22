/*
 * Created on Feb 22, 2003
 */
package de.berlios.rcpviewer.petstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Invisible;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.*;
import de.berlios.rcpviewer.progmodel.standard.Derived;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.Immutable;
import de.berlios.rcpviewer.progmodel.standard.InDomain;


/**
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
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
public class OrderItem  {

	/**
	 * Required by framework.
	 * 
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ... init ...
     * </ul>
     * </i>
	 */
    public OrderItem() {}

    /**
     * Initialize the object; by convention call after creating.
     *
     * <p>
     * Copies down the list price from the supplied {@link Item} as this 
     * OrderItem's unit price.  This is because the list price may vary over 
     * time whereas the unit price cannot.
     * 
     * @param item
     * @param quantity
     */
    public void init(
    		final CustomerOrder customerOrder, 
    		final Item item, final int quantity) {
    	_customerOrder = customerOrder;
        _item      = item;
        _quantity  = quantity;
        _unitPrice = item.getListPrice();
    }

    
    
    /**
     * Unique identifier (across all {@link Order}s) for this order item.
     * 
     * <p>
     * We use a unique identifier rather than a composite identifier (qualified 
     * by owning Order) to simplify persistence mapping.
     * 
     * <p>
     * Allocated by the persistence layer.
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
    public long getOrderItemId() {
        return _orderItemId;
    }
    /**
     * <p>
     * <i>Programming Model notes: <code>private</code> visibility so immutable
     * in the UI.
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
     * @param orderItemUId
     */
    private void setOrderItemId(final long orderItemUId) {
        _orderItemId = orderItemUId;
    }
    private long   _orderItemId;

    
    
    /**
     * The owning {@link CustomerOrder} of this order item.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul> 
     * <li> The <code>Invisible</code> annotation indicates that the attribute
     *      should not be displayed in the UI.  (A more verbose way would to
     *      have returned a <code>Prerequisites.invisible()</code> from a 
     *      <code>...Pre()</code> method).
     * <li> This is the opposite end of a 1:m bidirectional
     *      relationship with CustomerOrder.  No <code>OppositeOf</code> 
     *      annotation is required because we chose to put the annotation on 
     *      the other end of the relationship.  Note the package level 
     *      visibility for the {@link #setOrder(CustomerOrder)} method.
     * </i>
     */
    @Invisible
    public CustomerOrder getCustomerOrder() {
    	return _customerOrder;
    }
    /**
     * Allows the other end of the bidirectional association to maintain this
     * end.
     * 
     * 
     * @param order
     */
	void setOrder(CustomerOrder order) {
		_customerOrder = order;
	}
    private CustomerOrder _customerOrder;

    
    
    /**
     * The {@link Item} to which this order line relates.
     * 
     * <p>
     * There can only be one order line per Item.  Another way of thinking 
     * about this is that the Item is like a key to each OrderItem.  Note that
     * OrderItem is wholly contained within a {@link CustomerOrder}, whereas
     * Item is potentially shared.
     * 
     * <p>
     * This reference is immutable.
     * 
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
    @DescribedAs("The item to which this order line relates (eg a male koi)")
    public Item getItem() {
        return _item;
    }
    private void setItem(final Item item) {
        _item = item;
    }
    private Item   _item;



    /**
     * The (mutable) number of this item required in the Order.
     * 
     * @return
     */
    @Order(2)
    @DescribedAs("The number of this item required (eg 3 male koi)")
    public int getQuantity() {
        return _quantity;
    }
    public void setQuantity(final int quantity) {
        this._quantity = quantity;
    }
    /**
     * Quantity must be positive.
     * 
     * @param quantity
     * @return
     */
    public IPrerequisites setQuantityPre(final int quantity) {
    	return require(quantity > 0, "Quantity must be positive")
    		  .andRequire(getCustomerOrder().canBeModified(), 
    				  "Can only alter quantity if order has not been shipped.");
    }
    private int _quantity;



    /**
     * The list price of an {@link Item} <i>at the time that the order was 
     * placed</i>.
     * 
     * <p>
     * Since the list price of an Item may change over time; it is captured
     * when the order line item is created.  It cannot be modified.
     * 
     * <p>
     * <i>
     * Programming Model notes: 
     * <ul>
     * <li> the <code>private</code> visibility indicates that the attribute 
     *      cannot be changed through the UI.
     * <li> the mutator is required for the persistence layer.
     * <li> uses custom {@link Money} value type.
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(3)
    @DescribedAs("The price of a single item.  The overall cost of the order line item is derived from this and the quantity.")
    public BigDecimal getUnitPrice() {
        return _unitPrice;
    }
    private void setUnitPrice(final BigDecimal unitPrice) {
    	_unitPrice = unitPrice;
    }
    private BigDecimal _unitPrice;

    

    /**
     * <p>
     * <i>
     * Programming Model notes: 
     * <ul>
     * <li> The <code>Derived</code> annotation of course implies that the
     *      attribute is immutable.
     * </ul>
     * </i>
     * @return
     */
    @Order(4)
    @Derived
    @DescribedAs("The cost of this line item (quantity x unit price)")
    public BigDecimal getSubTotal() {
        return _unitPrice.multiply(getUnitPrice());
    }



    /**
     * @param quantity - can be negative, but shouldn't cause a zero or negative quantity overall.
     */
	void addQuantity(final int quantity) {
		this.setQuantity(getQuantity() + quantity);
	}
}
