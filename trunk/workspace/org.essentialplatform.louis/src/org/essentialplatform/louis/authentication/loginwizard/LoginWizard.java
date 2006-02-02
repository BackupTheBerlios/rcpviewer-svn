package org.essentialplatform.louis.authentication.loginwizard;

import net.sf.acegisecurity.Authentication;
import net.sf.acegisecurity.AuthenticationException;
import net.sf.acegisecurity.AuthenticationManager;
import net.sf.acegisecurity.providers.UsernamePasswordAuthenticationToken;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class LoginWizard extends Wizard {
	
	/**
	 * Owning authentication command.
	 */
	private LoginWizardAuthenticationCommand _command;
	
	
	public LoginWizard(LoginWizardAuthenticationCommand command) {
		_command = command;
		setNeedsProgressMonitor(false);
		setForcePreviousAndNextButtons(false);
		
		setWindowTitle(command.getWindowText());
	}

	/**
	 * Validates the credentials from the page using the supplied
	 * {@link #getAuthenticationManager()} and populates the {@link Authentication}
	 * (such that it can be read by {@link #getAuthentication()}.
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		final boolean[] authenticatedRef = new boolean[] { false }; // pass by ref
        BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
            public void run() {
                try {            
					String username= _page.getUsername();
					String password= _page.getPassword();
					Authentication token= new UsernamePasswordAuthenticationToken(username, password);
					_authentication = _command.getAuthenticationManager().authenticate(token);
					authenticatedRef[0] = _authentication.isAuthenticated();

                } catch (AuthenticationException e) {
					_page.setErrorMessage(e.getMessage()); // TODO: this trashes the display somewhat.
					getContainer().updateTitleBar();
                } 
            }
        });
        boolean authenticated = authenticatedRef[0];
        if (!authenticated) {
        	_page.setErrorMessage("Username or password incorrect.  Please retry.");
        }
        return authenticated;
	}

	
	private LoginPage _page; 
	public void addPages() {
		_page= new LoginPage(_command.getToolkit());
		_page.setTitle(_command.getTitleText());
		addPage(_page);
	}



	///////////////////////////////////////////////////////////////
	// Authentication
	// populated when wizard finishes
	///////////////////////////////////////////////////////////////


	private Authentication _authentication;
	/**
	 * The {@link Authentication} resultant from this login.
	 * 
	 * <p>
	 * Only populated after the wizard has finished at least once (successfully
	 * or not).
	 * 
	 * @return
	 */
	public Authentication getAuthentication() {
		return _authentication;
	}


	
}
