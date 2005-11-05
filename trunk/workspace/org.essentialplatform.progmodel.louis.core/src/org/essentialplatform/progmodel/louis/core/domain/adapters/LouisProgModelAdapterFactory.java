package org.essentialplatform.progmodel.louis.core.domain.adapters;

import java.util.Map;

import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.core.domain.adapters.AbstractAdapterFactory;
import org.essentialplatform.progmodel.louis.core.domain.LouisDomainClass;

/**
 * Creates a {@link LouisDomainClass} for the supplied {@link IDomainClass}.
 * 
 * @author Dan Haywood
 * @param <T>
 */
public class LouisProgModelAdapterFactory<T> extends AbstractAdapterFactory<T> {

	public LouisProgModelAdapterFactory() {}
	public LouisProgModelAdapterFactory(String url) {
		super(url);
	}
	public T createAdapter(IDomainClass adaptedDomainClass) {
		return (T)new LouisDomainClass(adaptedDomainClass);
	}

}
