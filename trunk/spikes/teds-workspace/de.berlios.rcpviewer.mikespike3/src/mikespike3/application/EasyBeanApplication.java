package mikespike3.application;

import mikespike3.commands.IAuthenticationCommand;
import mikespike3.commands.ICreateAndRunWorkbenchCommand;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;



public class EasyBeanApplication
implements IPlatformRunnable
{
	private IAuthenticationCommand _authenticationCommand;
	private ICreateAndRunWorkbenchCommand _createAndRunWorkbenchCommand;
	
	public EasyBeanApplication(
			IAuthenticationCommand pAuthenticationCommand, 
			ICreateAndRunWorkbenchCommand pWorkbenchCommand
	) {
		_authenticationCommand= pAuthenticationCommand;
		_createAndRunWorkbenchCommand= pWorkbenchCommand;
	}
	
	public Object run(Object args) throws Exception {
		
		try {
			if (_authenticationCommand.run() == null)
				return null;  
			
			
			//FIXME In the future there will be a better way to set sessions
			IClientSessionManager sessionManager= RuntimePlugin.getDefault().getSessionManager();
			IDomainRegistry domainRegistry= RuntimePlugin.getDefault().getDomainRegistry();
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
