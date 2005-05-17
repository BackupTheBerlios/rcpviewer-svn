package de.berlios.rcpviewer.progmodel.extended;

import java.util.HashMap;
import java.util.Map;

import de.berlios.rcpviewer.metamodel.IAdapterFactory;

public class AttributeComparatorAdapterFactory<T> implements IAdapterFactory<T> {

	private String url;

	public AttributeComparatorAdapterFactory() {}
	AttributeComparatorAdapterFactory(String url) {
		this.url = url;
	}
	public Map<String, String> getDetails() {
		Map<String, String> details = new HashMap<String, String>();
		details.put("url", url);
		return details;
	}
	public T createAdapter(Map<String, String> details) {
		return (T)new AttributeComparator();
	}

}
