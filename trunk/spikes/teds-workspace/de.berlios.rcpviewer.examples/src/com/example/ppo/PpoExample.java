package com.example.ppo;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;

import com.example.ppo.internal.PpoPlugin;

public class PpoExample {
	
	public static final String PLUGIN_ID= "de.berlios.rcpviewer.examples"; 
	
	public static String getSafeResourceString(String string) {
		Bundle bundle= Platform.getBundle(PLUGIN_ID);
		String string2= Platform.getResourceString(bundle, string);
		if (string2 == null)
			return string;
		return string2;
	}
	
	public static ImageDescriptor getImageDescriptor(String imageName) {

		PpoPlugin ppoPlugin= PpoPlugin.getInstance();
		imageName= getSafeResourceString(imageName);
		ImageDescriptor descriptor= ppoPlugin.getImageRegistry().getDescriptor(imageName);
		
		if (descriptor == null) {
			try {
				Bundle bundle= Platform.getBundle(PLUGIN_ID);
				URL url = Platform.resolve(bundle.getEntry("/icons/" + imageName));
				return ImageDescriptor.createFromURL(url);
			}catch (Throwable t) {
			}
		}
		
		if (descriptor == null)
			descriptor= ImageDescriptor.getMissingImageDescriptor();
		
		return descriptor;
	}
}
