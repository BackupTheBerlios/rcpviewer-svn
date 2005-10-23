package de.berlios.rcpviewer.actions;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionDelegate;

import de.berlios.rcpviewer.RCPViewer;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.editors.Editor;
import de.berlios.rcpviewer.editors.RcpViewerEditorInput;
import de.berlios.rcpviewer.gui.DefaultEditorContentBuilder;
import de.berlios.rcpviewer.rcp.RcpViewerPlugin;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionManager;

public class OpenDomainObjectActionDelegate 
extends ActionDelegate 
{
	
	private IDomainObject _domainObject;
	private IWorkbenchWindow _workbenchWindow;
	
	public OpenDomainObjectActionDelegate(IWorkbenchWindow pWorkbenchWindow, IDomainObject pDomainObject) {
		_domainObject= pDomainObject;
		_workbenchWindow= pWorkbenchWindow;
	}

	@Override
	public void run(org.eclipse.jface.action.IAction action) {
		run();
	};
	public void run() {

		try {
			
			// look for existing editor
			IEditorReference[] editorReferences= _workbenchWindow.getActivePage().getEditorReferences();
			for (int i = 0; i < editorReferences.length; i++) {
				IEditorReference reference = editorReferences[i];
				IEditorPart editorPart= reference.getEditor(false);
				if (editorPart == null)
					continue;
				if ((editorPart instanceof Editor) == false)
					continue;
				Editor editor= (Editor)editorPart;
				RcpViewerEditorInput input= (RcpViewerEditorInput)editor.getEditorInput();
				IDomainObject domainObject= input.getDomainObject();
				if (_domainObject.equals(domainObject)) {
					_workbenchWindow.getActivePage().activate(editorPart);
					return;
				}
			}

			// open it in a new editor
			IWorkbenchPage page = _workbenchWindow.getActivePage();
			RcpViewerEditorInput input= 
				new RcpViewerEditorInput(_domainObject, new DefaultEditorContentBuilder());
			page.openEditor(input, RCPViewer.OBJECT_EDITOR_ID);

		}
		catch (Exception exception) {
			String msg= "Error opening object"; 
			Status status= new Status(Status.ERROR, "de.berlios.rcpviewer.rcp", 0, msg, exception);
			RcpViewerPlugin.getDefault().getLog().log(status);
			MessageDialog.openError(
					_workbenchWindow.getShell(), 
					msg, 
					exception.getMessage());
		}
	}
	
	
	
}
