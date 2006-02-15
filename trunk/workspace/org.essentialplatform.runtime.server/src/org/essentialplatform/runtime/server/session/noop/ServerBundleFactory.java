package org.essentialplatform.runtime.server.session.noop;

import org.essentialplatform.runtime.server.ServerPlugin;
import org.osgi.framework.Bundle;
import org.springframework.beans.factory.FactoryBean;

public class ServerBundleFactory implements FactoryBean {

	public Object getObject() throws Exception {
		return ServerPlugin.getDefault().getBundle();
	}

	public Class getObjectType() {
		return Bundle.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
