/*
 * Created on Feb 22, 2003
 */
package de.berlios.rcpviewer.petstore.domain;

import java.util.HashSet;
import java.util.Set;

import de.berlios.rcpviewer.progmodel.extended.DeleteOperation;
import de.berlios.rcpviewer.progmodel.extended.FieldLengthOf;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.Invisible;
import de.berlios.rcpviewer.progmodel.extended.Lookup;
import de.berlios.rcpviewer.progmodel.extended.MaxLengthOf;
import de.berlios.rcpviewer.progmodel.extended.Optional;
import de.berlios.rcpviewer.progmodel.extended.RelativeOrder;
import de.berlios.rcpviewer.progmodel.extended.Prerequisites;
import de.berlios.rcpviewer.progmodel.standard.DescribedAs;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.TypeOf;


/**
 * Categorizes products.
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
 * <li>TODO: Hibernate mapping to T_CATEGORY
 * </ul>
 * 
 * <p>
 * Adapted from original xpetstore implementation by Herve Tchepannou.
 * 
 * @author Dan Haywood
 */
@InDomain
@DescribedAs("Categorizes products")
public class Category {


	/**
	 * Constructor.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> No-arg constructor required by platform.
     * </ul>
     * </i>
	 */
	public Category() {
	}


	/**
	 * Unique identifier for this category (not shown in the UI), assigned by
	 * the platform automatically.
	 * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul> 
     * <li> The <code>@Invisible</code> annotation indicates that the attribute
     *      should not be displayed in the UI.
     * <li> See overview for discussion on other programming model conventions
     *      and annotations.
     * </i>
	 */
	@Invisible
    public String getCategoryId() {
        return _categoryId;
    }
    private void setCategoryId(final String categoryId) {
        _categoryId = categoryId;
    }
    private String _categoryId;



    /**
     * Name of this category.
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
    @FieldLengthOf(30)
    @MaxLengthOf(50)
    @DescribedAs("Name of this category.")
    public String getName() {
        return _name;
    }
    private void setName(final String name) {
        _name = name;
    }
    private String _name;



    /**
     * Description of this category.
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
    @RelativeOrder(2)
    @FieldLengthOf(50)
    @MaxLengthOf(255)
    @DescribedAs("Description of this category.")
    public String getDescription() {
        return _description;
    }
    private void setDescription(final String description) {
        _description = description;
    }
    private String _description;



    /**
     * Products in this category.
     * 
     * <p>
     * This is a bidirectional 1:m collection of {@link Product}s in this 
     * category.
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
    @RelativeOrder(3)
    @TypeOf(Product.class)
    @DescribedAs("Products in this category.")
    public Set<Product> getProducts() {
        return _products;
    }
    private void setProducts(final Set<Product> products) {
    	_products = products;
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
     * Re-classify (move) a {@link Product} from its existing category into 
     * this category.
     * 
     * <p>
     * The product is removed from the set of products of its existing
     * category (if any), and then added to the set of products of this
     * category.
     * 
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> See overview for discussion on programming model conventions
     *      and annotations.
     * </ul>
     * </i>
     * 
     * @param product - product to be moved between categories
     */
    @RelativeOrder(1)
    @DescribedAs("Move a product from its current category into this category")
    public void reclassify(
    		@DescribedAs("Product to be reclassified")
    		final Product product) {
    	
    	Category existingCategory = product.getCategory();
    	if (existingCategory != null) {
        	existingCategory.removeFromProducts(product);
    	}
    	this.addToProducts(product);
    	
    }
    /**
     * Ensure that the supplied product is already in a different category.
     * 
     * <p>
     * Since product is a mandatory parameter, we do not need to perform
     * a null check on the product parameter itself.
     * 
     * @param product
     * @return
     */
    public IPrerequisites reclassifyPre(
    		final Product product) {
    	return Prerequisites.require(
    			product.getCategory() != this, 
    			"Product is already in this category.");
    }
    
    
    /**
     * Delete this category, subject to referential integrity constraints.
     *
     * <p>
     * <i>
     * Programming Model notes:
     * <ul>
     * <li> The {@link DeleteOperation} annotation indicates that this operation
     *      should be rendered in a standardized way. 
     * <li> The {@link #deletePre()} performs a referential integrity check,
     *      disabling the operation if any referencing {@link Product}s can be 
     *      found.
     * </ul>
     * </i>
     *
     */
    @DeleteOperation
    public void delete() {
    	// does nothing; the platform will perform the delete implicitly.
    }
    public IPrerequisites deletePre() {
    	return Prerequisites.require(
    			getProducts().size() == 0, 
    			"This category is referenced by one or more products.");
    }


}
