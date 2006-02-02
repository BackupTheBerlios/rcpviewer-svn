package org.essentialplatform.louis.authentication.loginwizard;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

class LoginPage extends WizardPage {

	
	private static final String PAGE_NAME_LOGIN = "signin";
	

	
	public LoginPage(FormToolkit pToolkit)
    {
		super(PAGE_NAME_LOGIN);
		_formToolkit= pToolkit;
	}

	///////////////////////////////////////////////////////////////
	// createControl
	///////////////////////////////////////////////////////////////

	/**
	 * factored out since used by both composite and the fields within 
	 * the composite.
	 */
	private int _horizontalSpacing;

	public void createControl(Composite parent) {
        _horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		Composite composite = createComposite(parent);
        
		createUserNameField(composite);
		createPasswordField(composite);
		
		_username.setFocus();
		setControl(composite);
	}
	
	private Composite createComposite(Composite parent) {
		Composite composite = _formToolkit.createComposite(parent);
		initializeDialogUnits(composite);
		GridLayout orderLayout = new GridLayout(2,false);
        orderLayout.horizontalSpacing = _horizontalSpacing;
        orderLayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        orderLayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        orderLayout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		composite.setLayout(orderLayout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        return composite;
	}

	///////////////////////////////////////////////////////////////
	// FormToolkit
	///////////////////////////////////////////////////////////////

	private FormToolkit _formToolkit;
	public FormToolkit getFormToolkit() {
		return _formToolkit;
	}

	///////////////////////////////////////////////////////////////
	// UserName
	///////////////////////////////////////////////////////////////

	private Text _username;
	public String getUsername() {
		return _username.getText();
	}

	private void createUserNameField(Composite composite) {
		Label userLabel = _formToolkit.createLabel(composite, "Username:");
        userLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		_username = _formToolkit.createText(composite, "", SWT.BORDER);
		GridData gridData= new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint= _horizontalSpacing * 16;
        _username.setLayoutData(gridData);
	}


	///////////////////////////////////////////////////////////////
	// Password
	///////////////////////////////////////////////////////////////

	private Text _password;
	public String getPassword() {
		return _password.getText();
	}
	private void createPasswordField(Composite composite) {
		GridData gridData;
		Label passLabel = _formToolkit.createLabel(composite, "Password: ");
        passLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		_password = _formToolkit.createText(composite, "", SWT.BORDER);
		_password.setEchoChar('*');
		gridData= new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint= _horizontalSpacing * 16;
        _password.setLayoutData(gridData);
	}


}
