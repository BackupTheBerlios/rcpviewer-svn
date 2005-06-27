package de.berlios.rcpviewer.gui.util;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.gui.GuiPlugin;

/**
 * Static methods for handling image operations.
 * @author Mike
 */
public class ImageUtil {
	
	public static final Point STATUS_BAR_IMAGE_SIZE = new Point ( 16, 16 );
	
	/**
	 * Creates an image for the passed <code>IDomainClass</code>.
	 * <br>This image is added to the <code>GUIPlugin</code>'s repository
	 * for caching and resource handling.
	 * TODO - need image attribute on IDomainClass
	 * @param clazz
	 * @return
	 */
	public static final Image getImage( IDomainClass clazz ) {
		if ( clazz == null ) throw new IllegalArgumentException();
		
		Image image = GuiPlugin.getDefault().getImageRegistry().get( clazz.getName() );
		if ( image == null ) {
			ImageDescriptor desc = null; // TODO - how get image out
			if ( desc == null ) {
				desc = ImageDescriptor.getMissingImageDescriptor();
			}
			image = desc.createImage();
			GuiPlugin.getDefault().getImageRegistry().put( clazz.getName(), image );
		}
		assert image != null;
		return image;
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
		Image resized = GuiPlugin.getDefault().getImageRegistry().get( key );
		if ( resized == null ) {
			resized = new Image( 
					Display.getCurrent(), 
					orig.scaledTo( size.x, size.y ) ) ;
			GuiPlugin.getDefault().getImageRegistry().put( key, resized );
		}
		return resized;
	}
	
	

	// prevent instantiation
	private ImageUtil() {
		super();
	}

}