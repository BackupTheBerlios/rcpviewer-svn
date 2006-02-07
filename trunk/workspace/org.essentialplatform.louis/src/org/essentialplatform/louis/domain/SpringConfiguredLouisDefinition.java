package org.essentialplatform.louis.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.louis.dnd.IDndTransferProvider;
import org.essentialplatform.louis.factory.IGuiFactories;
import org.essentialplatform.louis.labelproviders.ILouisLabelProvider;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.shared.domain.DomainBootstrapException;
import org.essentialplatform.runtime.shared.domain.DomainRegistryException;
import org.essentialplatform.runtime.shared.domain.IDomainRegistrar;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;
import org.osgi.framework.Bundle;

/**
 * Implementation of {@link ILouisDefinition} designed for configuration
 * by Spring, or manual instantiation.
 * 
 * @author Dan Haywood
 */
public class SpringConfiguredLouisDefinition extends AbstractLouisDefinition {

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(SpringConfiguredLouisDefinition.class);
	}

	
	////////////////////////////////////////////////////////////////////
	// Bundle
	////////////////////////////////////////////////////////////////////
	
	private Bundle _bundle;
	/**
	 * The (Eclipse) bundle representing the domain plugin.
	 * 
	 * <p>
	 * Set by Essential itself (rather than through Spring, say), primarily
	 * to assist the {@link IDomainRegistrar} in the verification of 
	 * domain classes (so that it can use the appropriate <tt>ClassLoader</tt>).
	 */
	public Bundle getBundle() {
		return _bundle;
	}
	/*
	 * Set by Essential itself (rather than through Spring, say).
	 */
	public void setBundle(Bundle domainBundle) {
		_bundle = domainBundle;
	}
	
	
}
