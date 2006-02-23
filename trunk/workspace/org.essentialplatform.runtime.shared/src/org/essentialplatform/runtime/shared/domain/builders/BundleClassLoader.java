package org.essentialplatform.runtime.shared.domain.builders;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.builders.ClassLoaderException;
import org.essentialplatform.core.domain.builders.IClassLoader;
import org.essentialplatform.runtime.shared.domain.DomainRegistryException;
import org.essentialplatform.runtime.shared.domain.SpringConfiguredDomainDefinition;
import org.osgi.framework.Bundle;

public class BundleClassLoader implements IClassLoader<Class, String> {

	protected Logger getLogger() {
		return Logger.getLogger(BundleClassLoader.class);
	}

	public BundleClassLoader() {
	}

	public BundleClassLoader(Bundle bundle) {
		setBundle(bundle);
	}

	/*
	 * @see org.essentialplatform.core.domain.builders.IClassLoader#loadClass(java.lang.Object)
	 */
	public Class loadClass(String classRepresentation) {
		try {
			return getBundle().loadClass(classRepresentation);
		} catch (ClassNotFoundException ex) {
			String msg = String.format(
					"Bundle#loadClass(\"%s\") failed", classRepresentation);   //$NON-NLS-1$
			getLogger().error(msg);
			throw new ClassLoaderException(ex);
		}
	}

	
	private Bundle _bundle;
	public Bundle getBundle() {
		return _bundle;
	}
	public void setBundle(Bundle bundle) {
		_bundle = bundle;
	}
	
}
