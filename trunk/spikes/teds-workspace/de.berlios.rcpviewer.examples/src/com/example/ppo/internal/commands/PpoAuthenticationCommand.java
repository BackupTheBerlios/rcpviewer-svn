package com.example.ppo.internal.commands;

import net.sf.acegisecurity.Authentication;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;

import com.example.ppo.commands.AuthenticationCommand;
import com.example.ppo.internal.LoginWizard;


public class PpoAuthenticationCommand
implements AuthenticationCommand
{
	private LoginWizard _loginWizard;
	
	public PpoAuthenticationCommand(LoginWizard pWizard) {
		_loginWizard= pWizard;
	}
	
	public Authentication run() throws CoreException {
		
		//loginDialog.setTitle("Sign In - OFace PPO Example Application");
		if (new WizardDialog(null, _loginWizard).open() != Dialog.OK) {
			return null;
		}
		return _loginWizard.getAuthentication();
	}
}
