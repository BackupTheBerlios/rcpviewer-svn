package org.essentialplatform.louis.dnd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.essentialplatform.louis.util.ConfigElementSorter;

/**
 * A global <code>IDndTransferProvider</code> that can handle any object.
 * 
 * <p>
 * Designed to be set up with Spring configuration files (as opposed to being
 * set up using extension points, for example).
 * 
 * <p>
 * Typical setup would be something like:
 * <pre>
 * &lt;bean id="globalDnDTransferProvider" 
 *     class="org.essentialplatform.louis.dnd.SpringConfiguredGlobalDnDTransferProvider">
 *     &lt;list>
 *         &lt;ref bean="transferProvider1"/>
 *         &lt;ref bean="transferProvider2"/>
 *     &lt;/list>
 * &lt;/bean>
 * 
 * &lt;bean id="transferProvider1" 
 *     class="some.example.TransferProvider">
 * &lt;/bean>
 * 
 * &lt;bean id="transferProvider2" 
 *     class="some.other.example.TransferProvider">
 * &lt;/bean>
 * </pre> 
 * <p>
 * Where the <tt>transferProvider1</tt> and <tt>transferProvider2</tt> implement
 * {@link IDndTransferProvider} in turn.
 * 
 * @author Dan Haywood
 */
public final class SpringConfiguredGlobalDnDTransferProvider extends AbstractDndTransferProvider {
	
	private List<IDndTransferProvider> _dndTransferProviders = new ArrayList<IDndTransferProvider>();
	public List<IDndTransferProvider> getDndTransferProviders() {
		return _dndTransferProviders;
	}
	/**
	 * For dependency injection.
	 * 
	 * <p>
	 * Optional; if not specified then defaults to an empty list.
	 * 
	 * @param dndTransferProviders
	 */
	public void setDndTransferProviders(
			List<IDndTransferProvider> dndTransferProviders) {
		_dndTransferProviders = dndTransferProviders;
	}
	
}
