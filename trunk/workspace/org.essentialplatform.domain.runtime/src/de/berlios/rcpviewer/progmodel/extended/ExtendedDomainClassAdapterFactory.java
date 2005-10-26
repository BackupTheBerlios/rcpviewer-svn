package de.berlios.rcpviewer.progmodel.extended;

import java.util.Map;

import de.berlios.rcpviewer.domain.AbstractAdapterFactory;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Creates an {@link ExtendedRuntimeDomainClass}.
 * 
 * @author Dan Haywood
 *
 * @param <T>
 */
public class ExtendedDomainClassAdapterFactory<T> extends AbstractAdapterFactory<T> {

	public ExtendedDomainClassAdapterFactory() {}
	public ExtendedDomainClassAdapterFactory(String url) {
		super(url);
	}
	public  T createAdapter(IDomainClass adaptedDomainClass) {
		return (T)new ExtendedRuntimeDomainClass(adaptedDomainClass);
	}

}
