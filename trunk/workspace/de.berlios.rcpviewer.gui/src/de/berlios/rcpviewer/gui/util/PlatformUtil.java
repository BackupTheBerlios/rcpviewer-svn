/*
 * Copyright (c) Incremental Ltd. 2004, 2005
 */
package de.berlios.rcpviewer.gui.util;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;




/**
 * Static library functions
 */
public class PlatformUtil {
    
    
    /**
     * Tidy access to <code>getActivePage</code>
     * @param editor
     * @param save
     * @return
     */
    public static final IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench()
          .getActiveWorkbenchWindow()
          .getActivePage();
    }

  
}
