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
import org.essentialplatform.core.IBundlePeer;
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.deployment.IBinding;
import org.essentialplatform.louis.app.IApplication;
import org.essentialplatform.louis.app.springsupport.BundleResolvingResourceLoader;
import org.essentialplatform.louis.app.springsupport.BundleResolvingXmlApplicationContext;
import org.essentialplatform.louis.domain.ILouisDefinition;
import org.essentialplatform.runtime.server.ServerPlugin;
import org.essentialplatform.runtime.server.StandaloneServer;
import org.essentialplatform.runtime.server.domain.bindings.RuntimeServerBinding;
import org.essentialplatform.runtime.shared.IRuntimeBinding;
import org.essentialplatform.runtime.shared.RuntimePlugin;
import org.essentialplatform.runtime.shared.domain.IDomainDefinition;
import org.essentialplatform.runtime.shared.domain.SingleDomainRegistry;
import org.osgi.framework.Bundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;

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

	private static final String BEAN_SERVER_ID = "server";
	private static final String BEAN_SERVER_DOMAIN_ID = "domain";
	private static final String BEAN_SERVER_BINDING_ID = "serverBinding";
	private static final int ERROR_NO_SERVER_BEAN = 200;
	private static final int ERROR_NO_SERVER_DOMAIN_BEAN = 210;
	private static final int ERROR_NO_SERVER_BINDING_BEAN = 220;
	
	private static final String BEAN_APP_ID = "app";
	private static final String BEAN_LOUIS_ID = "louis";
	private static final String BEAN_CLIENT_BINDING_ID = "clientBinding";
	private static final int ERROR_NO_APP_BEAN = 300;
	private static final int ERROR_NO_LOUIS_BEAN = 310;
	private static final int ERROR_NO_CLIENT_BINDING_BEAN = 320;
	

	private static final String SPRINGCONTEXT_EXTENSION_POINT = "org.essentialplatform.louis.springcontext";
	private static final String LOUIS_FILE = "louis-file";
	private static final String DOMAIN_FILE = "domain-file";
	private static final String CLIENTSIDE_SPRINGCONTEXT_BASE_XML = "spring-context-base.xml";
	private static final String SERVERSIDE_SPRINGCONTEXT_XML = "spring-context.xml";

	private ApplicationContext _clientContext;
	//private StaticApplicationContext _clientContext;
	private ILouisDefinition _louisDefinition;
	private IApplication _app;
	private IRuntimeBinding _clientBinding;
	
	private ApplicationContext _serverContext;
	private IDomainDefinition _serverDomainDefinition;
	private StandaloneServer _server;
	private IRuntimeBinding _serverBinding;

	/*
	 * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
	 */
	public Object run(Object args) throws Exception {

		Display display = Display.getDefault();
		try {
			String[] options= (String[])args;
			String domainPluginId = getDomainPluginId(options);
			String store = getStore(options);
			boolean runServer = !isNoServer(options);
	
			// locate the Extension
			final IConfigurationElement domainPluginCE = 
				findLouisSpringContextConfigurationElement(domainPluginId);
			if (domainPluginCE == null) {
				throwCoreException( 
					ERROR_NO_SPRINGCONTEXT_LOUIS_EXTENSION_POINT, "Could not locate application with -%s='%s'", DOMAIN_FLAG, domainPluginId);
			}

			// locate the client-side base context
			// certain components in this context may be overridden.
			IBundlePeer louisPlugin = LouisPlugin.getDefault();
			
			// locate the domain plugin's spring-context-domain.xml context 
			// this is included in both client- and server-side contexts
			Bundle domainPluginBundle = Platform.getBundle(domainPluginCE.getNamespace());
			
			// create client-side context
			// (must be done before the server-side context creation, can't remember why though...)
			_clientContext = new BundleResolvingXmlApplicationContext()
									.resolveAgainst(domainPluginBundle)
									.resolveAgainst(louisPlugin.getBundle())
									.configLocated(CLIENTSIDE_SPRINGCONTEXT_BASE_XML)
									.configLocated(domainPluginCE.getAttribute(DOMAIN_FILE))
									.configLocated(domainPluginCE.getAttribute(LOUIS_FILE))
									.andRefresh();
			_app = getBeanFrom(_clientContext, BEAN_APP_ID, ERROR_NO_APP_BEAN, IApplication.class);
			_louisDefinition = getBeanFrom(_clientContext, BEAN_LOUIS_ID, ERROR_NO_LOUIS_BEAN, ILouisDefinition.class);
			_clientBinding = getBeanFrom(_clientContext, BEAN_CLIENT_BINDING_ID, ERROR_NO_CLIENT_BINDING_BEAN, IRuntimeBinding.class);

			// initialize obtained beans, run client-side app
			_clientBinding.init(louisPlugin);
			_louisDefinition.init(domainPluginBundle);
			
			// domain initialisation (must use domain from client since need ILouisDomain adapters)
			Binding.setBinding(_clientBinding); // needed, unfortunately.

			SingleDomainRegistry domainRegistry = 
				new SingleDomainRegistry(_louisDefinition);
			RuntimePlugin.getDefault().setDomainRegistry(domainRegistry);
			domainRegistry.registerClassesInDomains();

			// locate the server-side context 
			IBundlePeer serverPlugin = ServerPlugin.getDefault();

			if (runServer) {
				// create server-side context
				_serverContext = new BundleResolvingXmlApplicationContext()
										.resolveAgainst(domainPluginBundle)
										.resolveAgainst(serverPlugin.getBundle())
										.configLocated(SERVERSIDE_SPRINGCONTEXT_XML)
										.configLocated(domainPluginCE.getAttribute(DOMAIN_FILE))
										.andRefresh();
				_server = getBeanFrom(_serverContext, BEAN_SERVER_ID, ERROR_NO_SERVER_BEAN, StandaloneServer.class);
				_serverDomainDefinition = getBeanFrom(_serverContext, BEAN_SERVER_DOMAIN_ID, ERROR_NO_SERVER_DOMAIN_BEAN, IDomainDefinition.class);
				_serverBinding = getBeanFrom(_serverContext, BEAN_SERVER_BINDING_ID, ERROR_NO_SERVER_BINDING_BEAN, IRuntimeBinding.class);

				// initialize obtained beans, start server
				_serverBinding.init(louisPlugin);
				_serverDomainDefinition.init(domainPluginBundle); // for classloading
				
				// TODO: this is a hack.  Need to decide whether we hard-code the store names within the
				// Spring file, in which case here we should be validating that there IS a serverSessionFactory
				// for the (domainName, objectStoreName) ... or we specify it here instead.
				_server.getServerSessionFactories().get(0).getDatabaseServer().init(store);

				_server.start();
			}

			// TODO: like the server booting, this too is a hack.  We
			// currently only support single SessionBinding.
			_app.init(store);
			
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

	private String getDomainPluginId(String[] options) throws CoreException {
		for (int i = options.length - 1; 0 < i--;) {
			if (options[i].equals(DOMAIN_FLAG)) {
				return options[i+1];
			}
		}
		throwCoreException(ERROR_NO_DOMAIN_FLAG, "No domain plugin specified.  Use the -%s <domainPluginId> to specify the plugin to search for a Spring config file.", DOMAIN_FLAG);
		return null; // never reached; previous will throw
	}
	private String getStore(String[] options) throws CoreException {
		for (int i = options.length - 1; 0 < i--;) {
			if (options[i].equals(STORE_FLAG)) {
				return options[i+1];
			}
		}
		throwCoreException(ERROR_NO_STORE_FLAG, "No store specified.  Use the -%s <domainPluginId> to specify the name of the object store.", STORE_FLAG);
		return null; // never reached; previous will throw
	}
	private boolean isNoServer(String[] options) {
		for (int i = options.length - 1; 0 < i--;) {
			if (options[i].equals(NO_SERVER_FLAG)) {
				return true;
			}
		}
		return false;
	}

	private <T> T  getBeanFrom(ApplicationContext context, String beanId, int errorNoBean, Class<T> requiredType) throws BeansException, CoreException {
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
