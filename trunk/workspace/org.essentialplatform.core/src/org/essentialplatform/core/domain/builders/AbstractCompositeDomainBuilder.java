package org.essentialplatform.core.domain.builders;

import java.util.List;
import java.util.ArrayList;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.builders.IDomainBuilder;

/**
 * Composite pattern applied to {@link IDomainBuilder}.
 * 
 * @author Dan Haywood
 */
public abstract class AbstractCompositeDomainBuilder implements IDomainBuilder {

	private final List<IDomainBuilder> _builders = new ArrayList<IDomainBuilder>();
	/**
	 * Create composite builder with no builders. 
	 *
	 * <p>
	 * Use {@link #addBuilder(IDomainBuilder)} to add builders to be invoked
	 * when this builder is itself asked to build the domain model.
	 */
	protected AbstractCompositeDomainBuilder() {
	}
	/**
	 * Convenience constructor that sets up the list of builders using the
	 * supplied array.
	 * 
	 * <p>
	 * Further builders may still be added using {@link #addBuilder(IDomainBuilder)}.
	 *  
	 * @param builders
	 */
	protected AbstractCompositeDomainBuilder(final IDomainBuilder[] builders) {
		super();
		for(IDomainBuilder builder: builders) {
			addBuilder(builder);
		}
	}

	/**
	 * Adds a {@link IDomainBuilder} to the list of the builders that will be
	 * used to build the metamodel.
	 * 
	 * @param builder
	 */
	protected final void addBuilder(IDomainBuilder builder) {
		_builders.add(builder);
	}
	
	/**
	 * Invokes build on all builders defined, in the order they were added.
	 *
	 * @see IDomainBuilder#build(IDomainClass)
	 * @throws IllegalStateException if no builders have been defined.
	 */
	public final void build(IDomainClass domainClass) {
		if (_builders.size() == 0) {
			throw new IllegalStateException("No builders defined.");
		}
		for(IDomainBuilder builder: _builders) {
			builder.build(domainClass);
		}
	}

	public final void identifyOppositeReferencesFor(IDomainClass domainClass) {
		for(IDomainBuilder builder: _builders) {
			builder.identifyOppositeReferencesFor(domainClass);
		}
	}

}
