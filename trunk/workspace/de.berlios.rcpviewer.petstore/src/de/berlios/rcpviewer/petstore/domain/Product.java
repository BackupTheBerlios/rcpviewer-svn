/*
 * Created on Feb 22, 2003
 */
package de.berlios.rcpviewer.petstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import java.util.LinkedHashSet;
import java.util.Set;

import de.berlios.rcpviewer.progmodel.extended.AppContainer;
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
 * A grouping of {@link Item}s. 
 * 
 * <p>
 * See Item for further discussion on the difference between Items, 
 * Products and {@link Category}s.
 * 
 * <p>
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
public class Product {
	
	/**
	 * Required by persistence layer.
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
	public Product() {
	}
	
	
	/**
	 * Unique identifier across all products.
	 * 
	 * <p>
	 * Allocated by the user.
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
	@Order(1)
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


    @Order(2)
    @DescribedAs("The name of this product, eg Koi fish")
    @BusinessKey("name")
    @MaxLengthOf(50)
    @FieldLengthOf(30)
    public String getName() {
        return _name;
    }
    public void setName(final String name) {
        _name = name;
    }
    private String _name;


    /**
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
     * @return
     */
    @Order(3)
    @DescribedAs("The description of this product")
    @Optional
    @MaxLengthOf(255)
    @FieldLengthOf(50)
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
     * This is a bidirectional m:1 relationship, <i>without</i> containment 
     * semantics.
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
    @Order(4)
    @ContainerOf
    @OppositeOf("products")
    @TypeOf(Item.class)
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
     * Set of {@link Item}s within this product.
     * 
     * <p>
     * This is a bidirectional 1:m relationship with containment semantics
     * (same as CustomerOrder:OrderItem).
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
     * @return
     */
    @Order(5)
    @ContainerOf
    @OppositeOf("product")
    @TypeOf(Item.class)
    public Set<Item> getItems() {
        return _items;
    }
    /**
     * Required by persistence layer.
     * 
     * @param items
     */
    private void setItems(final Set<Item> items) {
    	_items = items;
    }
    private void addToItems(final Item item) {
    	_items.add(item);
    	item.setProduct(this);
    }
    /**
     * Remove using the UI.
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
     * @param item
     */
    public void removeFromItems(final Item item) {
    	_items.remove(item);
    	item.setProduct(null);
    	getAppContainer().delete(item);
    }
    private Set<Item> _items = new LinkedHashSet<Item>();


    
    /**
     * 
     * <p>
     * <i>
     * Programming Model notes: the <code>imagePath</code> parameter is 
     * defined to be of type <code>FileName</code> - a custom value type.
     * </i>
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
     * @param description
     * @param listPrice
     * @param unitCost
     * @param imagePath
     * @return
     */
    @Order(1)
    public Item newItem(
    		@Named("Description")
    		@DescribedAs("Short description of this product")
    		@MaxLengthOf(255)
    		@FieldLengthOf(50)
    		final String description, 
    		@Named("List price")
    		@DescribedAs("Price at which this item currently sells")
    		final BigDecimal listPrice, 
    		@Named("Unit cost")
    		@DescribedAs("Price to produce this item")
    		final BigDecimal unitCost, 
    		@Named("Image")
    		@DescribedAs("Fully qualified file name holding picture of the item")
    		@Optional
    		final FilePath imagePath) {
    	
    	Item item = getAppContainer().createTransient(Item.class);
    	item.init(description, listPrice, unitCost, imagePath, this);
    	addToItems(item);
    	
    	return item;
    }
    /**
     * TODO
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
     * @param description
     * @param listPrice
     * @param unitCost
     * @param imagePath
     * @return
     */
    public IPrerequisites newItemPre(
    		final String description, 
    		final double listPrice, 
    		final double unitCost, 
    		final FilePath imagePath) {
    	return Prerequisites.require(false);
    }
    /**
     * TODO
     * 
     * @param description
     * @param listPrice
     * @param unitCost
     * @param imagePath
     * @return
     */
    public IPrerequisites newItemArgs(
    		final String[] description, 
    		final double[] listPrice, 
    		final double[] unitCost, 
    		final FilePath[] imagePath) {
    	return Prerequisites.require(false);
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
