package com.ibm.developerworks.google.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class LicenseKeyWizardPage extends WizardPage
{
    private Text licenseKeyText;

    protected LicenseKeyWizardPage(String pageName)
    {
        super(pageName);
        setTitle("License Key");
        setDescription("Define your Google API License Key");
    }

    public void createControl(Composite parent)
    {
        GridLayout pageLayout = new GridLayout();
        pageLayout.numColumns = 2;
        parent.setLayout(pageLayout);
        parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label label = new Label(parent, SWT.NONE);
        label.setText("License Key:");

        licenseKeyText = new Text(parent, SWT.BORDER);
        licenseKeyText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        setControl(parent);
    }

    public Text getLicenseKeyText()
    {
        return licenseKeyText;
    }

    public void setLicenseKeyText(Text licenseKeyText)
    {
        this.licenseKeyText = licenseKeyText;
    }
}