package mikespike3.application;

import mikespike3.commands.IAuthenticationCommand;
import net.sf.acegisecurity.Authentication;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;


public class EasyBeanAuthenticationCommand
implements IAuthenticationCommand
{
	private LoginWizard _loginWizard;
	
	public EasyBeanAuthenticationCommand(LoginWizard pWizard) {
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
