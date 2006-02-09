package org.essentialplatform.louis;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.essentialplatform.core.deployment.IBinding;
import org.essentialplatform.louis.app.IApplication;
import org.essentialplatform.louis.domain.ILouisDefinition;
import org.essentialplatform.runtime.server.ServerPlugin;
import org.essentialplatform.runtime.server.StandaloneServer;
import org.essentialplatform.runtime.shared.IRuntimeBinding;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;
import org.essentialplatform.runtime.shared.domain.IDomainRegistrar;
import org.osgi.framework.Bundle;
import org.springframework.beans.BeansException;
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
	private static final String NO_SERVER_FLAG = "-noserver";

	private static final int ERROR_NO_DOMAIN_FLAG = 100;
	private static final int ERROR_NO_STORE_FLAG = 102;
	
	private static final int ERROR_NO_SPRINGCONTEXT_LOUIS_EXTENSION_POINT = 110;
	private static final int ERROR_BEAN_WRONG_TYPE = 150;

	private static final int ERROR_NO_SERVER_BEAN = 200;
	private static final String BEAN_SERVER_ID = "server";
	
	private static final int ERROR_NO_APP_BEAN = 300;
	private static final int ERROR_NO_DOMAIN_BEAN = 310;
	private static final int ERROR_NO_LOUIS_BEAN = 320;
	
	private static final String BEAN_APP_ID = "app";
	private static final String BEAN_DOMAIN_ID = "domain";
	private static final String BEAN_LOUIS_ID = "louis";

	private static final String SPRINGCONTEXT_EXTENSION_POINT = "org.essentialplatform.louis.springcontext";
	private static final String LOUIS_FILE = "louis-file";
	private static final String DOMAIN_FILE = "domain-file";
	private static final String CLIENTSIDE_SPRINGCONTEXT_BASE_XML = "spring-context-base.xml";
	private static final String SERVERSIDE_SPRINGCONTEXT_XML = "spring-context.xml";

	private FileSystemXmlApplicationContext _clientContext;
	private ILouisDefinition _louisDefinition;
	private IApplication _app;
	
	private FileSystemXmlApplicationContext _serverContext;
	private IDomainDefinition _serverDomainDefinition;
	private StandaloneServer _server;


	/*
	 * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
	 */
	public Object run(Object args) throws Exception {

		Display display = Display.getDefault();
		try {
			String[] options= (String[])args;
			String domainPluginId = null;
			boolean runServer = true;
			String store = null;
			for (int i = options.length - 1; 0 < i--;) {
				if (options[i].equals(DOMAIN_FLAG)) {
					domainPluginId = options[i+1];
				}
				if (options[i].equals(STORE_FLAG)) {
					store = options[i+1];
				}
				if (options[i].equals(NO_SERVER_FLAG)) {
					runServer = false;
				}
			}
			if (domainPluginId == null) {
				throwCoreException(ERROR_NO_DOMAIN_FLAG, "No domain plugin specified.  Use the -%s <domainPluginId> to specify the plugin to search for a Spring config file.", DOMAIN_FLAG);
			}
			if (store == null) {
				throwCoreException(ERROR_NO_STORE_FLAG, "No store specified.  Use the -%s <domainPluginId> to specify the name of the object store.", STORE_FLAG);
			}
	
			// locate the Extension
			final IConfigurationElement domainPluginCE = 
				findLouisSpringContextConfigurationElement(domainPluginId);
			if (domainPluginCE == null) {
				throwCoreException( 
					ERROR_NO_SPRINGCONTEXT_LOUIS_EXTENSION_POINT, "Could not locate application with -%s='%s'", DOMAIN_FLAG, domainPluginId);
			}
			Bundle domainPluginBundle = Platform.getBundle(domainPluginCE.getNamespace());

			// locate the domain plugin's spring-context-domain.xml context 
			// this is included in both client- and server-side contexts
			String domainPluginDomainFilePath = domainPluginCE.getAttribute(DOMAIN_FILE);
			URL domainPluginDomainUrl = domainPluginBundle.getEntry(domainPluginDomainFilePath);
			domainPluginDomainUrl = Platform.resolve(domainPluginDomainUrl);
			domainPluginDomainFilePath = new File(domainPluginDomainUrl.getFile()).getCanonicalPath();

			// locate the server-side context 
			Bundle serverPluginBundle = ServerPlugin.getDefault().getBundle();
			URL serverPluginUrl = serverPluginBundle.getEntry(SERVERSIDE_SPRINGCONTEXT_XML);
			serverPluginUrl = Platform.resolve(serverPluginUrl);
			String serverPluginFilePath = new File(serverPluginUrl.getFile()).getCanonicalPath();
			
			// locate the client-side base context
			// certain components in this context may be overridden.
			Bundle louisPluginBundle = LouisPlugin.getDefault().getBundle();
			URL louisPluginUrl = louisPluginBundle.getEntry(CLIENTSIDE_SPRINGCONTEXT_BASE_XML);
			louisPluginUrl = Platform.resolve(louisPluginUrl);
			String louisPluginFilePath = new File(louisPluginUrl.getFile()).getCanonicalPath();

			// locate the domain plugin's spring-context-louis.xml context (for client-side) 
			// this potentially overrides definitions in base configs.
			String domainPluginLouisFilePath = domainPluginCE.getAttribute(LOUIS_FILE);
			URL domainPluginLouisUrl = domainPluginBundle.getEntry(domainPluginLouisFilePath);
			domainPluginLouisUrl = Platform.resolve(domainPluginLouisUrl);
			domainPluginLouisFilePath = new File(domainPluginLouisUrl.getFile()).getCanonicalPath();

			if (runServer) {
				// create server-side context
				_serverContext = new FileSystemXmlApplicationContext(new String[]{serverPluginFilePath, domainPluginDomainFilePath});
				_server = getBeanFrom(_serverContext, BEAN_SERVER_ID, ERROR_NO_SERVER_BEAN, StandaloneServer.class);
				_serverDomainDefinition = getBeanFrom(_serverContext, BEAN_DOMAIN_ID, ERROR_NO_DOMAIN_BEAN, IDomainDefinition.class);

				// initialize obtained beans, start server
				_server.getDatabaseServer().init(store);
				_serverDomainDefinition.init(domainPluginBundle, _server.getBinding().getDomainRegistrar());
				_server.start();
			}

			// create client-side context
			_clientContext = 
				new FileSystemXmlApplicationContext(
					new String[]{louisPluginFilePath, domainPluginDomainFilePath, domainPluginLouisFilePath});
			_app = getBeanFrom(_clientContext, BEAN_APP_ID, ERROR_NO_APP_BEAN, IApplication.class);
			_louisDefinition = getBeanFrom(_clientContext, BEAN_LOUIS_ID, ERROR_NO_LOUIS_BEAN, ILouisDefinition.class);

			// initialize obtained beans, run client-side app
			_louisDefinition.init(domainPluginBundle, _app.getBinding().getDomainRegistrar());
			_app.init(_louisDefinition, store); // from whence derives SessionBinding
			
			return _app.run(args);
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
			if (_server != null && _server.isStarted()) {
				_server.shutdown();
			}
		}
	}

	private <T> T  getBeanFrom(FileSystemXmlApplicationContext context, String beanId, int errorNoBean, Class<T> requiredType) throws BeansException, CoreException {
		final Object bean = context.getBean(beanId);
		if (bean == null) {
			throwCoreException(errorNoBean, "Could not locate bean %s", beanId);
		}
		if (!(requiredType.isAssignableFrom(bean.getClass()))) {
			throwCoreException(ERROR_BEAN_WRONG_TYPE, "Application '%s' (from Spring context file) does not implement %s", beanId, requiredType.getSimpleName());
		}
		return (T)bean;
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
	private IConfigurationElement findLouisSpringContextConfigurationElement(String springContextId) {
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
