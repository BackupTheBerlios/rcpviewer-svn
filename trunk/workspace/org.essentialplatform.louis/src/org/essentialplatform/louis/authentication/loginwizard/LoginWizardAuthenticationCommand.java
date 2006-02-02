package org.essentialplatform.louis.authentication.loginwizard;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.AuthenticationManager;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.essentialplatform.louis.authentication.IAuthenticationCommand;


public class LoginWizardAuthenticationCommand
implements IAuthenticationCommand
{
	private LoginWizard _loginWizard;
	
	public LoginWizardAuthenticationCommand() {
		_loginWizard = new LoginWizard(this);
	}
	
	public Authentication run() throws CoreException {
		
		if (new WizardDialog(null, _loginWizard).open() != Dialog.OK) {
			return null;
		}
		
		return _loginWizard.getAuthentication();
	}
	


	///////////////////////////////////////////////////////////////
	// FormToolkit (injected)
	///////////////////////////////////////////////////////////////

	private FormToolkit _toolkit;
	/**
	 * Used when constructing pages.
	 * 
	 * @return
	 */
	public FormToolkit getToolkit() {
		return _toolkit;
	}
	/**
	 * Mandatory.
	 * 
	 * @param toolkit
	 */
	public void setToolkit(FormToolkit toolkit) {
		_toolkit = toolkit;
	}

	///////////////////////////////////////////////////////////////
	// AuthenticationManager (injected)
	///////////////////////////////////////////////////////////////

	private AuthenticationManager _authenticationManager;
	public AuthenticationManager getAuthenticationManager() {
		return _authenticationManager;
	}
	/**
	 * Mandatory.
	 * 
	 * @param authenticationManager
	 */
	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		_authenticationManager = authenticationManager;
	}

	
	///////////////////////////////////////////////////////////////
	// WindowText (injected)
	///////////////////////////////////////////////////////////////

	private String _windowText = "";
	public String getWindowText() {
		return _windowText;
	}

	public void setWindowText(String pWindowText) {
		_windowText = pWindowText;
	}

	///////////////////////////////////////////////////////////////
	// TitleText (injected)
	///////////////////////////////////////////////////////////////

	private String _titleText = "";

	public String getTitleText() {
		return _titleText;
	}

	public void setTitleText(String pTitleText) {
		_titleText = pTitleText;
	}


}
