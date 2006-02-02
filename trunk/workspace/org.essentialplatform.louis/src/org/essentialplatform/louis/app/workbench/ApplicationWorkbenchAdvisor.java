package org.essentialplatform.louis.app.workbench;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.app.DefaultPerspective;
import org.essentialplatform.louis.jobs.ReportJob;


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
	public void eventLoopException(Throwable ex) {
		// TODO Auto-generated method stub
		super.eventLoopException(ex);
		CharArrayWriter exStackTrace = new CharArrayWriter();
		ex.printStackTrace(new PrintWriter(exStackTrace));
		new ReportJob(ex.getMessage(), null, ex).runInUIThread(null);
		MessageDialog.openError(
				null,
				LouisPlugin.getResourceString( 
						"ApplicationWorkbenchAdvisor.UncaughtError"), //$NON-NLS-1$
				ex.getMessage() );
	}	
}
