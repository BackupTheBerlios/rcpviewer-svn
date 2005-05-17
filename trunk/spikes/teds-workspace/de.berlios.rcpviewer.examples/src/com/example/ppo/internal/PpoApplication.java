package com.example.ppo.internal;

import net.sf.plugins.springframework.SpringApplicationLauncher;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;

import com.example.ppo.commands.AuthenticationCommand;
import com.example.ppo.commands.CreateAndRunWorkbenchCommand;

public class PpoApplication
extends SpringApplicationLauncher
implements IPlatformRunnable
{
	private AuthenticationCommand _authenticationCommand;
	private CreateAndRunWorkbenchCommand _createAndRunWorkbenchCommand;
	
	public PpoApplication(
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
