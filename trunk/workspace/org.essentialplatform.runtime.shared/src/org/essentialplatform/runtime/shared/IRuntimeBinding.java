package org.essentialplatform.runtime.shared;

import org.essentialplatform.core.deployment.IBinding;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.osgi.framework.Bundle;

public interface IRuntimeBinding extends IBinding {

	/**
	 * In order to validate class representations.
	 * @param bundle
	 */
	public void init(Bundle bundle);
	public Bundle getBundle();

	public void assertValid(final Object classRepresentation);

	/*
	 * The <tt>classRepresentation</tt> can be either a string or an actual
	 * class.
	 * 
	 * @see org.essentialplatform.core.deployment.Binding#getInDomainOf(java.lang.Object)
	 */
	public InDomain getInDomainOf(final Object classRepresentation);

	public IDomainBuilder getPrimaryBuilder();
	public IRuntimeBinding initPrimaryBuilder(IDomainBuilder domainBuilder);

}