package net.sf.plugins.springframework;

import org.eclipse.swt.widgets.Display;
import org.springframework.beans.factory.FactoryBean;

/**
 * Provides Spring access to the SWT display
 * @author ted stockwell
 */
public class DisplayFactoryBean
implements FactoryBean
{
	static {
		Display.getDefault();
	}
	
	public DisplayFactoryBean() {
		
	}

	public Object getObject() throws Exception {
		return Display.getDefault();
	}

	public Class getObjectType() {
		return Display.class;
	}

	public boolean isSingleton() {
		return true;
	}

}
