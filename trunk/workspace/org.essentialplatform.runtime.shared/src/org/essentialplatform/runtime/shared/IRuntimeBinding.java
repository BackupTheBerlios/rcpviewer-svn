package org.essentialplatform.runtime.shared;

import org.essentialplatform.core.deployment.IBinding;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;
import org.essentialplatform.runtime.shared.domain.IDomainRegistrar;
import org.osgi.framework.Bundle;

public interface IRuntimeBinding extends IBinding {

	public IRuntimeBinding init(IDomainBuilder domainBuilder);
	
	public IDomainBuilder getPrimaryBuilder();
	public void setPrimaryBuilder(IDomainBuilder domainBuilder);

	public Bundle getBundle();


	/*
	 * The <tt>classRepresentation</tt> can be either a string or an actual
	 * class.
	 * 
	 * @see org.essentialplatform.core.deployment.Binding#getInDomainOf(java.lang.Object)
	 */
	public InDomain getInDomainOf(final Object classRepresentation);

	public void assertValid(final Object classRepresentation);


	IDomainRegistrar getDomainRegistrar();
	void setDomainRegistrar(IDomainRegistrar domainRegistrar);
}