package org.essentialplatform.louis.factory;

import java.util.ArrayList;
import java.util.List;

import org.essentialplatform.louis.factory.attribute.BigDecimalAttributeGuiFactory;
import org.essentialplatform.louis.factory.attribute.BooleanAttributeGuiFactory;
import org.essentialplatform.louis.factory.attribute.CharAttributeGuiFactory;
import org.essentialplatform.louis.factory.attribute.DateAttributeGuiFactory;
import org.essentialplatform.louis.factory.attribute.PrimitiveAttributeGuiFactory;
import org.essentialplatform.louis.factory.attribute.StringAttributeGuiFactory;
import org.essentialplatform.louis.factory.reference.ReferenceGuiFactory;
import org.essentialplatform.louis.factory.reference.collection.CollectionGuiFactory;
import org.essentialplatform.louis.factory.reference.collection.CollectionMasterChildGuiFactory;
import org.essentialplatform.louis.factory.reference.collection.CollectionTableGuiFactory;

public abstract class AbstractGuiFactoriesChain implements IGuiFactories {

	/**
	 * That this is a List is important as it enforces a specific search order.
	 */
	private final List<IGuiFactory<?>> _factories = new ArrayList<IGuiFactory<?>>();
	/**
	 * Subclasses should call in their constructor, to register their factories
	 * first.
	 * 
	 * @param factory
	 */
	protected void addFactory(IGuiFactory factory) {
		_factories.add(factory);
	}


	/**
	 * Subclass implementations should register their own factories first, in
	 * their constructor, using {@link #addFactory(IGuiFactory)}.
	 */
	public final void init() {
		// now add default factories, again in order of their intended
		// prioritisation
		addFactory(new StringAttributeGuiFactory());
		addFactory(new BigDecimalAttributeGuiFactory());
		addFactory(new DateAttributeGuiFactory());
		addFactory(new BooleanAttributeGuiFactory());
		addFactory(new CharAttributeGuiFactory());
		addFactory(new PrimitiveAttributeGuiFactory());
		addFactory(new ReferenceGuiFactory());
		addFactory(new CollectionGuiFactory());
		addFactory(new CollectionTableGuiFactory());
		addFactory(new CollectionMasterChildGuiFactory());
		addFactory(new DomainClassGuiFactory());
	}


	/*
	 * @see org.essentialplatform.louis.factory.IGuiFactories#getFactory(java.lang.Object, org.essentialplatform.louis.factory.IGuiFactory)
	 */
	public final IGuiFactory<?> getFactory(Object model, IGuiFactory parent) {
		for( IGuiFactory<?> factory : _factories ) {
			if ( factory.isApplicable( model, parent ) ) {
				return factory;
			}
		}
		// catch-all
		return new DefaultGuiFactory();
	}
	
	/*
	 * @see org.essentialplatform.louis.factory.IGuiFactories#getFactories(java.lang.Object, org.essentialplatform.louis.factory.IGuiFactory)
	 */
	public final List<IGuiFactory<?>> getFactories(Object model, IGuiFactory<?> parent) {
		List<IGuiFactory<?>> applicable = new ArrayList<IGuiFactory<?>>();
		for( IGuiFactory<?> factory : _factories ) {
			if ( factory.isApplicable( model, parent ) ) {
				applicable.add( factory );
			}
		}
		// catch-all
		if ( applicable.isEmpty() ) applicable.add( new DefaultGuiFactory() );
		return applicable;
	}

}
