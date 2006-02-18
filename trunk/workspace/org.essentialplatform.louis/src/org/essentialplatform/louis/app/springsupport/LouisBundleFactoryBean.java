package org.essentialplatform.louis.app.springsupport;

import org.essentialplatform.core.springsupport.AbstractBundleFactoryBean;
import org.essentialplatform.louis.LouisPlugin;

public final class LouisBundleFactoryBean extends AbstractBundleFactoryBean {

	public LouisBundleFactoryBean() {
		super(LouisPlugin.getDefault());
	}
}
