package mikespike3.application;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import de.berlios.rcpviewer.rcp.RcpViewerWorkbenchAdvisor;


/**
 * Adds actions to a workbench window.
 */
public class EasyBeanWorkbenchAdvisor extends RcpViewerWorkbenchAdvisor {

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new EasyBeanWorkbenchWindowAdvisor(configurer);
    }
    
}
