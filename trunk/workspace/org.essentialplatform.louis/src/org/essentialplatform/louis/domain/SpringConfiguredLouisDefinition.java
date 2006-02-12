package org.essentialplatform.louis.domain;

import org.apache.log4j.Logger;
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
	 * Set by Essential itself (rather than through Spring, say), 
	 * to assist in the verification of domain classes (so that it can use 
	 * the appropriate <tt>ClassLoader</tt>).
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
