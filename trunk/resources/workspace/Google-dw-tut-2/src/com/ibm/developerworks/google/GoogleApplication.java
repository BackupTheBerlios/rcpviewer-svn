package com.ibm.developerworks.google;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class GoogleApplication implements IPlatformRunnable
{
    public Object run(Object args)
    {
        WorkbenchAdvisor workbenchAdvisor = new GoogleWorkbenchAdvisor();
        Display display = PlatformUI.createDisplay();
        int returnCode = PlatformUI.createAndRunWorkbench(display,
                workbenchAdvisor);
        if (returnCode == PlatformUI.RETURN_RESTART)
        {
            return IPlatformRunnable.EXIT_RESTART;
        } else
        {
            return IPlatformRunnable.EXIT_OK;
        }
    }

}