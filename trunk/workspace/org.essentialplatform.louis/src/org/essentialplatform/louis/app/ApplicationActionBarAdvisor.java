package org.essentialplatform.louis.app;

import java.util.Map;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.jobs.JobAction;
import org.essentialplatform.louis.jobs.NewDomainObjectJob;
import org.essentialplatform.louis.jobs.ReportJob;
import org.essentialplatform.runtime.domain.IDomainRegistry;

import org.essentialplatform.core.domain.IDomain;
import org.essentialplatform.core.domain.IDomainClass;
import org.essentialplatform.runtime.RuntimePlugin;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	
	public static final String CONTRIBUTION_ITEM_ID = "org.essentialplatform"; //$NON-NLS-1$
	
	private static final String ADDITIONS_GROUP_ID = "additions"; //$NON-NLS-1$

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
		// only used with FILL_PROXY flag - not default implementation
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface.action.IMenuManager)
     */
    protected void fillMenuBar(IMenuManager menuBar) {
    	IWorkbenchWindow window
    		= getActionBarConfigurer().getWindowConfigurer().getWindow();
		 menuBar.add( createFileMenu( window ) );
		 menuBar.add( createWindowMenu( window ) );
    }
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface.action.ICoolBarManager)
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
        ToolBarManager toolBarManager
        	= new ToolBarManager(SWT.HORIZONTAL | SWT.FLAT);
        toolBarManager.add( new GroupMarker( ADDITIONS_GROUP_ID ));

	 	IWorkbenchWindow window
 			= getActionBarConfigurer().getWindowConfigurer().getWindow();
        
	    // refresh button - standard action but add images.
	   	IAction refresh = ActionFactory.REFRESH.create(window);
	   	refresh.setImageDescriptor( 
	   			AbstractUIPlugin.imageDescriptorFromPlugin(
	   					LouisPlugin.getDefault().getBundle().getSymbolicName(),
	   					"/icons/refresh.png" ) ) ; //$NON-NLS-1$
	   	refresh.setDisabledImageDescriptor( 
	   			AbstractUIPlugin.imageDescriptorFromPlugin(
	   					LouisPlugin.getDefault().getBundle().getSymbolicName(),
	   					"/icons/refresh_disabled.png" ) ) ; //$NON-NLS-1$
	    ActionContributionItem refreshContributionItem
	    	= new ActionContributionItem(refresh);
	    getActionBarConfigurer().registerGlobalAction(refresh);
	    toolBarManager.prependToGroup( ADDITIONS_GROUP_ID , refreshContributionItem);	    
	    
		// save button
	   	IAction save = ActionFactory.SAVE.create(window);
	    ActionContributionItem saveContributionItem
	    	= new ActionContributionItem(save);
	    getActionBarConfigurer().registerGlobalAction(save);
	    toolBarManager.prependToGroup( ADDITIONS_GROUP_ID , saveContributionItem);
	     
		// additional contribution items
	    ToolBarContributionItem tbItem
	    	= new ToolBarContributionItem( toolBarManager, CONTRIBUTION_ITEM_ID );
	    coolBar.add(tbItem);
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
    private MenuManager createFileMenu( IWorkbenchWindow window ) {
    	assert window != null;
        MenuManager menu = new MenuManager(
				LouisPlugin.getResourceString( "ApplicationActionBarAdvisor.File" ),  //$NON-NLS-1$
				IWorkbenchActionConstants.M_FILE);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));

		MenuManager newmenu = new MenuManager(
				LouisPlugin.getResourceString( "ApplicationActionBarAdvisor.New" ), //$NON-NLS-1$
				ActionFactory.NEW.getId());
	    menu.add(newmenu);
	    createClassItems( newmenu );
	    newmenu.add(new GroupMarker("newStart")); //$NON-NLS-1$
		newmenu.add( ContributionItemFactory.NEW_WIZARD_SHORTLIST.create(window));
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
        menu.add(new Separator());
        menu.add(ActionFactory.CLOSE.create(window));
        menu.add(ActionFactory.CLOSE_ALL.create(window));
        menu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
        menu.add(new Separator());
        menu.add(ActionFactory.SAVE.create(window));
        menu.add(ActionFactory.SAVE_ALL.create(window));
        
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menu.add(ActionFactory.QUIT.create(window));
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
        return menu;
    }
    
	/**
	 * As it says.
	 */
    private MenuManager createWindowMenu(IWorkbenchWindow window ) {
    	assert window != null;
        MenuManager menu = new MenuManager(
				LouisPlugin.getResourceString( "ApplicationActionBarAdvisor.Window" ),  //$NON-NLS-1$
				IWorkbenchActionConstants.M_WINDOW );
        MenuManager viewMenu = new MenuManager(
				LouisPlugin.getResourceString( "ApplicationActionBarAdvisor.ShowView" ),  //$NON-NLS-1$
				"ApplicationActionBarAdvisor.ShowView" ); //$NON-NLS-1$
        menu.add( viewMenu );
        viewMenu.add( ContributionItemFactory.VIEWS_SHORTLIST.create( window ) );
        return menu;
    }

    /**
     * Adds a 'new' menu item for every class in every domain
     */
    private void createClassItems(MenuManager newMenu) {
		assert newMenu != null;
		
    	IWorkbenchWindow window
			= getActionBarConfigurer().getWindowConfigurer().getWindow();
		
		// get all classes from domain(s)
    	RuntimePlugin runtimePlugin= RuntimePlugin.getDefault();
    	IDomainRegistry domainRegistry= runtimePlugin.getDomainRegistry();
    	Map<String, IDomain> domains= domainRegistry.getDomains();
    	int count = 0;
		for (IDomain domain: domains.values()) {
    		for (IDomainClass domainClass: domain.classes()) {
				assert domainClass instanceof IDomainClass;
				NewDomainObjectJob job = new NewDomainObjectJob( 
						(IDomainClass)domainClass );
				JobAction action = new JobAction( job );
				// overwrite default name for action with class name
				action.setText( domainClass.getName() ); 
				action.setToolTipText( domainClass.getDescription() ); 
				newMenu.add( action );
    		}
			newMenu.add( new Separator() );
    	}	
	}
}
