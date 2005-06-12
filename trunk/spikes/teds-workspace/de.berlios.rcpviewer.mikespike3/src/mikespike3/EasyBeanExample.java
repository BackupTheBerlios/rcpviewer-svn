package mikespike3;

import java.net.URL;

import mikespike3.application.EasyBeanPlugin;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;

public class EasyBeanExample {
	
	public static final String PLUGIN_ID= "de.berlios.rcpviewer.mikespike3"; 
	public static final String DOMAIN_ID= PLUGIN_ID+"domain"; 
	
	public static String getSafeResourceString(String string) {
		Bundle bundle= Platform.getBundle(PLUGIN_ID);
		String string2= Platform.getResourceString(bundle, string);
		if (string2 == null)
			return string;
		return string2;
	}
	
	public static ImageDescriptor getImageDescriptor(String imageName) {

		EasyBeanPlugin plugin= EasyBeanPlugin.getInstance();
		imageName= getSafeResourceString(imageName);
		ImageDescriptor descriptor= plugin.getImageRegistry().getDescriptor(imageName);
		
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
