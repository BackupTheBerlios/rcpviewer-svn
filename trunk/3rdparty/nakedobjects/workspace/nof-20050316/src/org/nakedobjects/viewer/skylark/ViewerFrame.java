package org.nakedobjects.viewer.skylark;

import org.nakedobjects.container.configuration.ComponentException;
import org.nakedobjects.container.configuration.ConfigurationException;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.UpdateNotifier;
import org.nakedobjects.viewer.ObjectViewingMechanism;
import org.nakedobjects.viewer.ObjectViewingMechanismListener;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ViewerFrame extends Frame implements RenderingArea, ObjectViewingMechanism {
    private static final int HEIGHT = 700;
    private static final int WIDTH = 800;
    private static final int Y = 10;
    private static final int X = 50;
    private Viewer viewer;
 
    /**
     * Creates a bordered frame.
     * @see java.awt.Frame#Frame(String)
     */
    public ViewerFrame() {	    
 
        viewer = new Viewer();
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    quit();
                }
            });

        addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    ViewerFrame.this.viewer.sizeChange();
                }
            });

        setBackground(Style.APPLICATION_BACKGROUND.getAwtColor());

        /*
         * compensate for change in tab handling in Java 1.4
         */
        try {
            Class c = getClass();
            Method m = c.getMethod("setFocusTraversalKeysEnabled",                new Class[] { Boolean.TYPE });
            m.invoke(this, new Object[] { Boolean.FALSE });
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException ignore) {
            /*
             * Ignore no such method exception as this method is only available, and 
             * needed, in version 1.4 and later.
             */
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public UpdateNotifier getUpdateNotifier() {
        return viewer.getUpdateNotifier();
    }

    public void init(NakedObject object, ObjectViewingMechanismListener listener) throws ConfigurationException, ComponentException {
        viewer.init(this, object, listener);
    }

    /**
     * Calls <code>update()</code> to do double-buffered drawing of all views.
     *
     * @see #update(Graphics)
     * @see java.awt.Component#paint(Graphics)
     */
    public final void paint(Graphics g) {
        update(g);
    }

    public void quit() {
        viewer.close();
    }

    public void start() {
        setBounds(X, Y, WIDTH, HEIGHT);
        viewer.start();
        viewer.sizeChange();
    	show();
    }

    /**
     * Paints the double-buffered image.  Calls the <code>draw()</code> method
     * on each top-level view.
     *
     * @see java.awt.Component#update(Graphics)
     */
    public void update(Graphics g) {
        viewer.paint(g);
    }
    
    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }
    
    /**
	 * Expose as a .NET property
	 * @property
	 */
    public void set_Viewer(Viewer viewer) {
        this.viewer = viewer;
    }
    
    /**
	 * Expose as a .NET property
	 * @property
	 */
	public void set_Title(String title) {
		setTitle(title);
	}

}


/*
Naked Objects - a framework that exposes behaviourally complete
business objects directly to the user.
Copyright (C) 2000 - 2005  Naked Objects Group Ltd

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

The authors can be contacted via www.nakedobjects.org (the
registered address of Naked Objects Group is Kingsway House, 123 Goldworth
Road, Woking GU21 1NR, UK).
*/