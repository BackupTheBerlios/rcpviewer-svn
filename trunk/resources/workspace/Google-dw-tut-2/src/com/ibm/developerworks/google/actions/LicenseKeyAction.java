package com.ibm.developerworks.google.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.ibm.developerworks.google.views.SearchView;
import com.ibm.developerworks.google.wizards.LicenseKeyWizard;

public class LicenseKeyAction implements IViewActionDelegate
{
    private SearchView searchView;

    public void init(IViewPart view)
    {
        this.searchView = (SearchView) view;
    }

    public void run(IAction action)
    {
        LicenseKeyWizard wizard = new LicenseKeyWizard();
        WizardDialog dialog = new WizardDialog(searchView.getViewSite()
                .getShell(), wizard);
        dialog.open();

    }

    public void selectionChanged(IAction action, ISelection selection)
    {

    }

}