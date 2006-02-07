package org.essentialplatform.louis.domain;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.essentialplatform.louis.dnd.ExtensionPointReadingGlobalDnDTransferProvider;
import org.essentialplatform.louis.factory.ExtensionPointReadingGuiFactories;
import org.essentialplatform.louis.labelproviders.ExtensionPointReadingGlobalLabelProvider;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;

/**
 * An implementation of {@link IDomainDefinition} whose properties are defaulted
 * to the implementations that read from various extension points.
 * 
 * <p>
 * Developers who are familiar and comfortable with Eclipse extension points
 * can register this class in Spring as the definition of <tt>domain</tt> bean,
 * and thereafter configure their application using the appropriate
 * extension points documented elsewhere.
 * 
 * <p>
 * Note that the name of the domain must still be provided via Spring.  
 * 
 * <p>
 * Example usage:
 * <pre>
 * &lt;bean id="domain" class="org.essentialplatform.louis.app.ExtensionPointReadingDomainDefinition">
 *     &lt;property name="name" value="marketing"/>
 * &lt;/bean>
 * </pre>
 * 
 * @author Dan Haywood
 */
public final class ExtensionPointReadingLouisDefinition extends AbstractLouisDefinition {

	@Override
	protected Logger getLogger() {
		return Logger.getLogger(ExtensionPointReadingLouisDefinition.class);
	}

	public static final String DOMAIN_CLASS_EXTENSION_POINT = "org.essentialplatform.runtime.shared.domainclass";  //$NON-NLS-1$
	public static final String CLASS_PROPERTY = "class"; //$NON-NLS-1$

	/**
	 * Overrides all the defaults inherited from base class.
	 * @throws CoreException 
	 */
	public ExtensionPointReadingLouisDefinition() throws CoreException {
		setGuiFactories(new ExtensionPointReadingGuiFactories());
		setGlobalLabelProvider(new ExtensionPointReadingGlobalLabelProvider());
		setGlobalDndTransferProvider(new ExtensionPointReadingGlobalDnDTransferProvider());
	}


}
