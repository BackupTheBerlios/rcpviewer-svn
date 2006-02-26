package org.essentialplatform.core.springsupport;

import org.essentialplatform.core.util.ExtensionPointContributionLocator;
import org.osgi.framework.Bundle;
import org.springframework.beans.factory.InitializingBean;

/**
 * Acts as a {@link org.springframework.beans.factory.FactoryBean} to look up
 * {@link Bundle}s based on a {@link org.eclipse.core.runtime.IConfigurationElement}
 * that they contribute to (for a given <tt>id</tt>). 
 * 
 * @author Dan Haywood
 *
 */
public class ExtensionPointContributingBundleFactoryBean extends
		AbstractBundleFactoryBean implements InitializingBean {


	private String _configurationElementId;
	public String getConfigurationElementId() {
		return _configurationElementId;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param configurationElementId
	 */
	public void setConfigurationElementId(String configurationElementId) {
		_configurationElementId = configurationElementId;
	}
	
	
	private String _id;
	public String getId() {
		return _id;
	}
	/**
	 * For dependency injection.
	 * 
	 * @param id
	 */
	public void setId(String id) {
		_id = id;
	}
	
	
	public void afterPropertiesSet() throws Exception {
		ExtensionPointContributionLocator locator = 
			new ExtensionPointContributionLocator(_configurationElementId, _id);
		Bundle bundle = locator.getBundle();
		if (bundle == null) {
			return;
		}
		setBundle(bundle);
	}

	
	/**
	 * Returns the result from the superclass, or the default if null.
	 */
	@Override
	public Object getObject() throws Exception {
		Object obj = super.getObject();
		if (obj != null) {
			return obj;
		}
		return _defaultBundle;
	}

	private Bundle _defaultBundle;
	/**
	 * So that we can provide a dummy bundle on first pass.
	 * @param bundle
	 */
	public void setDefaultBundle(Bundle bundle) {
		_defaultBundle = bundle;
	}


}
