package net.sf.plugins.springframework;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.springframework.context.ApplicationContext;

/**
 * This class is registered in the Spring plugin with an application id of 
 * net.sf.plugins.springframwework.launcher.
 * 
 * @author ted stockwell
 *
 */
public class SpringApplicationLauncher 
implements IPlatformRunnable
{
	public Object run(Object pArgs) throws Exception {
		
		// discover the Spring application context to use
		String contextId= null;
		String[] options= (String[])pArgs;
		for (int i= options.length - 1; 0 < i--;)
			if (options[i].equals("-beanFactory")) {
				contextId= options[i+1];
				break;
			}
		if (contextId == null)
			contextId= System.getProperty("net.sf.plugins.springframework.main");
		if (contextId == null)
			throw new Error("No application context specified.  Use the -beanFactory <contextId> to specify the context or set the net.sf.plugins.springfactory.main to the context id");

		ApplicationContext applicationContext= 
			SpringPlugin.getInstance().createApplicationContext(contextId);					
		
		IPlatformRunnable main= (IPlatformRunnable)applicationContext.getBean("main");
		return main.run(pArgs);
	}
}
