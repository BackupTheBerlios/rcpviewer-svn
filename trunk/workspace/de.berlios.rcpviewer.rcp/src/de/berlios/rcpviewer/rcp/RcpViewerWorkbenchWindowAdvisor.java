package de.berlios.rcpviewer.rcp;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class RcpViewerWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public RcpViewerWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new RcpViewerActionBarAdvisor(configurer);
    }
    
    @Override
    public void preWindowOpen() {
    	super.preWindowOpen();
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowMenuBar(true);
        configurer.setShowProgressIndicator(true);
    }

    @Override
    public void postWindowOpen() {
    	super.postWindowOpen();
    }
}
