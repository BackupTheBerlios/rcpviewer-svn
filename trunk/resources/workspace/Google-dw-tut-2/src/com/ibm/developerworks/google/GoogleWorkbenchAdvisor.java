package com.ibm.developerworks.google;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class GoogleWorkbenchAdvisor extends WorkbenchAdvisor
{

    public GoogleWorkbenchAdvisor()
    {

    }

    public String getInitialWindowPerspectiveId()
    {

        return GooglePerspective.ID;
    }

    public void preWindowOpen(IWorkbenchWindowConfigurer configurer)
    {
        super.preWindowOpen(configurer);
        configurer.setTitle("Google");
        configurer.setInitialSize(new Point(600, 500));
        configurer.setShowStatusLine(false);
        configurer.setShowCoolBar(false);
        configurer.setShowMenuBar(true);
        configurer.setShowPerspectiveBar(false);
    }

    public void fillActionBars(IWorkbenchWindow window,
            IActionBarConfigurer configurer, int flags)
    {
        IMenuManager menuBar = configurer.getMenuManager();
        
        MenuManager fileMenu = new MenuManager("File",
                IWorkbenchActionConstants.M_FILE);
        fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
        fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        fileMenu.add(ActionFactory.QUIT.create(window));
        fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
        
        menuBar.add(fileMenu);        
    }

    private MenuManager createFileMenu(IWorkbenchWindow window)
    {
        MenuManager menu = new MenuManager("File",
                IWorkbenchActionConstants.M_FILE);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menu.add(ActionFactory.QUIT.create(window));
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
        return menu;
    }
    
    private MenuManager createHelpMenu(IWorkbenchWindow window)
    {
        MenuManager menu = new MenuManager("Help",
                IWorkbenchActionConstants.M_HELP);
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
        menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
        return menu;
    }
}