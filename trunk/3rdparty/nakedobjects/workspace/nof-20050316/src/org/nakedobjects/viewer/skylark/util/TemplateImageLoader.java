package org.nakedobjects.viewer.skylark.util;

import org.nakedobjects.viewer.skylark.UiConfiguration;

import java.awt.Canvas;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;


class TemplateImageLoader {
    private static final String[] EXTENSIONS = { "gif", "jpg", "jpeg", "png" };
    private final static Logger LOG = Logger.getLogger(TemplateImageLoader.class);
    private boolean alsoLoadAsFiles;
    private final MediaTracker mt = new MediaTracker(new Canvas());
    /**
     * A keyed list of core images, one for each name, keyed by the image path.
     */
    private Hashtable loadedImages = new Hashtable();
    private Vector missingImages = new Vector();


    /**
     * Creates a PictureLoader and loads a fallback icon.
     */
    TemplateImageLoader() {
        alsoLoadAsFiles = UiConfiguration.getInstance().alsoLoadImageAsFiles();
    }

    /*
     * private Image createImage() { byte[] pixels = new byte[128 * 128]; for
     * (int i = 0; i < pixels.length; i++) { pixels[i] = (byte) (i % 128); }
     * 
     * byte[] r = new byte[] { 0, 127 }; byte[] g = new byte[] { 0, 127 };
     * byte[] b = new byte[] { 0, 127 }; IndexColorModel colorModel = new
     * IndexColorModel(1, 2, r, g, b);
     * 
     * Image image = Toolkit.getDefaultToolkit().createImage(new
     * MemoryImageSource(128, 128, colorModel, pixels, 0, 128));
     * 
     * return image; }
     */

    /**
     * Returns a picture template for the specifed picture (as specified by a
     * path to a file or resource). If the path has no extension (.gif, .png
     * etc) then all valid extensions are searched for.
     * 
     * This method attempts to load the image from the jar/zip file this class
     * was loaded from ie, your application, and then from the file system as a
     * file if can't be found as a resource. If neither method works the default
     * image is returned.
     * 
     * @return returns a PictureTemplate for the specified image file, or null
     *                 if none found.
     */
    TemplateImage getTemplateImage(final String path) {
        final int extensionAt = path.lastIndexOf('.');
        final String root = extensionAt == -1 ? path : path.substring(0, extensionAt);

        if (loadedImages.contains(root)) {
            return (TemplateImage) loadedImages.get(root);

        } else if(missingImages.contains(root)) {
            return null;
            
        } else {
            Image image = null;
            if (extensionAt >= 0) {
                image = load(path);
                return TemplateImage.create(image);
            } else {
                for (int i = 0; i < EXTENSIONS.length; i++) {
                    image = load(root + "." + EXTENSIONS[i]);
                    if (image != null) {
                        return TemplateImage.create(image);
                    }
                }
            }
            missingImages.addElement(root);
            return null;
        }
    }

    private Image load(final String path) {
        Image image = loadAsResource(path);
        if (image == null && alsoLoadAsFiles) {
            image = loadAsFile(path);
        }

        return image;
    }

    /**
     * Get an Image object from the specified file path on the file system.
     */
    private Image loadAsFile(final String path) {
        final File file = new File(path);

        if (!file.exists()) {
            //          LOG.debug("Could not find image file: " +
            // file.getAbsolutePath());

            return null;
        } else {
            LOG.debug("Attempting to load image file " + file.getAbsolutePath());

            Toolkit t = Toolkit.getDefaultToolkit();
            Image image = t.getImage(file.getAbsolutePath());

            if (image != null) {
                mt.addImage(image, 0);

                try {
                    mt.waitForAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mt.isErrorAny()) {
                    LOG.error("Found image file but failed to load it: " + file.getAbsolutePath());
                    mt.removeImage(image);
                    image = null;
                } else {
                    mt.removeImage(image);
                    LOG.info("Image loaded from file: " + file);
                }
            }

            return image;
        }
    }

    /**
     * Get an Image object from the jar/zip file that this class was loaded
     * from.
     */
    private Image loadAsResource(String path) {
        URL url = TemplateImageLoader.class.getResource("/" + path);
        if (url == null) {
            return null;
        }

        Image image = Toolkit.getDefaultToolkit().getImage(url);
        if (image != null) {
            mt.addImage(image, 0);
            try {
                mt.waitForAll();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mt.isErrorAny()) {
                LOG.error("Found image but failed to load it from resources: " + url + " " + mt.getErrorsAny()[0]);
                mt.removeImage(image);
                image = null;
            } else {
                mt.removeImage(image);
                LOG.info("Image loaded from resources: " + url);
            }
        }

        if (image == null || image.getWidth(null) == -1) {
            throw new RuntimeException(image.toString());
        }

        return image;
    }
}

/*
 * Naked Objects - a framework that exposes behaviourally complete business
 * objects directly to the user. Copyright (C) 2000 - 2005 Naked Objects Group
 * Ltd
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * The authors can be contacted via www.nakedobjects.org (the registered address
 * of Naked Objects Group is Kingsway House, 123 Goldworth Road, Woking GU21
 * 1NR, UK).
 */
