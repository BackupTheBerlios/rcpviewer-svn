package org.essentialplatform.louis;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * This is the entry point to starting the EssentialPlatform client.
 * 
 * <p>
 * This class is registered as an Eclipse RCP Application 
 * (in <tt>plugin.xml</tt>) under the Id  
 * <tt>org.essentialplatform.louis.app</tt>.  
 */
public class Bootstrap implements IPlatformRunnable {

	private static final String DOMAIN_FLAG = "-domain";
	private static final String STORE_FLAG = "-store";
	
	private static final int ERROR_NO_DOMAIN_FLAG = 100;
	private static final int ERROR_NO_STORE_FLAG = 102;
	
	private static final int ERROR_NO_SPRINGCONTEXT_EXTENSION_POINT = 110;
	private static final int ERROR_NO_APP_BEAN = 120;
	private static final int ERROR_APP_NOT_IPLATFORMRUNNABLE = 130;

	private static final String BEAN_APP_ID = "app";

	private static final String SPRINGCONTEXT_PARENT_XML = "spring-context-parent.xml";
	private static final String SPRINGCONTEXT_EXTENSION_POINT = "org.essentialplatform.louis.springcontext";


	/*
	 * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
	 */
	public Object run(Object args) throws Exception {

		Display display = Display.getDefault();
		try {
			String[] options= (String[])args;
			String domainPluginId = null;
			String store = null;
			for (int i = options.length - 1; 0 < i--;) {
				if (options[i].equals(DOMAIN_FLAG)) {
					domainPluginId = options[i+1];
				}
				if (options[i].equals(STORE_FLAG)) {
					store = options[i+1];
				}
			}
			if (domainPluginId == null) {
				throwCoreException(ERROR_NO_DOMAIN_FLAG, "No domain plugin specified.  Use the -%s <domainPluginId> to specify the plugin to search for a Spring config file.", DOMAIN_FLAG);
			}
			if (store == null) {
				throwCoreException(ERROR_NO_STORE_FLAG, "No store specified.  Use the -%s <domainPluginId> to specify the name of the object store.", STORE_FLAG);
			}
	
			// locate the Extension
			final IConfigurationElement ce = 
				findDomainConfigConfigurationElement(domainPluginId);
	
			if (ce == null) {
				throwCoreException( 
					ERROR_NO_SPRINGCONTEXT_EXTENSION_POINT, "Could not locate application with -%s='%s'", DOMAIN_FLAG, domainPluginId);
			}
	
			// locate the parent (base) config 
			Bundle parentBundle = LouisPlugin.getDefault().getBundle();
			URL parentUrl = parentBundle.getEntry(SPRINGCONTEXT_PARENT_XML);
			parentUrl = Platform.resolve(parentUrl);
			String parentFilePath = new File(parentUrl.getFile()).getCanonicalPath();

			// locate the application's config 
			// this potentially overrides definitions in base config.
			String filePath = ce.getAttribute("file");
			Bundle bundle = Platform.getBundle(ce.getNamespace());
			URL url = bundle.getEntry(filePath);
			url = Platform.resolve(url);
			filePath = new File(url.getFile()).getCanonicalPath();
			
			// load the context
			// the ordering is important - the 2nd filePath will replace any 
			// definitions in the first.
			// (the original design was to use parent contexts, but this didn't 
			// work when I tried it out.  This amounts to much the same thing, anyway).
			FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext(new String[]{parentFilePath, filePath});


			// locate the bean called "app", and check that it implements IPlatformRunnable.
			final Object appObj = context.getBean(BEAN_APP_ID);
			if (appObj == null) {
				throwCoreException(ERROR_NO_APP_BEAN, "Could not locate bean %s", BEAN_APP_ID);
			}
			if (!(appObj instanceof IPlatformRunnable)) {
				throwCoreException(ERROR_APP_NOT_IPLATFORMRUNNABLE, "Object '%s' (from Spring context file) does not implement IPlatformRunnable", BEAN_APP_ID);
			}
			final IPlatformRunnable app = (IPlatformRunnable)appObj;
			
			return app.run(args);
		} catch(Exception ex) {
			// propagating an exception here will mean the the platform will ask us if we want to 
			// inspect the error log (probably not)
			return IPlatformRunnable.EXIT_OK;
		} finally {
			try {
				display.dispose();
			} catch(Exception ex) {
				// if there's been a problem, then attempting to dispose of the
				// display will probably fail.  We (probably) don't want to propagate
				// any exception though because it will cause the Eclispe platform
				// to ask us if we want to look at the error log (probably not)
				
				// Therefore, just swallow any exception.
			}
		}
	}

	private void throwCoreException(final int code, final String formatMessage, Object... args) throws CoreException {
		Status status ;
		status = new Status(
			Status.ERROR, 
			LouisPlugin.getDefault().getBundle().getSymbolicName(),
			code,
			String.format(formatMessage, args),
			(Throwable)null);
		throw new CoreException(status);
	}
	
	/**
	 * Searches through all &lt;springcontext> extensions under the
	 * &lt;springcontext> extension point and returns the <tt>ConfigurationElement</tt>
	 * matching the supplied Id.
	 * 
	 * <p>
	 * The returned <tt>ConfigurationElement</tt> (representing the supplied
	 * <tt>&lt;springcontext/></tt> element according to the extension point's 
	 * schema) has additional info, in particular the name of the Spring config 
	 * file for the contributing plugin.
	 * 
	 * <p>
	 * @return the ConfigurationElement or <tt>null</tt> if none could be found.
	 */
	private IConfigurationElement findDomainConfigConfigurationElement(String springContextId) {
		IExtensionPoint extensionPoint = 
			Platform.getExtensionRegistry().getExtensionPoint(SPRINGCONTEXT_EXTENSION_POINT);
		for(IConfigurationElement element: extensionPoint.getConfigurationElements()) {
			String id = element.getAttribute("id");
			if (springContextId.equals(id)) {
				return element;
			}
		}
		return null; 
	}



}
