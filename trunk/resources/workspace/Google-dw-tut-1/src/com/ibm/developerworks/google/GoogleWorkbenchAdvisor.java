package com.ibm.developerworks.google;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class GoogleWorkbenchAdvisor extends WorkbenchAdvisor {

	public String getInitialWindowPerspectiveId() {
		return "com.ibm.developerworks.google.GooglePerspective";
	}

	public void preWindowOpen(IWorkbenchWindowConfigurer configurer) {
		super.preWindowOpen(configurer);
		configurer.setTitle("Google");
		configurer.setInitialSize(new Point(300, 300));
		configurer.setShowMenuBar(false);
		configurer.setShowStatusLine(false);
		configurer.setShowCoolBar(false);
	}
}