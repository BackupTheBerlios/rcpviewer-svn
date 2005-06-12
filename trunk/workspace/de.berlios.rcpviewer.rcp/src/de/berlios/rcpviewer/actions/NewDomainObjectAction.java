package de.berlios.rcpviewer.actions;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import de.berlios.rcpviewer.RCPViewer;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.editors.RcpViewerEditorInput;
import de.berlios.rcpviewer.gui.DefaultEditorContentBuilder;
import de.berlios.rcpviewer.rcp.RcpViewerPlugin;
import de.berlios.rcpviewer.session.IDomainObject;
import de.berlios.rcpviewer.session.ISession;
import de.berlios.rcpviewer.session.ISessionManager;

public class NewDomainObjectAction extends Action {
	
	private IRuntimeDomainClass _domainClass;
	private IWorkbenchWindow _workbenchWindow;
	
	public NewDomainObjectAction(IWorkbenchWindow pWorkbenchWindow, IRuntimeDomainClass pDomainClass) {
		_domainClass= pDomainClass;
		_workbenchWindow= pWorkbenchWindow;
		setText(_domainClass.getName());
		setToolTipText(_domainClass.getDescription());
	}

	@Override
	public void run() {

		try {
			ISessionManager sessionManager= RuntimePlugin.getDefault().getSessionManager();
			ISession session= sessionManager.get(sessionManager.getCurrentSession());
			IDomainObject domainObject= session.createTransient(_domainClass);

			// open it in a new editor
			IWorkbenchPage page = _workbenchWindow.getActivePage();
			RcpViewerEditorInput input= 
				new RcpViewerEditorInput(domainObject, new DefaultEditorContentBuilder());
			page.openEditor(input, RCPViewer.OBJECT_EDITOR_ID);

		}
		catch (Exception exception) {
			String msg= "Error creating object"; 
			Status status= new Status(Status.ERROR, "de.berlios.rcpviewer.rcp", 0, msg, exception);
			RcpViewerPlugin.getDefault().getLog().log(status);
			MessageDialog.openError(
					_workbenchWindow.getShell(), 
					msg, 
					exception.getMessage());
		}
	}
	
	
	
}
