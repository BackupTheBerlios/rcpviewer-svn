package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Concrete implementation of {@link IFeatureId}, describing an attribute, an
 * operation or a class itself.
 * 
 * <p>
 * Features are value objects; they can be compared for equality using
 * {@link Object#equals(Object)}.
 * 
 * @author Dan Haywood
 */
public final class FeatureId<V> implements IFeatureId {

	private final IDomainClass<V> _domainClass;
	private final Type _featureType;
	private final String _name;
	/**
	 * derived string representation.
	 */
	private final String _asString;
	
	public static <T> IFeatureId create( 
			final String name, 
			final IDomainClass<T> domainClass, 
			final Type featureType) {
		return new FeatureId(name, domainClass, featureType);
	}
	
	private FeatureId( 
			final String name, 
			final IDomainClass<V> domainClass, 
			final Type featureType) {

		_name = name;
		_domainClass = domainClass;
		_featureType = featureType;
		_asString = _domainClass.getEClass().getInstanceClassName() + "#" + _name;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.runtime.IFeature#getDomainClass()
	 */
	public IDomainClass<V> getDomainClass() {
		return _domainClass;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.runtime.IFeature#getFeatureType()
	 */
	public Type getFeatureType() {
		return _featureType;
	}

	/*
	 * @see de.berlios.rcpviewer.domain.runtime.IFeature#getName()
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Whether this feature and another are equal by value.
	 * 
	 * <p>
	 * Since feature names are unique, simply delegates to {@link #getName()}.
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(final Object other) {
		if (other instanceof IFeatureId) {
			return this.equals((IFeatureId)other);
		}
		return false;
	}

	/**
	 * Typesafe implementation of <code>equals(Object)</code>, to
	 * determine whether this feature and another are equal by value.
	 * 
	 * <p>
	 * Since feature names are unique, simply delegates to {@link #getName()}.
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(final IFeatureId other) {
		return getDomainClass() == other.getDomainClass() &&
		       getName().equals(other.getName());
	}
	
	/**
	 * Simply uses the hashcode of the {@link #getName()} (a String).
	 */
	public int hashCode() {
		return getName().hashCode();
	}

	public String toString() {
		return _asString;
	}
}
