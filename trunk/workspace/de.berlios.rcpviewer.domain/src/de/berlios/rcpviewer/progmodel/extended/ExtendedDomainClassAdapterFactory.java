package de.berlios.rcpviewer.progmodel.extended;

import java.util.HashMap;
import java.util.Map;

import de.berlios.rcpviewer.domain.AbstractAdapterFactory;
import de.berlios.rcpviewer.domain.IAdapterFactory;
import de.berlios.rcpviewer.domain.IDomainClass;

public class ExtendedDomainClassAdapterFactory<T> extends AbstractAdapterFactory<T> {

	public ExtendedDomainClassAdapterFactory() {}
	public ExtendedDomainClassAdapterFactory(String url) {
		super(url);
	}
	public <V> T createAdapter(IDomainClass<V> adaptedDomainClass, Map<String, String> details) {
		return (T)new ExtendedDomainClass<V>(adaptedDomainClass);
	}

}
