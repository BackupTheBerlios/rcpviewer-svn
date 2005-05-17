package com.example.ppo.internal;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.AuthenticationException;
import net.sf.acegisecurity.AuthenticationManager;
import net.sf.acegisecurity.providers.UsernamePasswordAuthenticationToken;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class LoginWizard extends Wizard {
	
	LoginPage _page; 
	AuthenticationManager _authenticationManager;
	Authentication _authentication;
	
	FormToolkit _toolkit;
	String _windowText= "";
	String _titleText= "";
	
	public LoginWizard(FormToolkit pToolkit, AuthenticationManager pAuthenticationManager) {
		_toolkit= pToolkit;
		_authenticationManager= pAuthenticationManager;
		setNeedsProgressMonitor(false);
		setForcePreviousAndNextButtons(false);
	}
	
	public void addPages() {
		_page= new LoginPage(_toolkit);
		_page.setTitle(getTitleText());
		addPage(_page);
	}

	public boolean performFinish() {
		final boolean[] result= new boolean[] { false };
        BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
            public void run() {
				
                try {            
					String username= _page.getUsername();
					String password= _page.getPassword();
					Authentication token= new UsernamePasswordAuthenticationToken(username, password);
					_authentication= _authenticationManager.authenticate(token);
					result[0]= true;

                } catch (AuthenticationException e) {
					_page.setErrorMessage(e.getMessage());
					getContainer().updateTitleBar();
                } 
            }
        });
		return result[0];
	}

	public Authentication getAuthentication() {
		return _authentication;
	}

	public String getWindowText() {
		return _windowText;
	}

	public void setWindowText(String pWindowText) {
		_windowText = pWindowText;
		setWindowTitle(_windowText);
	}

	public String getTitleText() {
		return _titleText;
	}

	public void setTitleText(String pTitleText) {
		_titleText = pTitleText;
	}

}
