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

	
}
