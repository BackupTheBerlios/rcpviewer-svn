package mikespike3.application;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

import de.berlios.rcpviewer.rcp.RcpViewerActionBarAdvisor;
import de.berlios.rcpviewer.rcp.RcpViewerWorkbenchWindowAdvisor;

public class EasyBeanWorkbenchWindowAdvisor extends RcpViewerWorkbenchWindowAdvisor {

    public EasyBeanWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new RcpViewerActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
    	super.preWindowOpen();
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setTitle("EasyBean Example");

    }
}
