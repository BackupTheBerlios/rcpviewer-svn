
package com.ibm.developerworks.google.wizards;

import org.eclipse.jface.wizard.Wizard;


public class LicenseKeyWizard extends Wizard
{
    private static String licenseKey;
    private LicenseKeyWizardPage page;
    
    public LicenseKeyWizard()
    {
        super();
        this.setWindowTitle("License Key");
    }

    public void addPages()
    {
        page = new LicenseKeyWizardPage("licenseKey");
        addPage(page);
    }
    public boolean performFinish()
    {
        if(page.getLicenseKeyText().getText().equalsIgnoreCase(""))
        {
            page.setErrorMessage("You must provide a license key.");
            page.setPageComplete(false);
            return false;
        }
        else
        {
            licenseKey = page.getLicenseKeyText().getText();
            return true;
        }
        
    }

    public static String getLicenseKey()
    {
        return licenseKey;
    }

    public static void setLicenseKey(String licenseKey)
    {
        LicenseKeyWizard.licenseKey = licenseKey;
    }
}
