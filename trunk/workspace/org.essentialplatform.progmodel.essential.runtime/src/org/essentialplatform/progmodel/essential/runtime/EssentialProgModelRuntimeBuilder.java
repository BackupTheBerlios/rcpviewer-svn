package org.essentialplatform.progmodel.essential.runtime;

import org.essentialplatform.core.domain.builders.AbstractCompositeDomainBuilder;
import org.essentialplatform.core.domain.builders.IClassLoader;
import org.essentialplatform.core.domain.builders.IDomainBuilder;
import org.osgi.framework.Bundle;


public final class EssentialProgModelRuntimeBuilder extends AbstractCompositeDomainBuilder {

	public EssentialProgModelRuntimeBuilder() {
		super(new IDomainBuilder[]{
				new EssentialProgModelStandardRuntimeBuilder(),
				new EssentialProgModelExtendedRuntimeBuilder(),
		});
	}

	
	
	
	private IClassLoader _classLoader;
	public IClassLoader getClassLoader() {
		return _classLoader;
	}
	public void setClassLoader(IClassLoader classLoader) {
		_classLoader = classLoader;
		for(IDomainBuilder builder: getBuilders()) {
			builder.setClassLoader(classLoader);
		}
	}

}
