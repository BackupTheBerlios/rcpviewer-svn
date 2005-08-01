/*
 * Created on Feb 22, 2003
 */
package de.berlios.rcpviewer.petstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.swt.internal.ole.win32.ISpecifyPropertyPages;

import de.berlios.rcpviewer.progmodel.extended.DeleteOperation;
import de.berlios.rcpviewer.progmodel.extended.IAppContainer;
import de.berlios.rcpviewer.progmodel.extended.BusinessKey;
import de.berlios.rcpviewer.progmodel.extended.FieldLengthOf;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.MinLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Optional;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.standard.ContainerOf;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.extended.Named;
import de.berlios.rcpviewer.progmodel.standard.OppositeOf;
import de.berlios.rcpviewer.progmodel.standard.TypeOf;


/**
 * A grouping of {@link StockItem}s. 
 * 
 * <p>
 * See {@link StockItem} for further discussion on the difference between 
 * {@link StockItem}s, {@link Product}s and {@link Category}s.
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
 * <li>TODO: Hibernate mapping to T_PRODUCT
 * </ul>
 * 
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * @author Dan Haywood
 */
@InDomain
public class Product {
	
	/**
	 * Constructor.
	 *
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> No-arg constructor required by platform.
     * <li> The <code>Product</code>'s state is (mostly) empty, but the 
     *      various constraints on the attributes of <code>Product</code>,
     *      as well as the {@link #save()} operation itself, will mean that
     *      the object cannot be saved until sufficient state has been 
     *      entered by the user.
     * <li> Since <code>StockItem</code>s are created by {@link Product}s, the only
     *      initial state to set up is the product to which the item belongs.
     *      This is done through the 
     * </ul>
     * </i>
	 */
	public Product() {
	}
	
	
	/**
	 * Unique identifier across all products.
	 * 
	 * <p>
	 * Allocated by the <i>user</i> (rather than the platform).
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link BusinessKey} annotation indicates that the attribute 
     *      can be used (possibly in conjunction with other attributes) as a 
     *      unique identifier of the object instance with respect to other 
     *      object instances.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
	 * @return
	 */
	@Order(1)
    @BusinessKey("id")
	@MinLengthOf(8)
	@MaxLengthOf(10)
	@DescribedAs("Unique identifier for this product")
    public String getProductId() {
        return _productId;
    }
    public void setProductId(final String productId) {
        _productId = productId;
    }
    private String _productId;


    /**
     * The name of this product, eg Koi fish.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link BusinessKey} annotation indicates that the attribute 
     *      can be used (possibly in conjunction with other attributes) as a 
     *      unique identifier of the object instance with respect to other 
     *      object instances.
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(2)
    @BusinessKey("name")
    @FieldLengthOf(30)
    @MaxLengthOf(50)
    @DescribedAs("The name of this product, eg Koi fish.")
    public String getName() {
        return _name;
    }
    public void setName(final String name) {
        _name = name;
    }
    private String _name;


    /**
     * The description of this type of product.
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
    @Order(3)
    @Optional
    @FieldLengthOf(50)
    @MaxLengthOf(255)
    @DescribedAs("The description of this type of product.")
    public String getDescription() {
        return _description;
    }
    public void setDescription(final String description) {
        _description = description;
    }
    private String _description;

    

    /**
     * The {@link Category} in which this product resides.
     * 
     * <p>
     * The category is mutable - it can be changed over time. 
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> This is a bidirectional m:1 relationship, <i>without</i> 
     *      containment semantics.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(4)
    @ContainerOf
    @OppositeOf("products")
    @TypeOf(StockItem.class)
	public Category getCategory() {
		return _category;
	}
	/**
	 * Maintained by category.
	 * 
	 * @param category
	 */
	void setCategory(final Category category) {
		_category = category;
	}
    private Category _category;

	
	
    /**
     * Set of {@link StockItem}s within this product.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> This is a bidirectional 1:m relationship with {@link StockItem}, 
     *      with containment semantics ({@link ContainerOf} annotation).
     * <li> The {@link #addToStockItems(StockItem)} method has package level 
     *      visibility to allow a newly created {@link StockItem} to be added 
     *      to the collection.  Thereafter the product looks after the 
     *      relationship.  Since the visibility is not <code>public</code>, it
     *      is not possible (even if it made sense) to add {@link StockItem}s
     *      directly through the UI.
     * <li> In contrast, the {@link #removeFromItems(StockItem)} method has 
     *      <code>public</code> level visibility so that items can be removed
     *      via the UI.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(5)
    @ContainerOf(cascadeDelete=true)
    @OppositeOf("product")
    @TypeOf(StockItem.class)
    public Set<StockItem> getStockItems() {
        return _stockItems;
    }
    private void setStockItems(final Set<StockItem> stockItems) {
    	_stockItems = stockItems;
    }
    void addToStockItems(final StockItem item) {
    	_stockItems.add(item);
    	item.setProduct(this);
    }
    public void removeFromItems(final StockItem item) {
    	_stockItems.remove(item);
    	item.setProduct(null);
    	getAppContainer().delete(item);
    }
    private Set<StockItem> _stockItems = new LinkedHashSet<StockItem>();


    
    /**
     * Creates a new {@link StockItem} (eg <i>Male Puppy Bulldog</i>) for this 
     * product (eg <i>Bulldog</i>). 
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Although the item is created, it is still transient.  It is 
     *      therefore not (yet) added to the collection of items for this 
     *      product.  When the item is finally persisted, then the relationship
     *      is made.
     * <li> The operation cannot be invoked unless the product itself has been
     *      persisted; see {@link #newItemPre()}.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
     * @return
     */
    @Order(1)
    @DescribedAs("Create a new item.  The item is not added to the product's set until it is persisted.")
    public StockItem newItem() {
    	
    	StockItem item = getAppContainer().createTransient(StockItem.class);
    	item.init(this);
    	
    	return item;
    }
    /**
     * The product must have been persisted before being able to create new
     * items.
     * 
     * @return
     */
    public IPrerequisites newItemPre() {
    	return Prerequisites.require(isPersistent(), "Must be persistent");
    }
    



    /**
     * Delete this product, subject to referential integrity constraints.
     *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link DeleteOperation} annotation indicates that this operation
     *      should be rendered in a standardized way. 
     * <li> Since there is no <code>deletePre()</code> method, the user can
     *      invoke the delete without performing any checks.  It is quite
     *      possible for this to fail if the persistence layer detects a
     *      referential integrity error (eg an {@link OrderLine} referencing
     *      this object).
     * <li> That said, the {@link Cascade} annotation on the 
     *      {@link #getStockItems()} relationship indicates that the platform
     *      should automatically attempt to cascade delete any referencing 
     *      stock items.  Provided that those stock items have not in turn been
     *      referenced, the delete should succeed.
     * </ul>
     * </i>
     *
     */
    @DeleteOperation
    public void delete() {
    	// does nothing; the platform will perform the delete implicitly.
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
	 * Allows framework to inject application container.
	 */
	public void setAppContainer(final IAppContainer appContainer) {
		_appContainer = appContainer;
	}
	private IAppContainer _appContainer;


}
