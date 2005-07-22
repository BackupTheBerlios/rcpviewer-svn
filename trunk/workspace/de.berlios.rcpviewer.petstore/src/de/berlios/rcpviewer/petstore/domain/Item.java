/*
 * Created on Feb 22, 2003
 */
package de.berlios.rcpviewer.petstore.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import de.berlios.rcpviewer.progmodel.extended.BusinessKey;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Order;
import de.berlios.rcpviewer.progmodel.standard.InDomain;


/**
 * A (class of) item that may be purchased from the petstore.
 * 
 * <p>
 * We should be precise here: an instance of item represents a class of actual
 * things that can be purchased.  The petstore keeps track of the actual number
 * of such-and-such an item that are in stock.
 * 
 * <p>
 * Put another way: it is akin to an entry on a menu, rather than an actual 
 * meal.  Or, if you are familiar with Coad's <i>Java Modelling in Color</i>, 
 * then it's blue, not green).
 * 
 * <p>
 * Items are related to {@link Product}s, which are really just a higher level
 * categorization (Products are also blue).  Products are themselves categorized
 * into {@link Category}s (again blue).
 * 
 * <p>
 * For example:
 * <ul>
 * <li> an Item might be <i>Male Puppy Bulldog</i>, or <i>Spotted Tiger Shark</i>
 * <li> a Product might be <i>Bulldog</i>, or <i>Tiger Shark</i>
 * <li> a Category might be <i>Dog</i>, or <i>Fish</i>.
 * </ul>
 * 
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * @author Dan Haywood
 */
@InDomain
public class Item {

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
	public Item() {
	}
	
	
	public void init(
			final String description, 
			final BigDecimal listPrice, 
			final BigDecimal unitCost, 
			final FilePath imagePath, 
			final Product product) {
		_description = description;
		_imagePath = imagePath;
		_listPrice = listPrice;
		_product = product;
		_unitCost = unitCost;
	}


    /**
     * Assigned by the persistence layer.
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
     */
    @Order(1)
    public String getItemId() {
        return _itemId;
    }
    private void setItemId(final String itemId) {
        _itemId = itemId;
    }
    private String  _itemId;


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
     */
    @Order(2)
    @BusinessKey("product-description.1")
    public Product getProduct() {
        return _product;
    }
    public void setProduct(final Product product) {
        _product = product;
    }
    private Product _product;
    

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
     */
    @Order(3)
    @BusinessKey("product-description.2")
    @MaxLengthOf(255)
    public String getDescription() {
        return _description;
    }
    public void setDescription(final String description) {
        _description = description;
    }
    private String  _description;
    

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
     */
    @Order(4)
    public BigDecimal getUnitCost() {
        return _unitCost;
    }
    public void setUnitCost(final BigDecimal unitCost) {
        _unitCost = unitCost;
    }
    private BigDecimal _unitCost;



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
    public BigDecimal getListPrice() {
        return _listPrice;
    }
    public void setListPrice(final BigDecimal listPrice) {
        _listPrice = listPrice;
    }
    private BigDecimal _listPrice;


   

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
     */
    @Order(6)
    public FilePath getImagePath() {
        return _imagePath;
    }
    public void setImagePath(final FilePath imagePath) {
        _imagePath = imagePath;
    }
    private FilePath _imagePath;



}
