package org.essentialplatform.runtime.client.tests;

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
import org.essentialplatform.core.deployment.Binding;
import org.essentialplatform.core.deployment.IBinding;
import org.essentialplatform.louis.app.IApplication;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Bootstrap implements IPlatformRunnable {

	public Object run(Object args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
//	private static final String DOMAIN_FLAG = "-domain";
//	private static final String STORE_FLAG = "-store";
//	private static final String NO_SERVER_FLAG = "-noserver";



}
