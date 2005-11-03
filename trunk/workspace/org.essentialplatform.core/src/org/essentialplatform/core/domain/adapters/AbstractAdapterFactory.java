package org.essentialplatform.core.domain.adapters;

import java.util.HashMap;
import java.util.Map;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.adapters.IAdapterFactory;

public abstract class AbstractAdapterFactory<T> implements IAdapterFactory<T> {

	protected AbstractAdapterFactory() {}
	protected AbstractAdapterFactory(String url) {
		this.url = url;
	}

	private String url;
	public String getUrl() {
		return url;
	}

	public Map<String, String> getDetails() {
		Map<String, String> details = new HashMap<String, String>();
		details.put("url", getUrl());
		return details;
	}
	public abstract T createAdapter(IDomainClass adaptedDomainClass);
	
}
