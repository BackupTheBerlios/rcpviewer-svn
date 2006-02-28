package org.essentialplatform.louis.app.workbench;

import java.util.Map;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.jobs.JobAction;
import org.essentialplatform.louis.jobs.NewDomainObjectJob;
import org.essentialplatform.louis.jobs.ReportJob;
import org.essentialplatform.runtime.shared.RuntimePlugin;
import org.essentialplatform.runtime.shared.domain.adapters.IDomainRegistry;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	
	private IWorkbenchAction save;

	private IWorkbenchAction refresh;

	private IWorkbenchAction close;

	private IWorkbenchAction closeAll;

	private IWorkbenchAction saveAll;

	private IWorkbenchAction quit;

	private IWorkbenchAction preferences;

	private IContributionItem viewMenuEntries;

    /**
     * @param configurer
     */
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.IWorkbenchWindow)
     */
    protected void makeActions(IWorkbenchWindow window) {
	   	save = ActionFactory.SAVE.create(window);
	   	register(save);
	   	
	   	final String pluginID = LouisPlugin.getDefault().getBundle().getSymbolicName();
	   	refresh = ActionFactory.REFRESH.create(window);
	   	refresh.setImageDescriptor(
	   			AbstractUIPlugin.imageDescriptorFromPlugin(pluginID, 
	   					"/icons/refresh.png" ) ) ; //$NON-NLS-1$
	   	refresh.setDisabledImageDescriptor( 
	   			AbstractUIPlugin.imageDescriptorFromPlugin(pluginID,
	   					"/icons/refresh_disabled.png" ) ) ; //$NON-NLS-1$
	   	register(refresh);
	   	
	   	close = ActionFactory.CLOSE.create(window);
	   	register(close);
	   	
	   	closeAll = ActionFactory.CLOSE_ALL.create(window);
	   	register(closeAll);
	   	
	   	saveAll = ActionFactory.SAVE_ALL.create(window);
	   	register(saveAll);
	   	
	   	quit = ActionFactory.QUIT.create(window);
	   	register(quit);
	   	
	   	preferences = ActionFactory.PREFERENCES.create(window);
	   	register(preferences);
	   	
	   	viewMenuEntries = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface.action.IMenuManager)
     */
    protected void fillMenuBar(IMenuManager menuBar) {
		 menuBar.add( createFileMenu() );
		 menuBar.add( createWindowMenu() );
    }
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface.action.ICoolBarManager)
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
        ToolBarManager toolBarManager
        	= new ToolBarManager(SWT.HORIZONTAL | SWT.FLAT);
        toolBarManager.add(save);
        toolBarManager.add(refresh);        
	    coolBar.add(toolBarManager);
	}
	

	/**
	 * Does nothing but record status line manager reference
	 * @see org.eclipse.ui.application.ActionBarAdvisor#fillStatusLine(org.eclipse.jface.action.IStatusLineManager)
	 */
	@Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		super.fillStatusLine(statusLine);
		ReportJob.setStatusLineManager( statusLine );
	}

	/**
	 * As it says.
	 */
    private MenuManager createFileMenu() {
        MenuManager menu = new MenuManager(
				LouisPlugin.getResourceString( "ApplicationActionBarAdvisor.File" ),  //$NON-NLS-1$
				IWorkbenchActionConstants.M_FILE);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));

		MenuManager newmenu = new MenuManager(
				LouisPlugin.getResourceString( "ApplicationActionBarAdvisor.New" ), //$NON-NLS-1$
				ActionFactory.NEW.getId());
	    menu.add(newmenu);
	    createClassItems( newmenu );
        menu.add(new Separator());
        menu.add(close);
        menu.add(closeAll);
        menu.add(new Separator());
        menu.add(save);
        menu.add(saveAll);
        menu.add(quit);
        return menu;
    }
    
	/**
	 * As it says.
	 */
    private MenuManager createWindowMenu() {
        MenuManager menu = new MenuManager(
				LouisPlugin.getResourceString( "ApplicationActionBarAdvisor.Window" ),  //$NON-NLS-1$
				IWorkbenchActionConstants.M_WINDOW );
        MenuManager viewMenu = new MenuManager(
				LouisPlugin.getResourceString( "ApplicationActionBarAdvisor.ShowView" ),  //$NON-NLS-1$
				"ApplicationActionBarAdvisor.ShowView" ); //$NON-NLS-1$
        viewMenu.add(viewMenuEntries);
        menu.add( viewMenu );
        menu.add( new Separator() );
        menu.add(preferences);
        return menu;
    }

    /**
     * Adds a 'new' menu item for every class in every domain
     */
    private void createClassItems(MenuManager newMenu) {
		assert newMenu != null;

		// get all classes from domain(s)
    	IDomainRegistry domainRegistry= RuntimePlugin.getDomainRegistry();
    	Map<String, IDomain> domains= domainRegistry.getDomains();
		for (IDomain domain: domains.values()) {
    		for (IDomainClass domainClass: domain.classes()) {
				assert domainClass instanceof IDomainClass;
				NewDomainObjectJob job = new NewDomainObjectJob( 
						(IDomainClass)domainClass );
				JobAction action = new JobAction( job );
				// overwrite default name for action with class name
				action.setId(domainClass.getClass().getName() + ".new"); //$NON-NLS-1$
				action.setText( domainClass.getName() ); 
				action.setToolTipText( domainClass.getDescription() );
				register(action);
				newMenu.add( action );
    		}
			newMenu.add( new Separator() );
    	}	
	}
}
