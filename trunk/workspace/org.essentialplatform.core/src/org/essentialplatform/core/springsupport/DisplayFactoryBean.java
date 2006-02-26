package org.essentialplatform.core.springsupport;

import org.eclipse.swt.widgets.Display;
import org.springframework.beans.factory.FactoryBean;

/**
 * Provides Spring access to the SWT display.
 * 
 * <p>
 * Note that this implements the Spring {@link org.springframework.beans.factory.FactoryBean}, 
 * so that the bean used when referenced elsewhere in the Spring config file
 * is that returned when Spring invokes the {@link #getObject()} method.
 * 
 * @author ted stockwell
 */
public final class DisplayFactoryBean implements FactoryBean
{

	/**
	 * Make sure that there is a Display. 
	 * 
	 * <p>
	 * This also makes this thread into the UI thread.
	 */
	static {
		Display.getDefault();
	}

	/**
	 * No-arg constructor required by Spring.
	 */
	public DisplayFactoryBean() {
	}

	/*
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return Display.getDefault();
	}

	/*
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class getObjectType() {
		return Display.class;
	}

	/*
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

}
