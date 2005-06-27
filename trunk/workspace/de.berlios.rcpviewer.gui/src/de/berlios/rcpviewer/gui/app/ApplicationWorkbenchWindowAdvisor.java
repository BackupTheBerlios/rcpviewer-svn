package de.berlios.rcpviewer.gui.app;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import de.berlios.rcpviewer.gui.GuiPlugin;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public static final String TITLE_KEY = 
		"ApplicationWorkbenchWindowAdvisor.Title";
	
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(true);
        configurer.setTitle( GuiPlugin.getResourceString( TITLE_KEY ) );
    }


	/**
	 * Maximises the window.
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createWindowContents(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	public void createWindowContents(Shell shell) {
		super.createWindowContents(shell);
		shell.setMaximized(true);
	}
	
	
}
