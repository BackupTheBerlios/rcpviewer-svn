package org.essentialplatform.core.domain.builders;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;


/**
 * Defines an interface for the current (runtime or compile-time) 
 * {@link IDomain} to build up the semantics of the {@link IDomainClass}es 
 * within it.
 * 
 * <p>
 * The <i>primary</i> domain builder is hard-coded (TODO: make into an
 * extension point); <i>secondary</i> domain builders can be registered using 
 * {@link IDomain#addBuilder(IDomainBuilder)} and will be called for each
 * known {@link IDomainClass} when {@link IDomain#done()} is called.
 *  
 * @author Dan Haywood
 *
 */
public interface IDomainBuilder {

	/**
	 * Null object pattern. 
	 */	
	public IDomainBuilder NOOP = new IDomainBuilder() {
		public void build(IDomainClass domainClass) {
			// noop
		}

		public void identifyOppositeReferencesFor(IDomainClass domainClass) {
			// noop
		}
	};

	/**
	 * Called for each domain class, for all domain builders except the
	 * primary domain builder.
	 * 
	 * @param <V>
	 * @param domainClass
	 */
	public void build(IDomainClass domainClass);

	/**
	 * The {@link IDomain} will always call this method before returning a 
	 * looked up domain class, so that the builder can, if necessary, fully
	 * process any bi-directional relationships.
	 * 
	 * <p>
	 * Builders that are able to fully process the domain model from 
	 * {@link #build(IDomainClass)} can provide a no-op implementation of this
	 * method.
	 * 
	 * @param domainClass
	 */
	public void identifyOppositeReferencesFor(IDomainClass domainClass);

}
