/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.example.ppo.internal;


import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import com.example.ppo.PpoExample;
import com.example.ppo.PpoFactory;
import com.example.ppo.PpoPackage;

import de.berlios.rcpviewer.RCPViewer;
import de.berlios.rcpviewer.presentation.RCPViewerEditorInput;


public class PurchaseOrderNewWizard extends Wizard implements INewWizard {

	protected PpoPackage ppoPackage = PpoPackage.eINSTANCE;
	protected PpoFactory ppoFactory = ppoPackage.getPpoFactory();
	protected IWorkbench workbench;

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		PpoPlugin ppoPlugin= PpoPlugin.getInstance();
		setWindowTitle(PpoExample.getSafeResourceString("New Purchase Order"));
		setDefaultPageImageDescriptor(PpoExample.getImageDescriptor("newPurchaseOrder"));
	}

	protected EObject createInitialModel() {
		EClass eClass = (EClass)ppoPackage.getEClassifier("PurchaseOrder");
		EObject rootObject = ppoFactory.create(eClass);
		return rootObject;
	}
	
	@Override
	public boolean canFinish() {
		return true;
	}
	
	
	
	public boolean performFinish() {
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = workbenchWindow.getActivePage();

		try {
			// create a new object
			EObject rootObject = createInitialModel();


			// open it in a new editor
			page.openEditor(new RCPViewerEditorInput(rootObject), RCPViewer.OBJECT_EDITOR_ID);

			return true;
		}
		catch (Exception exception) {
			String msg= "Error creating purchase order"; 
			Status status= new Status(Status.ERROR, PpoExample.PLUGIN_ID, 0, msg, exception);
			PpoPlugin.getInstance().getLog().log(status);
			MessageDialog.openError(
					workbenchWindow.getShell(), 
					PpoExample.getSafeResourceString("Error creating purchase order"), 
					exception.getMessage());
			return false;
		}
	}


}
