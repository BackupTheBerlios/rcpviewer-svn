package org.essentialplatform.louis.app;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.essentialplatform.louis.LouisPlugin;


public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#initialize(org.eclipse.ui.application.IWorkbenchConfigurer)
	 */
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
    	if ( configurer == null ) throw new IllegalArgumentException();
    	configurer.setSaveAndRestore( true );
	}

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(org.eclipse.ui.application.IWorkbenchWindowConfigurer)
     */
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    	return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
	 */
	public String getInitialWindowPerspectiveId() {
		return DefaultPerspective.ID;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#eventLoopException(java.lang.Throwable)
	 */
	@Override
	public void eventLoopException(Throwable exception) {
		// TODO Auto-generated method stub
		super.eventLoopException(exception);
		MessageDialog.openError(
				null,
				LouisPlugin.getResourceString( 
						"ApplicationWorkbenchAdvisor.UncaughtError"), //$NON-NLS-1$
				exception.toString() );
	}	
}
