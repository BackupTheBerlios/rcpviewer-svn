package mikespike3.application;

import mikespike3.EasyBeanExample;
import mikespike3.commands.AuthenticationCommand;
import mikespike3.commands.CreateAndRunWorkbenchCommand;
import mikespike3.model.EasyBean;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;

import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainRegistry;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.persistence.IObjectStore;
import de.berlios.rcpviewer.persistence.inmemory.InMemoryObjectStore;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionManager;
import de.berlios.rcpviewer.session.local.Session;


public class EasyBeanApplication
implements IPlatformRunnable
{
	private AuthenticationCommand _authenticationCommand;
	private CreateAndRunWorkbenchCommand _createAndRunWorkbenchCommand;
	
	public EasyBeanApplication(
			AuthenticationCommand pAuthenticationCommand, 
			CreateAndRunWorkbenchCommand pWorkbenchCommand
	) {
		_authenticationCommand= pAuthenticationCommand;
		_createAndRunWorkbenchCommand= pWorkbenchCommand;
	}
	
	public Object run(Object args) throws Exception {
		
		try {
			if (_authenticationCommand.run() == null)
				return null;  
			
			
			//FIXME In the future there will be a better way to set sessions
			ISessionManager sessionManager= RuntimePlugin.getDefault().getSessionManager();
			IDomainRegistry domainRegistry= RuntimePlugin.getDefault().getDomainRegistry();
			// REVIEW_CHANGES: domain names are not the same as plugin Ids.
			IDomain domain= domainRegistry.getDomain("default");
			ISession session = sessionManager.createSession(domain, new InMemoryObjectStore());

			int returnCode = _createAndRunWorkbenchCommand.run();
			
			if (returnCode == PlatformUI.RETURN_RESTART) 
				return IPlatformRunnable.EXIT_RESTART;
			
			return IPlatformRunnable.EXIT_OK;
		}
		finally {
			Platform.endSplash();
		}
	}

}
