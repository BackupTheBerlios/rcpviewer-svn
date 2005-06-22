package mikespike3.application;

import mikespike3.commands.ICreateAndRunWorkbenchCommand;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class EasyBeanCreateAndRunWorkbenchCommand implements 
ICreateAndRunWorkbenchCommand
{	
	Display _display;
	WorkbenchAdvisor _advisor;
	
	public EasyBeanCreateAndRunWorkbenchCommand(Display pDisplay, WorkbenchAdvisor pAdvisor) {
		_display= pDisplay;
		_advisor= pAdvisor;
	}
	
	public int run() throws CoreException
	{
		int returnCode = PlatformUI.createAndRunWorkbench(_display, _advisor);
		
		// starting in v3.0M8 it is necessary to dispose the display (the 
		// workbench used to dispose the display)
		if (!_display.isDisposed())
			_display.dispose();
		
		return returnCode;
	}
}
