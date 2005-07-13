package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Identifies a feature (attribute or operation) of a class, such that it 
 * may be used in various contexts (typically system-level services implementing
 * aspects that are strictly outside the domain model itself).
 * 
 * <p>
 * For example:
 * <ul>
 * <li> an authorization manager might use a feature as a way of determining 
 *      if the currently logged on user can access the feature
 * <li> an internationalization service might use it to provide localized 
 *      names/descriptions of the feature inq question.
 * <li> a help manager might use a feature to look up (or render) help text.
 * </u>
 * 
 * @author Dan Haywood
 *
 */
public interface IFeatureId {
	
	/**
	 * Type of feature, namely the class itself, or an attribute, or an 
	 * operation.
	 * 
	 * @author Dan Haywood
	 */
	public enum Type {
		CLASS, ATTRIBUTE, OPERATION
	}
	
	/**
	 * The class to which this feature belongs.
	 * @return
	 */
	public IDomainClass getDomainClass();
	
	/**
	 * The {@link Type} of feature.
	 * 
	 * <p>
	 * Typically the interest will lie with {@link Type#ATTRIBUTE}s and
	 * {@link Type#OPERATION}s, but some services (eg internationalization?)
	 * might be interested in a feature representing the class itself
	 * ({@link Type#CLASS}).
	 * 
	 */
	public Type getFeatureType();
	
	/**
	 * The name of this feature.
	 * 
	 * <p>
	 * Follows Javadoc conventions, eg:
	 * <ul>
	 * <li><code>com.mycompany.Customer</code> for a {@link Type#CLASS}
	 * <li><code>com.mycompany.Customer#firstName</code> for a
	 * {@link Type#ATTRIBUTE} of the class (called <code>firstName</code>)
	 * <li><code>com.mycompany.Customer#placeOrder</code> for a
	 *  {@link Type#OPERATION} of the class (called <code>placeOrder</code>)
	 * </ul>
	 * 
	 * @return
	 */
	public String getName();
}
