package net.sf.plugins.springframework;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringPlugin
extends Plugin
{
	static private SpringPlugin __instance;
	
	public static SpringPlugin getInstance() {
		Plugin plugin= Platform.getPlugin("net.sf.plugins.springframework");
		return (SpringPlugin)plugin;
	}
	
	public SpringPlugin() {
		super();
		__instance= this;
	}
	
	
	public void start(BundleContext pContext) throws Exception {
		super.start(pContext);
	}
	
	public ApplicationContext createApplicationContext(String pContextId)
	throws CoreException
	{
		return createApplicationContext(pContextId, null);
	}

	public ApplicationContext createApplicationContext(String pContextId, ApplicationContext parent)
	throws CoreException
	{
		IConfigurationElement configurationElement= findBeanFactoryConfigurationElement(pContextId);
		SpringPlugin springPlugin= SpringPlugin.getInstance();

		try {
			
			// create parent context
			IConfigurationElement[] importElements= configurationElement.getChildren("import");
			for (int i= 0; importElements != null && i < importElements.length; i++) {
				String importId= importElements[i].getAttribute("id");
				if (importId == null)
					continue;
				parent= springPlugin.createApplicationContext(importId, parent);
			}
			
			
			
			String filePath= configurationElement.getAttribute("file");
			Bundle bundle= Platform.getBundle(configurationElement.getNamespace());
			URL url= bundle.getEntry(filePath);
			url= Platform.resolve(url);
			filePath= new File(url.getFile()).getCanonicalPath();
			
			ClassLoader loader= Thread.currentThread().getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(SpringPlugin.getInstance().getDescriptor().getPluginClassLoader());
				FileSystemXmlApplicationContext context= (parent != null) ?
					new FileSystemXmlApplicationContext(new String[] { filePath }, parent) :
					new FileSystemXmlApplicationContext(filePath);
				return context;
			}
			finally {
				Thread.currentThread().setContextClassLoader(loader);
			}

		} catch (Exception e) {
			Status status= new Status(
					Status.ERROR, 
					configurationElement.getNamespace(),
					0,
					"Error creating bean factory "+pContextId,
					e);
			throw new CoreException(status);
		}
	}

	private IConfigurationElement findBeanFactoryConfigurationElement(String pContextId)
	throws CoreException
	{
		IExtensionPoint extensionPoint= Platform.getExtensionRegistry().getExtensionPoint("net.sf.plugins.springframework.beanFactories");
		IConfigurationElement[] configurationElements= extensionPoint.getConfigurationElements();
		for (int i= 0; i < configurationElements.length; i++) {
			IConfigurationElement element= configurationElements[i];
			String id= element.getAttribute("id");
			if (pContextId.equals(id))
				return element;
		}		
		
		Status status= new Status(
				Status.ERROR, 
				__instance.getBundle().getSymbolicName(),
				0,
				"No such bean factory :"+pContextId,
				(Throwable)null);
		throw new CoreException(status);
	}

	public Object launchApplication(String beanFactoryId, Object pArgs) 
	throws Exception 
	{
		ApplicationContext applicationContext= 
			SpringPlugin.getInstance().createApplicationContext(beanFactoryId);					
		
		IPlatformRunnable main= (IPlatformRunnable)applicationContext.getBean("main");
		return main.run(pArgs);
	}
	
	
}
