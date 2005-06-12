package mikespike3.application;

import net.sf.plugins.springframework.SpringPlugin;

import org.eclipse.core.runtime.IPlatformRunnable;


/**
 * A convenience class that enables me to start the EasyBean example from the 
 * Eclipse IDE by just right clicking on the project and choosing "Debug As Eclipse Application"
 * 
 * @author ted stockwell
 */
public class EasyBeanSpringLauncher
implements IPlatformRunnable
{
	public Object run(Object args) throws Exception {
		SpringPlugin springPlugin= SpringPlugin.getInstance();
		return springPlugin.launchApplication("de.berlios.rcpviewer.easybean", args);
	}


}
