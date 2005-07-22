/*
 * Created on Feb 22, 2003
 */
package de.berlios.rcpviewer.petstore.domain;

import java.util.HashSet;
import java.util.Set;

import de.berlios.rcpviewer.progmodel.extended.FieldLengthOf;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Invisible;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Optional;
import de.berlios.rcpviewer.progmodel.extended.Order;
import static de.berlios.rcpviewer.progmodel.extended.Prerequisites.*;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.TypeOf;


/**
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * <p>
 * <i>
 * Programming Model notes:
 * <ul>
 * <li> ...
 * </ul>
 * </i>
 * @author Dan Haywood
 */
@InDomain
@DescribedAs("Categorizes products")
public class Category {


	/**
	 * No-arg constructor, required for persistence layer.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
	 */
	public Category() {
	}


	/**
	 * Unique identifier for this category (not shown in the UI).
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
    public String getCategoryId() {
        return _categoryId;
    }
    /**
     * Automatically assigned by persistence layer.
     * 
     * @param categoryId
     */
    private void setCategoryId(String categoryId) {
        _categoryId = categoryId;
    }
    private String _categoryId;



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
    @Order(1)
    @DescribedAs("Category name")
    @FieldLengthOf(30)
    @MaxLengthOf(50)
    public String getName() {
        return _name;
    }
    public void setName(final String name) {
        _name = name;
    }
    private String _name;



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
    @Order(2)
    @DescribedAs("Description of this category")
    @Optional
    @FieldLengthOf(50)
    @MaxLengthOf(255)
    public String getDescription() {
        return _description;
    }
    public void setDescription(final String description) {
        _description = description;
    }
    private String _description;



    /**
     * Bidirectional 1:m collection of {@link Product}s in this category.
     * 
     * <p>
     * Products may be only in a single category.
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
    @DescribedAs("Products in this category")
    @TypeOf(Product.class)
    public Set<Product> getProducts() {
        return _products;
    }
    public void addToProducts(final Product product) {
        _products.add(product);
        product.setCategory(this);
    }
    public void removeFromProducts(final Product product) {
        _products.remove(product);
        product.setCategory(null);
    }
    private Set<Product> _products = new HashSet<Product>();
    
    
    
    /**
     * Re-classify (move) a {@link Product} from its existing category into this
     * category.
     * 
     * <p>
     * This operation demonstrates drag-n-drop operations.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> ...
     * </ul>
     * </i>
     * 
     * @param product to be moved
     */
    @Order(1)
    @DescribedAs("Move a product from its current category into this category")
    public void reclassify(
    		@DescribedAs("Product to be reclassified")
    		final Product product) {
    	
    	product.getCategory().removeFromProducts(product);
    	this.addToProducts(product);
    	
    }
}
