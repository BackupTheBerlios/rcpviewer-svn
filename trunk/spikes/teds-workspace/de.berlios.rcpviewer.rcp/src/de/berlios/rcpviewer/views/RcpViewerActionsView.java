package de.berlios.rcpviewer.views;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

public class RcpViewerActionsView extends ViewPart {
	
	Composite _controlPart= null;
	
	public RcpViewerActionsView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		
		//TODO unfinished
		
		Display display= parent.getDisplay();
		_controlPart= new Composite(parent, SWT.NONE);
		_controlPart.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
	}

	@Override
	public void setFocus() {
		_controlPart.setFocus();
	}
}
