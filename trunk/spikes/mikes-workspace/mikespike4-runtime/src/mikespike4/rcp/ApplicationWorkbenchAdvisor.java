package mikespike4.rcp;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.progress.UIJob;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return DefaultPerspective.ID;
	}

	@Override
	public void postStartup() {
		super.postStartup();
		UIJob job = new UIJob("Test"){
			public IStatus runInUIThread(IProgressMonitor monitor) {
				return Status.OK_STATUS;
			}
		} ;
		job.schedule();
	}
	
	
}
