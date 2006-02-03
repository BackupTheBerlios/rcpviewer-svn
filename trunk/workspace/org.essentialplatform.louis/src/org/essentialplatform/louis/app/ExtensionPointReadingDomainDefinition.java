package org.essentialplatform.louis.app;

import org.eclipse.core.runtime.CoreException;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.essentialplatform.louis.dnd.ExtensionPointReadingGlobalDnDTransferProvider;
import org.essentialplatform.louis.dnd.IDndTransferProvider;
import org.essentialplatform.louis.factory.ExtensionPointReadingGuiFactories;
import org.essentialplatform.louis.factory.IGuiFactories;
import org.essentialplatform.louis.labelproviders.ExtensionPointReadingGlobalLabelProvider;
import org.essentialplatform.louis.labelproviders.ILouisLabelProvider;
import org.essentialplatform.progmodel.essential.runtime.EssentialProgModelRuntimeBuilder;
import org.essentialplatform.runtime.shared.domain.ExtensionPointReadingDomainBootstrap;
import org.essentialplatform.runtime.shared.domain.IDomainBootstrap;

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
public final class ExtensionPointReadingDomainDefinition extends DomainDefinition {


	/**
	 * Overrides all the defaults inherited from base class.
	 * @throws CoreException 
	 */
	public ExtensionPointReadingDomainDefinition() throws CoreException {
		setDomainBootstrap(new ExtensionPointReadingDomainBootstrap());
		setGuiFactories(new ExtensionPointReadingGuiFactories());
		setGlobalLabelProvider(new ExtensionPointReadingGlobalLabelProvider());
		setGlobalDnDTransferProvider(new ExtensionPointReadingGlobalDnDTransferProvider());
	}
	
}
