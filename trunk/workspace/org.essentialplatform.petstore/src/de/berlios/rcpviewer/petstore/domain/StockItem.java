/*
 * Created on Feb 22, 2003
 */
package de.berlios.rcpviewer.petstore.domain;

import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.none;

import java.io.Serializable;
import java.math.BigDecimal;

import de.berlios.rcpviewer.progmodel.extended.BusinessKey;
import de.berlios.rcpviewer.progmodel.extended.DeleteOperation;
import de.berlios.rcpviewer.progmodel.extended.FieldLengthOf;
import de.berlios.rcpviewer.progmodel.extended.IAppContainer;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Lifecycle;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.RelativeOrder;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.extended.SaveOperation;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.OppositeOf;


/**
 * A (class of) item that may be purchased from the petstore.
 * 
 * <p>
 * We should be precise here: an instance of <code>StockItem</code> represents 
 * a class of actual things that can be purchased.  The petstore keeps track 
 * of the actual number of such-and-such an item that are in stock.
 * 
 * <p>
 * Put another way: it is akin to an entry on a menu, rather than an actual 
 * meal.  Or, if you are familiar with Coad's <i>Java Modelling in Color</i>, 
 * then it's blue, not green).
 * 
 * <p>
 * <code>StockItem</code>s are related to {@link Product}s, which are really 
 * just a higher level categorization (Products are also blue).  
 * <code>Product</code>s are themselves categorized into {@link Category}s 
 * (again blue).
 * 
 * <p>
 * For example:
 * <ul>
 * <li> a <code>StockItem</code> might be <i>Male Puppy Bulldog</i>, or 
 *      <i>Spotted Tiger Shark</i>
 * <li> a <code>Product</code> might be <i>Bulldog</i>, or <i>Tiger Shark</i>
 * <li> a <code>Category</code> might be <i>Dog</i>, or <i>Fish</i>.
 * </ul>
 * 
 * <p>
 * TODOs
 * <ul>
 * <li>TODO: Hibernate mapping to T_ITEM
 * </ul>
 * 
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * @author Dan Haywood
 */
@Lifecycle(searchable=true,instantiable=true,saveable=true)
@InDomain
public class StockItem {

	/**
	 * Constructor.
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> No-arg constructor required by platform.
     * <li> The <code>StockItem</code>'s state is (mostly) empty, but the 
     *      various constraints on the attributes of <code>StockItem</code>,
     *      as well as the {@link #save()} operation itself, will mean that
     *      the object cannot be saved until sufficient state has been 
     *      entered by the user.
     * <li> Since <code>StockItem</code>s are created by {@link Product}s, the only
     *      initial state to set up is the product to which the item belongs.
     *      This is done through the 
     * </ul>
     * </i>
     * 
	 */
	public StockItem() {
	}
	
	
	/**
	 * Initialize the item with its owning {@link Product}.
	 * 
	 * <p>
	 * All other state of the item is entered by the user through the UI.
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
	 * @param product
	 */
	void init(final Product product) {
		_product = product;
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
    @RelativeOrder(1)
    @DescribedAs("Unique identifier for this stock item.  Items are numbered uniquely across products.")
    public String getItemId() {
        return _itemId;
    }
    private void setItemId(final String itemId) {
        _itemId = itemId;
    }
    private String  _itemId;


    /**
     * The type of product that this item represents.
	 *
     * <p>
     * The product is immutable - it cannot be changed.
     *  
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link BusinessKey} annotation indicates that the attribute 
     *      can be used (possibly in conjunction with other attributes) as a 
     *      unique identifier of the object instance with respect to other 
     *      object instances.  The suffix indicates its significance within the
     *      key.
     * <li> Bidirectional m:1 association with {@link Product}.  The 
     *      {@link OppositeOf} annotation is (happens to be) on the other end.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * 
     * </ul>
     * </i>
     */
    @RelativeOrder(2)
    @BusinessKey(name="product-description",pos=1)
    @DescribedAs("The type of product that this item represents.")
    public Product getProduct() {
        return _product;
    }
    void setProduct(final Product product) {
        _product = product;
    }
    private Product _product;
    

    /**
     * Short description of this stock item.
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link BusinessKey} annotation indicates that the attribute 
     *      can be used (possibly in conjunction with other attributes) as a 
     *      unique identifier of the object instance with respect to other 
     *      object instances.  The suffix indicates its significance within the
     *      key.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @RelativeOrder(3)
    @BusinessKey(name="product-description", pos=2)
	@FieldLengthOf(50)
    @MaxLengthOf(255)
    @DescribedAs("Short description of this stock item.")
    public String getDescription() {
        return _description;
    }
    public void setDescription(final String description) {
        _description = description;
    }
    private String  _description;
    

    /**
     * Price to produce this stock item.
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
    @RelativeOrder(4)
	@DescribedAs("Price to produce this stock item.")
    public BigDecimal getUnitCost() {
        return _unitCost;
    }
    public void setUnitCost(final BigDecimal unitCost) {
        _unitCost = unitCost;
    }
    private BigDecimal _unitCost;



    /**
	 * Price at which this item currently sells.
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
    @RelativeOrder(5)
	@DescribedAs("Price at which this item currently sells.")
    public BigDecimal getListPrice() {
        return _listPrice;
    }
    public void setListPrice(final BigDecimal listPrice) {
        _listPrice = listPrice;
    }
    private BigDecimal _listPrice;


   

    /**
     * Location of image file that illustrates this item.
	 *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> Uses a custom {@link FilePath} value type.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     */
    @RelativeOrder(6)
	@DescribedAs("Location of image file that illustrates this item.")
    public FilePath getImagePath() {
        return _imagePath;
    }
    public void setImagePath(final FilePath imagePath) {
        _imagePath = imagePath;
    }
    private FilePath _imagePath;

    

    /**
     * Invoked when the item is first saved, or at any time subsequently.
     *
     * <p>
     * Adds the item to its owning {@link Product}.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link SaveOperation} annotation indicates that it is
     *      this method that is the save method.  By convention the method is
     *      called <code>save</code>, but it must be <code>public</code>, take 
     *      no arguments and return <code>void</code>.
     * <li> When the StockItem is initially created (see {@link #init(Product)}
     *      it has a reference to its owning <code>Product</code>.  However, the
     *      creating <code>Product</code> does not refer to the 
     *      <code>StockItem</code> at this point because persistent objects 
     *      cannot refer to transient objects.  The link is finally set up  
     *      when the object is saved in the {@link #save()} method.
     * </ul>
     * </i>
     */
    @SaveOperation
    public void save() {
    	_product.addToStockItems(this);  
    }
    /**
     * No prerequisites (other than declarative prereqs on attributes).
     * 
     * @return
     */
    public IPrerequisites savePre() {
    	return none();
    }


    /**
     * Delete this stock item, subject to referential integrity constraints.
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
     * </ul>
     * </i>
     *
     */
    @DeleteOperation
    public void delete() {
    	// does nothing; the platform will perform the delete implicitly.
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
