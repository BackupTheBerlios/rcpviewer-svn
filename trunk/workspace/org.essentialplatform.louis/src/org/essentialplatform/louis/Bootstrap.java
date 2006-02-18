package org.essentialplatform.louis;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.essentialplatform.core.IBundlePeer;
import org.essentialplatform.core.springsupport.BundleResolvingXmlApplicationContext;
import org.essentialplatform.core.springsupport.ExtensionPointContributingBundleFactoryBean;
import org.essentialplatform.core.util.ExtensionPointContributionLocator;
import org.essentialplatform.louis.app.IApplication;
import org.essentialplatform.runtime.server.ServerPlugin;
import org.essentialplatform.runtime.server.StandaloneServer;
import org.essentialplatform.runtime.shared.IRuntimeBinding;
import org.essentialplatform.runtime.shared.RuntimePlugin;
import org.essentialplatform.runtime.shared.domain.SingleDomainRegistry;
import org.essentialplatform.runtime.shared.domain.SpringConfiguredDomainDefinition;
import org.osgi.framework.Bundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

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
	
	private static final int ERROR_BEAN_WRONG_TYPE = 190;


	private static final String BEAN_APP_ID = "app";
	/**
	 * Note the <tt>&</tt> prefix in order to obtain the {@link FactoryBean} 
	 * rather than its product.
	 */
	private static final String BEAN_DOMAIN_BUNDLE_FACTORY_ID = "&domainBundle";
	private static final String BEAN_DOMAIN_REGISTRY_ID = "domainRegistry";
	private static final int ERROR_NO_APP_BEAN = 200;
	private static final int ERROR_NO_DOMAIN_BUNDLE_FACTORY_BEAN = 210;
	private static final int ERROR_NO_DOMAIN_REGISTRY_BEAN = 220;
	
	private static final String BEAN_SERVER_ID = "server";
	private static final int ERROR_NO_SERVER_BEAN = 300;

	private static final String SPRINGCONTEXT_EXTENSION_POINT = "org.essentialplatform.louis.springcontext";
	private static final String LOUIS_FILE = "louis-file";
	private static final String DOMAIN_FILE = "domain-file";
	private static final String CLIENTSIDE_SPRINGCONTEXT_BASE_XML = "spring-context-base.xml";
	private static final String SERVERSIDE_SPRINGCONTEXT_XML = "spring-context.xml";

	private BundleResolvingXmlApplicationContext _clientContext;
	private IApplication _app;
	
	private BundleResolvingXmlApplicationContext _serverContext;
	private StandaloneServer _server;

	/**
	 * Creates a Spring context for both client- and server-side, fetches out
	 * the appropriate bean and boots up.
	 * 
	 * <p>
	 * The client-side Spring context is built out of three Spring config files:
	 * <ul>
	 * <li> The louis plugin provides a base context that wires up much of the
	 *      infrastructure.
	 * <li> The domain plugin must provides a "domain" context that provides the
	 *      <tt>'domain'</tt> bean, implementing {@link IDomainDefinition}.
	 * <li> The domain plugin must provide a "louis" context that optionally
	 *      provides the <tt>'louis'</tt> bean, implementing {@link ILouisDefinition}.  If
	 *      none is provided then a fallback definition of this bean in the 
	 *      first context (from the louis plugin
	 *      itself) is used.
	 * </ul>
	 * 
	 * <p>
	 * Both contexts are s in fact built (or <i>refresh</i>ed, in the 
	 * Spring parlance) twice.  The first refresh is solely to acquire the
	 * <tt>'domainBundleFactoryBean'</tt> that is responsible for returning the
	 * {@link Bundle} for the domain plugin.  Once acquired from the Spring context,
	 * its properties are programmatically injected with the information needed 
	 * (taken from the command line arguments) to actually be able to return
	 * the bundle.  The context is then built again, this time correctly
	 * initializing all beans in the context.
	 * 
	 * 
	 * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
	 */
	public Object run(Object args) throws Exception {

		Display display = Display.getDefault();
		try {
			String[] options= (String[])args;
			String domainPluginId = getDomainPluginId(options);
			String store = getStore(options);
			boolean runServer = !isNoServer(options);
	
			// locate the client-side base context
			IBundlePeer louisPlugin = LouisPlugin.getDefault();
			
			ExtensionPointContributionLocator domainPluginLocator = 
				new ExtensionPointContributionLocator(SPRINGCONTEXT_EXTENSION_POINT, domainPluginId);
			Bundle domainPluginBundle = domainPluginLocator.getBundle();
			IConfigurationElement domainPluginCE = domainPluginLocator.getConfigurationElement();

			ExtensionPointContributingBundleFactoryBean domainBundleFactory;

			// create client-side context
			// two pass process: first, build the context in order to get a handle
			// on the 'domainBundle' bean.  Then configure this bean.  Then 
			// refresh the context again.
			_clientContext = new BundleResolvingXmlApplicationContext()
									.resolveAgainst(domainPluginBundle)
									.resolveAgainst(louisPlugin.getBundle())
									.configLocated(CLIENTSIDE_SPRINGCONTEXT_BASE_XML)
									.configLocated(domainPluginCE.getAttribute(DOMAIN_FILE))
									.configLocated(domainPluginCE.getAttribute(LOUIS_FILE))
									.andRefresh();
			configureDomainBundleFactory(_clientContext, domainPluginId);
			_clientContext.refresh();
			
			_app = getBeanFrom(_clientContext, BEAN_APP_ID, ERROR_NO_APP_BEAN, IApplication.class);

			SingleDomainRegistry domainRegistry = 
				getBeanFrom(_clientContext, BEAN_DOMAIN_REGISTRY_ID, ERROR_NO_DOMAIN_REGISTRY_BEAN, SingleDomainRegistry.class);
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
				configureDomainBundleFactory(_serverContext, domainPluginId);
				_serverContext.refresh();
				
				_server = getBeanFrom(_serverContext, BEAN_SERVER_ID, ERROR_NO_SERVER_BEAN, StandaloneServer.class);

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

	private void configureDomainBundleFactory(ApplicationContext context, String domainPluginId) throws BeansException, CoreException {
		ExtensionPointContributingBundleFactoryBean domainBundleFactory;
		domainBundleFactory = getBeanFrom(context, BEAN_DOMAIN_BUNDLE_FACTORY_ID, ERROR_NO_DOMAIN_BUNDLE_FACTORY_BEAN, ExtensionPointContributingBundleFactoryBean.class);
		domainBundleFactory.setConfigurationElementId(SPRINGCONTEXT_EXTENSION_POINT);
		domainBundleFactory.setId(domainPluginId);
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
	
//	/**
//	 * Searches through all &lt;springcontext> extensions under the
//	 * &lt;springcontext> extension point and returns the <tt>ConfigurationElement</tt>
//	 * matching the supplied Id.
//	 * 
//	 * <p>
//	 * The returned <tt>ConfigurationElement</tt> (representing the supplied
//	 * <tt>&lt;springcontext/></tt> element according to the extension point's 
//	 * schema) has additional info, in particular the name of the Spring config 
//	 * file for the contributing plugin.
//	 * 
//	 * <p>
//	 * @return the ConfigurationElement or <tt>null</tt> if none could be found.
//	 */
//	private IConfigurationElement findLouisSpringContextConfigurationElement(String springContextId) {
//		IExtensionPoint extensionPoint = 
//			Platform.getExtensionRegistry().getExtensionPoint(SPRINGCONTEXT_EXTENSION_POINT);
//		for(IConfigurationElement element: extensionPoint.getConfigurationElements()) {
//			String id = element.getAttribute("id");
//			if (springContextId.equals(id)) {
//				return element;
//			}
//		}
//		return null; 
//	}



}
