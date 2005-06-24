package de.berlios.rcpviewer.progmodel.extended;

import java.util.HashMap;
import java.util.Map;

import de.berlios.rcpviewer.domain.IAdapterFactory;
import de.berlios.rcpviewer.domain.IDomainClass;

public class ExtendedDomainClassAdapterFactory<T> implements IAdapterFactory<T> {

	private String url;

	public ExtendedDomainClassAdapterFactory() {}
	ExtendedDomainClassAdapterFactory(String url) {
		this.url = url;
	}
	public Map<String, String> getDetails() {
		Map<String, String> details = new HashMap<String, String>();
		details.put("url", url);
		return details;
	}
	public <V> T createAdapter(IDomainClass<V> adaptedDomainClass, Map<String, String> details) {
		return (T)new ExtendedDomainClass<V>(adaptedDomainClass);
	}

}
