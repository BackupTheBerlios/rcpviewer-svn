package de.berlios.rcpviewer.application;


import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
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
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;

import de.berlios.rcpviewer.RCPViewer;

public class RCPViewerWorkbenchAdvisor
extends WorkbenchAdvisor
{
	public String getInitialWindowPerspectiveId() {
		return RCPViewer.RCP_VIEWER_PERSPECTIVE_ID;
	}
	
	
    
    public void fillActionBars(IWorkbenchWindow window, IActionBarConfigurer configurer, int flags) {
        super.fillActionBars(window, configurer, flags);

        if ((flags & FILL_MENU_BAR) != 0) {
            fillMenuBar(window, configurer);
        }

        if ((flags & FILL_COOL_BAR) != 0) {
            fillCoolBar(window, configurer);
        }
    }

	private void fillCoolBar(IWorkbenchWindow window, IActionBarConfigurer configurer) {

        ICoolBarManager coolBarManager = configurer.getCoolBarManager();

        // add main toolbar
        ToolBarManager toolBarManager= new ToolBarManager(SWT.FLAT | SWT.RIGHT) {
				protected void itemAdded(IContributionItem item) {
					// force text to be shown
					if (item instanceof ActionContributionItem)
						((ActionContributionItem)item).setMode(ActionContributionItem.MODE_FORCE_TEXT);
					
					super.itemAdded(item);
				}
        	};
        	toolBarManager.add(new GroupMarker("additions"));
         ToolBarContributionItem tbItem = new ToolBarContributionItem(toolBarManager, "de.berlios.rcpviewer");
         coolBarManager.add(tbItem);
    }

	protected void fillMenuBar(IWorkbenchWindow window, IActionBarConfigurer configurer) {

        IMenuManager menuManager= configurer.getMenuManager();

        menuManager.add(createFileMenu(window));
    }

    protected MenuManager createFileMenu(IWorkbenchWindow window) {
        MenuManager menu = new MenuManager("File", //$NON-NLS-1$
            IWorkbenchActionConstants.M_FILE);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));

		MenuManager newmenu = new MenuManager("New", ActionFactory.NEW.getId());
	    menu.add(newmenu);

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
	
}
