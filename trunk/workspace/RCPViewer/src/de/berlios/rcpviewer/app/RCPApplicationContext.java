package de.berlios.rcpviewer.app;

import org.nakedobjects.object.ApplicationContext;
import org.nakedobjects.object.NakedClass;

public class RCPApplicationContext 
extends ApplicationContext 
{
	
	public NakedClass addClass(String className) {
		return super.addClass(className);
	}
}
