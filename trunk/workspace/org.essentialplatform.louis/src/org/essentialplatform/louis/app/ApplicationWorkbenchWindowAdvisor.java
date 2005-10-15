package org.essentialplatform.louis.app;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.util.PlatformUtil;


/**
 * @author Mike
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public static final String TITLE_KEY = 
		"ApplicationWorkbenchWindowAdvisor.Title"; //$NON-NLS-1$
	
    /**
     * @param configurer
     */
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
     */
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
     */
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowCoolBar( true );
        configurer.setShowStatusLine( true );
        configurer.setTitle( LouisPlugin.getResourceString( TITLE_KEY ) );
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowShellClose()
	 */
	@Override
	public boolean preWindowShellClose() {
		// ensures all editors are closed on exit - this way no restore is
		// attempted on them.
		PlatformUtil.getActivePage().closeAllEditors( true );
		return true;
	}
    
    


}
