package de.berlios.rcpviewer.presentation;

import org.eclipse.jface.viewers.ILabelProvider;

public interface IRCPViewerLabelProvider extends ILabelProvider {
	
    public String getToolTipText(Object object);
	
}
