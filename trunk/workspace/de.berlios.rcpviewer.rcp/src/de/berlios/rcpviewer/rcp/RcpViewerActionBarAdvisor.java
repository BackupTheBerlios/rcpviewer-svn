package de.berlios.rcpviewer.rcp;

import java.util.Map;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
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

import de.berlios.rcpviewer.actions.NewDomainObjectAction;
import de.berlios.rcpviewer.domain.IDomain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IDomainRegistry;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;
import de.berlios.rcpviewer.domain.runtime.RuntimePlugin;

public class RcpViewerActionBarAdvisor extends ActionBarAdvisor {

    public RcpViewerActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	super.fillMenuBar(menuBar);

        menuBar.add(createFileMenu());
    }
    
    

    protected MenuManager createFileMenu() {
    	IWorkbenchWindow window= getActionBarConfigurer().getWindowConfigurer().getWindow();
        MenuManager menu = new MenuManager("File", IWorkbenchActionConstants.M_FILE);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));

		MenuManager newmenu = new MenuManager("New", ActionFactory.NEW.getId());
	    menu.add(newmenu);
	    createClassItems(newmenu);
	    newmenu.add(new GroupMarker("newStart"));
		newmenu.add(ContributionItemFactory.NEW_WIZARD_SHORTLIST.create(window));
        
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
    private void createClassItems(MenuManager pNewmenu) {
    	IWorkbenchWindow window= getActionBarConfigurer().getWindowConfigurer().getWindow();
    	RuntimePlugin runtimePlugin= RuntimePlugin.getDefault();
    	IDomainRegistry domainRegistry= runtimePlugin.getDomainRegistry();
    	Map<String, IDomain> domains= domainRegistry.getDomains();
    	for (IDomain domain: domains.values()) {
    		for (IDomainClass domainClass: domain.classes()) {
    			NewDomainObjectAction action= 
    				new NewDomainObjectAction(window, (IRuntimeDomainClass)domainClass);
    			pNewmenu.add(action);
    		}
    		
    		pNewmenu.add(new Separator());
    	}
		// TODO Auto-generated method stub
		
	}

	@Override
    protected void fillCoolBar(ICoolBarManager pCoolBar) {
    	super.fillCoolBar(pCoolBar);
    	

        // add toolbar for RCPViewer contributions
        ToolBarManager toolBarManager= new ToolBarManager(SWT.HORIZONTAL | SWT.FLAT);
        toolBarManager.add(new GroupMarker("additions"));
        
	     /**
	      * Add Save button to main tool bar.
	      */
	 	 IWorkbenchWindow window= getActionBarConfigurer().getWindowConfigurer().getWindow();
	   	 IAction save = ActionFactory.SAVE.create(window);
	     ActionContributionItem actionContributionItem= new ActionContributionItem(save);
	     getActionBarConfigurer().registerGlobalAction(save);
	     toolBarManager.prependToGroup("additions", actionContributionItem);
	     
	     ToolBarContributionItem tbItem = new ToolBarContributionItem(toolBarManager, "de.berlios.rcpviewer");
	     pCoolBar.add(tbItem);
  }
    
}
