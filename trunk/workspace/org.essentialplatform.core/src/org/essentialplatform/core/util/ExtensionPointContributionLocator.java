package org.essentialplatform.core.util;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * A utility class for locating a {@link org.eclipse.core.runtime.IConfigurationElement} and
 * owning {@link Bundle} that contributes to the specified configuration element
 * with a specified <tt>Id</tt>.
 * 
 * <p>
 * The <tt>Bundle</tt> is obtained from the <tt>IConfigurationElement</tt>'s
 * namespace. 
 * 
 * @author Dan Haywood
 */
public final class ExtensionPointContributionLocator {

	public ExtensionPointContributionLocator(String configurationElementId, String id) {
		_configurationElementId = configurationElementId;
		_id = id;
		deriveBundleAndConfigurationElementIfPossible();
	}

	private final String _configurationElementId;
	public String getConfigurationElementId() {
		return _configurationElementId;
	}

	
	private final String _id;
	public String getId() {
		return _id;
	}
	

	private IConfigurationElement _configurationElement;
	public IConfigurationElement getConfigurationElement() {
		return _configurationElement;
	}
	
	
	private Bundle _bundle;
	public Bundle getBundle() {
		return _bundle;
	}
	
	/**
	 * Looks for a plugin that extends the configuration element (as identified
	 * by {@link #setConfigurationElementId(String)} where the contribution's
	 * <tt>id</tt> matches that injected into {@link #setId(String)}.
	 * 
	 * <p>
	 * If found then obtains the {@link Bundle} from configuration element and
	 * sets (otherwise does nothing).
	 */
	private void deriveBundleAndConfigurationElementIfPossible() {
		if (_configurationElementId == null || _id == null) {
			return;
		}
		
		IExtensionPoint extensionPoint = 
			Platform.getExtensionRegistry().getExtensionPoint(_configurationElementId);
		for(IConfigurationElement element: extensionPoint.getConfigurationElements()) {
			String id = element.getAttribute("id");
			if (_id.equals(id)) {
				_configurationElement = element;
				_bundle = Platform.getBundle(_configurationElement.getNamespace());
				return;
			}
		}
	}

}
