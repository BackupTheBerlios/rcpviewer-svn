package org.essentialplatform.louis.util;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.progmodel.louis.core.domain.LouisDomainClass;
import org.essentialplatform.runtime.client.domain.bindings.RuntimeClientBinding;
import org.osgi.framework.Bundle;

import org.essentialplatform.core.domain.IDomainClass;

/**
 * Static methods for handling image operations.
 * @author Mike
 */
public class ImageUtil {
	
	/**
	 * Fetches / creates image from passed plugin-relative path from passed
	 * plugin.  Caches and disposes of the image using the plugin's image
	 * registry.
	 * @param plugin
	 * @param imagePath
	 * @return
	 */
	public static final Image getImage( 
			AbstractUIPlugin plugin, String imagePath ) {
		String key = plugin.getBundle().getSymbolicName() + "." + imagePath; //$NON-NLS-1$
		Image image = plugin.getImageRegistry().get( key );
		if ( image == null ) {
			ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(
					plugin.getBundle().getSymbolicName(),
					imagePath );
			if ( desc == null ) {
				desc = ImageDescriptor.getMissingImageDescriptor();
			}
			image = desc.createImage();
			plugin.getImageRegistry().put( key, image );
		}
		assert image != null;
		return image;
	}
	
	/**
	 * Fetches / creates image descriptor from passed plugin-relative path 
	 * from passed plugin.  
	 * @param plugin
	 * @param imagePath
	 * @return
	 */
	public static final ImageDescriptor getImageDescriptor( 
			AbstractUIPlugin plugin, String imagePath ) {
		String key = plugin.getBundle().getSymbolicName() + "." + imagePath; //$NON-NLS-1$
		ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(
				plugin.getBundle().getSymbolicName(),
				imagePath );
		if ( desc == null ) {
			desc = ImageDescriptor.getMissingImageDescriptor();
		}
		return desc;
	}
	
	/**
	 * Creates an image for the passed <code>IDomainClass.
	 * <br>The image is discovered using the following logic:
	 * <ol>
	 * <li>looks for image according to annotation 'ImageUrlAt'
	 * <li>if none found, looks for default image file in default location - that is a 
	 * .png file in an 'icons' subdirectory of the class's parent plugin
	 * <li>if none found, uses the Eclipse standard 'missing image'
	 * </ol>
	 * <br>This image is added to the <code>GUIPlugin</code>'s repository
	 * for caching and resource handling.
	 * @param clazz
	 * @return
	 */
	public static final Image getImage( IDomainClass clazz ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		
		RuntimeClientBinding.RuntimeClientClassBinding<?> binding = 
			(RuntimeClientBinding.RuntimeClientClassBinding<?>)clazz.getBinding();
		Class javaClass = binding.getJavaClass();
		
		Image image = LouisPlugin.getDefault().getImageRegistry().get( javaClass.getSimpleName() );
		if ( image == null ) {
			ImageDescriptor desc = null;
			
			// use annotation value if available
			// (this ain't been tested, y'know...)
			LouisDomainClass louisDC = clazz.getAdapter(LouisDomainClass.class);
			desc = louisDC.imageDescriptor();
			
			// look for default image - require parent bundle and relative path
			if (desc == null) {
				desc = getImageDescriptor(javaClass, "png"); //$NON-NLS-1$
			}
			
			// last resort - missing image
			if ( desc == null ) {
				desc = ImageDescriptor.getMissingImageDescriptor();
			}
			
			image = desc.createImage();
			LouisPlugin.getDefault().getImageRegistry().put( javaClass.getSimpleName(), image );
		}
		assert image != null;
		return image;
	}

	public static ImageDescriptor getImageDescriptor(Class javaClass, String format) {
		return getImageDescriptor(javaClass, 0, format);
	}
	/**
	 * <tt>icons/ClassName-xx.yyy</tt> where -xx is the file size, yyy is the format.
	 * 
	 * @param javaClass
	 * @param size if 0, then "-xx" is omitted.
	 * @param format
	 * @return
	 */
	public static ImageDescriptor getImageDescriptor(Class javaClass, int size, String format) {
		ImageDescriptor desc;
		Bundle parentBundle = LouisPlugin.getDefault().getPackageAdmin().getBundle(javaClass );
		StringBuffer buf = new StringBuffer();
		buf.append( "/icons/" ) //$NON-NLS-1$
	       .append( javaClass.getSimpleName() );
		if (size != 0) {
			buf.append("-")   //$NON-NLS-1$
			   .append(size);
		}
		buf.append(".").append( format ); //$NON-NLS-1$
		
		desc = AbstractUIPlugin.imageDescriptorFromPlugin(
					parentBundle.getSymbolicName(),
					buf.toString() );
		return desc;
	}
	
	/**
	 * Creates a copy of the image scaled to the passed size.
	 * <br>This image is added to the <code>GUIPlugin</code>'s repository
	 * for caching and resource handling.
	 * <br>If the passed image is the correct size simply returns it.
	 * <br>This method must be called in the UI thread else it will throw
	 * an <code>IllegalThreadStateException</code>
	 * @param image not null
	 * @param size not null
	 * @return
	 */
	public static final Image resize( Image image, Point size ) {
		if ( image == null ) throw new IllegalArgumentException();
		if ( size == null ) throw new IllegalArgumentException();
		if ( Display.getCurrent() == null ) throw new IllegalThreadStateException();
		
		// check if anything to do
		ImageData orig = image.getImageData();
		if ( orig.height == size.y && orig.width == size.x ) return image;
		
		// check if already resized and cached
		String key = image.toString() + size.toString();
		Image resized = LouisPlugin.getDefault().getImageRegistry().get( key );
		if ( resized == null ) {
			resized = new Image( 
					Display.getCurrent(), 
					orig.scaledTo( size.x, size.y ) ) ;
			LouisPlugin.getDefault().getImageRegistry().put( key, resized );
		}
		return resized;
	}

	
	// prevent instantiation
	private ImageUtil() {
		super();
	}

}
