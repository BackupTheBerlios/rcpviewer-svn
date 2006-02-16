package org.essentialplatform.louis.app.springsupport;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;

/**
 * Works the same as {@link FileSystemXmlApplicationContext}
 * except that it uses the more capable {@link BundleResolvingResourceLoader}
 * to additional resolve {@link org.springframework.core.io.Resource}s of the
 * form <tt>bundle-entry:</tt>.
 * 
 * @author Dan Haywood
 */
public class BundleResolvingXmlApplicationContext extends
		FileSystemXmlApplicationContext {

	public BundleResolvingXmlApplicationContext()
			throws BeansException {
		super(null, false);
	}


	/**
	 * Constructor chaining (fluent interface idiom).
	 * 
	 * @param bundle
	 * @return
	 * @throws BeansException
	 */
	public BundleResolvingXmlApplicationContext resolveAgainst(Bundle bundle)
		throws BeansException {
		_resourceLoaders.add(new BundleResolvingResourceLoader(bundle));
		return this;
	}

	/**
	 * Constructor chaining (fluent interface idiom).
	 * 
	 * @param bundle
	 * @return
	 * @throws BeansException
	 */
	public BundleResolvingXmlApplicationContext configLocated(String configLocation)
		throws BeansException {
		if (!configLocation.startsWith(BundleResolvingResourceLoader.BUNDLE_ENTRY_SCHEME)) {
			configLocation = BundleResolvingResourceLoader.BUNDLE_ENTRY_SCHEME + configLocation;
			
		}
		_configLocations.add(configLocation);
		return this;
	}


	/**
	 * Constructor chaining (fluent interface idiom).
	 * 
	 * @param bundle
	 * @return
	 * @throws BeansException
	 */
	public BundleResolvingXmlApplicationContext parentedBy(ApplicationContext parent)
		throws BeansException {
		setParent(parent);
		return this;
	}
	
	/**
	 * Constructor chaining (fluent interface idiom).
	 * 
	 * @param bundle
	 * @return
	 * @throws BeansException
	 */
	public BundleResolvingXmlApplicationContext andRefresh() {
		refresh();
		return this;
	}
	

	/**
	 * Replaces that in {@link FileSystemXmlApplicationContext} in order to
	 * support fluent interface.
	 */
	private List<String> _configLocations = new ArrayList<String>(); 
	protected String[] getConfigLocations() {
		return _configLocations.toArray(new String[]{});
	}


	
	private final List<BundleResolvingResourceLoader> _resourceLoaders = new ArrayList<BundleResolvingResourceLoader>();
	/**
	 * Uses the implementation provided by {@link BundleResolvingResourceLoader#getResource(String)}.
	 * @see #setResourceLoader
	 */
	public Resource getResource(String location) {
		for(BundleResolvingResourceLoader resourceLoader: _resourceLoaders) {
			Resource resource = resourceLoader.getResourceIfBundleEntry(location);
			if (resource != null) {
				return resource;
			}
		}
		// ask the first to get the resource if it can.
		return _resourceLoaders.get(0).getResource(location);
	}

}
