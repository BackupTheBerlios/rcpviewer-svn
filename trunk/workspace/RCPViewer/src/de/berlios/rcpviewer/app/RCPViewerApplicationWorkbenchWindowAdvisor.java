package de.berlios.rcpviewer.app;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.nakedobjects.object.ApplicationContext;

/**
 * Auto-generated then simplified
 * @author Mike
 */
class RCPViewerApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	
	private static final String BACKUP_TITLE = "Naked Object RCP Application";

    RCPViewerApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new RCPViewerApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(400, 300));
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
		Object obj = ExplorationPlugin.getDefault().getRootObject().getObject();
		if ((obj instanceof ApplicationContext) == false)
			throw new RuntimeException("Root object must be an instance of ApplicationContext");
		String title = ((ApplicationContext)obj).name();
		if ( title == null ) title = BACKUP_TITLE;
        configurer.setTitle( title ) ;
    }
}
