/**
 * 
 */
package org.essentialplatform.core.springsupport;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;

/**
 * Adds support for resources of the form <tt>bundle-entry:/foo/bar</tt>,
 * looked up using {@link Bundle#getEntry(java.lang.String)}.
 *  
 * <p>
 * Install using {@link org.springframework.context.support.GenericApplicationContext#setResourceLoader(org.springframework.core.io.ResourceLoader)}.
 * However, must be called prior to refresh (so cannot use with
 * {@link org.springframework.context.support.FileSystemXmlApplicationContext}.
 *  
 * @author Dan Haywood
 */
public final class BundleResolvingResourceLoader extends DefaultResourceLoader {

	public BundleResolvingResourceLoader(Bundle bundle) {
		_bundle = bundle;
	}

	private final Bundle _bundle;
	static final String BUNDLE_ENTRY_SCHEME = "bundle-entry:";
	/**
	 * The bundle to resolve from.
	 * @return
	 */
	public Bundle getBundle() {
		return _bundle;
	}
	
	@Override
	public Resource getResource(String location) {
		Resource resource = getResourceIfBundleEntry(location);
		if (resource != null) {
			return resource;
		}
		return super.getResource(location);
	}

	/**
	 * Unlike {@link #getResource(String)}, this method will return <tt>null</tt>
	 * if the resource cannot be found.  
	 * 
	 * <p>
	 * This design then allows us to call several such instances one after the
	 * other looking to see if any can locate the resource in their 
	 * corresponding bundle.
	 * 
	 * @param location
	 * @return
	 * @throws IOException 
	 */
	public Resource getResourceIfBundleEntry(String location) {
		Assert.notNull(location, "location is required");
		if (!location.startsWith(BUNDLE_ENTRY_SCHEME)) {
			return null;
		}
		try {
			String bundleLocation = location.substring(BUNDLE_ENTRY_SCHEME.length()); 
			URL bundleLocationUrl = _bundle.getEntry(bundleLocation);
			if (bundleLocationUrl == null) {
				return null;
			}
			bundleLocationUrl = Platform.resolve(bundleLocationUrl);
			return new UrlResource(bundleLocationUrl);
		} catch (IOException ex) {
			return null;
		}
	}
}