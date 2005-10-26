/*
 * Created on Feb 22, 2003
 */
package de.berlios.rcpviewer.petstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Invisible;
import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.extended.Named;
import de.berlios.rcpviewer.progmodel.extended.RelativeOrder;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.*;
import de.berlios.rcpviewer.progmodel.standard.Derived;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.Immutable;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.petstore.domain.StockItem;

/**
 * Represents the purchase of a quantity of some {@link StockItem} in the 
 * context of a larger {@link CustomerOrder}.
 * 
 * <p>
 * There can only be one order line per {@link StockItem}.  If more or less
 * of such an item is required, then the quantity can be adjusted.
 * 
 * <p>
 * <i>
 * Programming Model notes:
 * <ul>
 * <li> Although there is no <code>save()</code> method for this object, it is 
 *      provided implicitly by the platform.
 * </ul>
 * </i>
 * 
 * <p>
 * TODOs
 * <ul>
 * <li>TODO: Hibernate mapping to T_ORDER_ITEM
 * </ul>
 * 
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * @author Dan Haywood
 */
@Lifecycle(searchable=false,instantiable=false,saveable=true)
@InDomain
public class OrderLine  {

	/**
	 * Required by framework.
	 * 
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> No arg constructor is required by the platform.
     * <li> Since <code>OrderLine</code>s are created programmatically (by the 
     *      owning {@link CustomerOrder} - see  
     *      {@link CustomerOrder#addOrderLine(StockItem, int)}), the initial state is instead
     *      set up using the {@link #init() method.
     * </ul>
     * </i>
	 */
    public OrderLine() {}

    /**
     * Initialize the object; by convention call after creating.
     *
     * <p>
     * Copies down the list price from the supplied {@link StockItem} as this 
     * <code>OrderLine</code>'s unit price.  This is because the list price may 
     * vary over time whereas the unit price cannot.
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
     * @param item
     * @param quantity
     */
    void init(
    		final CustomerOrder customerOrder, 
    		final StockItem item, final int quantity) {
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
     *      should not be displayed in the UI.
     * </i>
     */
    @Invisible
    public long getOrderLineId() {
        return _orderLineId;
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
     * @param orderLineUId
     */
    private void setOrderLineId(final long orderLineUId) {
        _orderLineId = orderLineUId;
    }
    private long _orderLineId;

    
    
    /**
     * The owning {@link CustomerOrder} of this order item.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul> 
     * <li> The {@link Invisible} annotation indicates that the attribute
     *      should not be displayed in the UI.
     * <li> This is the opposite end of a 1:m bidirectional
     *      relationship with CustomerOrder.  No <code>OppositeOf</code> 
     *      annotation is required because we chose to put the annotation on 
     *      the other end of the relationship.  Note the package level 
     *      visibility for the {@link #setCustomerOrder(CustomerOrder)} method.
     * </i>
     */
    @Invisible
    public CustomerOrder getCustomerOrder() {
    	return _customerOrder;
    }
	void setCustomerOrder(CustomerOrder customerOrder) {
		_customerOrder = customerOrder;
	}
    private CustomerOrder _customerOrder;

    
    
    /**
     * The {@link StockItem} to which this <code>OrderLine</code> relates.
     * 
     * <p>
     * There can only be one <code>OrderLine</code> per <code>StockItem</code>.  
     * Another way of thinking about this is that the <code>StockItem</code> is like 
     * a key to each <code>OrderLine</code>.  Note that <code>OrderLine</code>
     * is wholly contained within a {@link CustomerOrder}, whereas 
     * <code>StockItem</code> is potentially shared.
     * 
     * <p>
     * This reference is immutable, being set up in the 
     * {@link #init(CustomerOrder, StockItem, int)} method.
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
    @RelativeOrder(1)
    @DescribedAs("The item to which this order line relates (eg a male koi)")
    public StockItem getItem() {
        return _item;
    }
    private void setItem(final StockItem item) {
        _item = item;
    }
    private StockItem   _item;



    /**
     * The (mutable) number of this item required in the Order.
     * 
     * @return
     */
    @RelativeOrder(2)
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
    		  .andRequire(getCustomerOrder().stillPending(), 
    				  "Can only alter quantity if order has not been shipped.");
    }
    private int _quantity;



    /**
     * The list price of an {@link StockItem} <i>at the time that the order was 
     * placed</i>.
     * 
     * <p>
     * Since the list price of an StockItem may change over time; it is captured
     * when the order line item is created.  It cannot be modified.
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
     * @return
     */
    @RelativeOrder(3)
    @DescribedAs("The price of a single item.  The overall cost of the order line item is derived from this and the quantity.")
    public BigDecimal getUnitPrice() {
        return _unitPrice;
    }
    private void setUnitPrice(final BigDecimal unitPrice) {
    	_unitPrice = unitPrice;
    }
    private BigDecimal _unitPrice = new BigDecimal(0L);

    

    /**
     * The cost of this line item (quantity x unit price).
     * 
     * <p>
     * <i>
     * Programming Model notes: 
     * <ul>
     * <li> The <code>Derived</code> annotation of course implies that the
     *      attribute is immutable.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * @return
     */
    @RelativeOrder(4)
    @Derived
    @DescribedAs("The cost of this line item (quantity x unit price).")
    public BigDecimal getSubTotal() {
    	if (getUnitPrice() == null) {
    		return new BigDecimal(0);
    	}
        return getUnitPrice().multiply(new BigDecimal(getQuantity()));
    }



    /**
     * Helper method, eg used by {@link CustomerOrder} to adjust the quantity
     * of an existing <code>OrderLine</code>.
     * 
     * @param quantity - can be negative, but shouldn't cause a zero or negative quantity overall.
     */
	void addQuantity(final int quantity) {
		this.setQuantity(getQuantity() + quantity);
	}
}
