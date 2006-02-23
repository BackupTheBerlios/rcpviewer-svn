package org.essentialplatform.runtime.shared.domain.builders;

import org.apache.log4j.Logger;
import org.essentialplatform.core.domain.builders.ClassLoaderException;
import org.essentialplatform.core.domain.builders.IClassLoader;

public class StandardClassLoader implements IClassLoader<Class,String> {

	protected Logger getLogger() {
		return Logger.getLogger(StandardClassLoader.class);
	}

	public Class loadClass(String classRepresentation) throws ClassLoaderException {
		try {
			return Class.forName(classRepresentation);
		} catch (ClassNotFoundException ex) {
			String msg = String.format(
					"Class#forName(\"%s\") failed", classRepresentation);   //$NON-NLS-1$
			getLogger().error(msg);
			throw new ClassLoaderException(ex);
		}
	}

}
