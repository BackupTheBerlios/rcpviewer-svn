package org.essentialplatform.core.domain.builders;


public interface IClassLoader<V,K> {
	public V loadClass(final K classRepresentation) throws ClassLoaderException;
	
}
