package mikespike3.application;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class LoginPage extends WizardPage {

	
	private FormToolkit _formToolkit;
	private Text _username;
	private Text _password;

	
	public LoginPage(FormToolkit pToolkit)
    {
		super("signin");
		_formToolkit= pToolkit;
	}

	public void createControl(Composite parent)
    {
		Composite composite = _formToolkit.createComposite(parent);
		initializeDialogUnits(composite);
        int horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		GridLayout orderLayout = new GridLayout(2,false);
        orderLayout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        orderLayout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        orderLayout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        orderLayout.horizontalSpacing = horizontalSpacing;
		composite.setLayout(orderLayout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        
		//Username
		Label userLabel = _formToolkit.createLabel(composite, "Username:");
        userLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		_username = _formToolkit.createText(composite, "", SWT.BORDER);
		GridData gridData= new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint= horizontalSpacing * 16;
        _username.setLayoutData(gridData);

		// Password
		Label passLabel = _formToolkit.createLabel(composite, "Password: ");
        passLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		_password = _formToolkit.createText(composite, "", SWT.BORDER);
		_password.setEchoChar('*');
		gridData= new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint= horizontalSpacing * 16;
        _password.setLayoutData(gridData);
		

		
		_username.setFocus();
		
		setControl(composite);
	}
	

	public FormToolkit getFormToolkit() {
		return _formToolkit;
	}

	public void setFormToolkit(FormToolkit pFormToolkit) {
		_formToolkit = pFormToolkit;
	}

	public String getPassword() {
		return _password.getText();
	}

	public String getUsername() {
		return _username.getText();
	}


	


}
