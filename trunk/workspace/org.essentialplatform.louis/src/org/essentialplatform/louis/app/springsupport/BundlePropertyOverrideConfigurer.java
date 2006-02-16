package org.essentialplatform.louis.app.springsupport;

import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;

public class BundlePropertyOverrideConfigurer extends
		PropertyOverrideConfigurer {

	public BundlePropertyOverrideConfigurer() {
		super();
	}

	@Override
	public void setLocation(Resource arg0) {
		// TODO Auto-generated method stub
		super.setLocation(arg0);
	}
	
	@Override
	public void setLocations(Resource[] arg0) {
		// TODO Auto-generated method stub
		super.setLocations(arg0);
	}
	
}
