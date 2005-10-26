package de.berlios.rcpviewer.domain;


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
	 * Called for each domain class, for all domain builders except the
	 * primary domain builder.
	 * 
	 * @param <V>
	 * @param domainClass
	 */
	public void build(IDomainClass domainClass);

}
