package org.essentialplatform.runtime.server.springsupport;

import org.essentialplatform.core.springsupport.AbstractBundleFactoryBean;
import org.essentialplatform.runtime.server.ServerPlugin;

public final class ServerBundleFactoryBean extends AbstractBundleFactoryBean {

	public ServerBundleFactoryBean() {
		super(ServerPlugin.getDefault());
	}
}
