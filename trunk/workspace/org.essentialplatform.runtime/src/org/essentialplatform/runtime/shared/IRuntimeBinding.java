package org.essentialplatform.runtime.shared;

import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.osgi.framework.Bundle;

public interface IRuntimeBinding {

	public Bundle getBundle();

	public IDomainBuilder getPrimaryBuilder();

	/*
	 * The <tt>classRepresentation</tt> can be either a string or an actual
	 * class.
	 * 
	 * @see org.essentialplatform.core.deployment.Binding#getInDomainOf(java.lang.Object)
	 */
	public InDomain getInDomainOf(final Object classRepresentation);

	public void assertValid(final Object classRepresentation);


}