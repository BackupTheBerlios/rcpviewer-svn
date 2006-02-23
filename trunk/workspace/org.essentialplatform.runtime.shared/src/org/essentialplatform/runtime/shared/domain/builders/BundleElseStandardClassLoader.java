package org.essentialplatform.runtime.shared.domain.builders;

import org.essentialplatform.core.domain.builders.IClassLoader;
import org.osgi.framework.Bundle;

public class BundleElseStandardClassLoader implements IClassLoader<Class, String> {

	private BundleClassLoader _bundleClassLoader = new BundleClassLoader();
	private StandardClassLoader _standardClassLoader = new StandardClassLoader();
	

	public BundleElseStandardClassLoader() {
	}

	public BundleElseStandardClassLoader(Bundle bundle) {
		setBundle(bundle);
	}

	
	/*
	 * @see org.essentialplatform.core.domain.builders.IClassLoader#loadClass(java.lang.Object)
	 */
	public Class loadClass(String classRepresentation) {
		if (getBundle() != null) {
			return _bundleClassLoader.loadClass(classRepresentation);
		} else {
			return _standardClassLoader.loadClass(classRepresentation);
		}
	}

	
	public Bundle getBundle() {
		return _bundleClassLoader.getBundle();
	}
	public void setBundle(Bundle bundle) {
		_bundleClassLoader.setBundle(bundle);
	}
	
}
