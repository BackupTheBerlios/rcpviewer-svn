package org.essentialplatform.core.domain.builders;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;


/**
 * Defines an interface for the current (runtime or compile-time) 
 * {@link IDomain} to build up the semantics of the {@link IDomainClass}es 
 * within it.
 * 
 * <p>
 * The <i>primary</i> domain builder takes responsibiliity for building up the
 * base structure of the metamodel, identifying classes, attributes, references
 * and so forth.  In addition, <i>secondary</i> domain builders can be 
 * registered to capture additional semantics specific to the programming 
 * model that they represent.  The semantics are typically stored in
 * extension classes.  A typical example would be to capture annotations 
 * specific to the host platform for which the viewing mechanism is targetted
 * (eg Eclipse RCP or perhaps CSS or Tapestry components).
 *
 * <p>
 * In addition, Domain builders targetted for the runtime environment will
 * create {@link IDomainClass}es implementations that have a binding
 * ({@link org.essentialplatform.core.domain.IDomainClass#getBinding()}) that
 * in turn can be used to instantiation domain objects (to wrap the underlying
 * pojos).  These domain object take care of the choreography between the
 * larger Essential platform and an individual pojo.  This choreography may
 * become pluggable eventually, and would be supported by a strategy of the
 * runtime binding which would delegate the interactions.
 * 
 * <p>
 * TODO: the primary and secondary builders are currently hard-coded; make 
 * into an extension point.  (The former is passed as a constructor to
 * {@link org.essentialplatform.runtime.deployment.Binding}; the latter are 
 * passed in using {@link Domain#addBuilder(IDomainBuilder)}.
 * 
 * @author Dan Haywood
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

		public void setClassLoader(IClassLoader classLoader) {
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

	
	/**
	 * Sets the class loader, in case needed.
	 * 
	 * @param classLoader
	 */
	void setClassLoader(IClassLoader classLoader);
}
