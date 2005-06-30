package de.berlios.rcpviewer.gui.app;

import java.util.Map;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
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

import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainRegistry;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.jobs.JobAction;
import de.berlios.rcpviewer.gui.jobs.NewDomainObjectJob;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	
	public static final String CONTRIBUTION_ITEM_ID = "de.berlios.rcpviewer";
	
	private static final String ADDITIONS_GROUP_ID = "additions";

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
		 menuBar.add( createFileMenu() );
    }
	
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface.action.ICoolBarManager)
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
        ToolBarManager toolBarManager
        	= new ToolBarManager(SWT.HORIZONTAL | SWT.FLAT);
        toolBarManager.add( new GroupMarker( ADDITIONS_GROUP_ID ));

		// save button
	 	IWorkbenchWindow window
	 		= getActionBarConfigurer().getWindowConfigurer().getWindow();
	   	IAction save = ActionFactory.SAVE.create(window);
	    ActionContributionItem actionContributionItem
	    	= new ActionContributionItem(save);
	    getActionBarConfigurer().registerGlobalAction(save);
	    toolBarManager.prependToGroup( ADDITIONS_GROUP_ID , actionContributionItem);
	     
		// additional contribution items
	    ToolBarContributionItem tbItem
	    	= new ToolBarContributionItem( toolBarManager, CONTRIBUTION_ITEM_ID );
	    coolBar.add(tbItem);
	}

	/**
	 * As it says.
	 */
    private MenuManager createFileMenu() {
    	IWorkbenchWindow window
    		= getActionBarConfigurer().getWindowConfigurer().getWindow();
        MenuManager menu = new MenuManager(
				GuiPlugin.getResourceString( "ApplicationActionBarAdvisor.File" ), 
				IWorkbenchActionConstants.M_FILE);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));

		MenuManager newmenu = new MenuManager(
				GuiPlugin.getResourceString( "ApplicationActionBarAdvisor.New" ), 
				ActionFactory.NEW.getId());
	    menu.add(newmenu);
	    createClassItems( newmenu );
	    newmenu.add(new GroupMarker("newStart"));
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
				assert domain instanceof IRuntimeDomainClass;
				NewDomainObjectJob job = new NewDomainObjectJob( 
						(IRuntimeDomainClass)domainClass );
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
