package org.nakedobjects.viewer.skylark.special;

import org.nakedobjects.viewer.skylark.Bounds;
import org.nakedobjects.viewer.skylark.InternalDrag;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.Viewer;


public class ResizeDrag extends InternalDrag {
    public static final int BOTTOM = 2;
    public static final int BOTTOM_LEFT = 7;
    public static final int BOTTOM_RIGHT = 8;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int TOP = 1;
    public static final int TOP_LEFT = 5;
    public static final int TOP_RIGHT = 6;
    /**
     * the location of the corner opposite the pointer that will form the
     * resizing rectangle.
     */
    private final Location anchor;
    private final int direction;
    private final ViewResizeOutline overlay;
    private final View view;
    private final Size minimumSize; 
    private final Size maximumSize;

    public ResizeDrag(View view, Bounds resizeArea, int direction) {
        this(view, resizeArea, direction, null, null);
    }
        
    public ResizeDrag(View view, Bounds resizeArea, int direction, Size minimumSize, Size maximumSize) {
        this.view = view;
        this.direction = direction;
        this.anchor = resizeArea.getLocation(); 
        this.minimumSize = minimumSize;
        this.maximumSize = maximumSize;
        overlay = new ViewResizeOutline();
        overlay.setBounds(resizeArea);
    }

    protected void cancel(Viewer viewer) {
        view.dragCancel(this);
    }

    protected void drag(Viewer viewer, Location location, int mods) {

        switch (direction) {
        case TOP:
            extendUpward(location);
            break;

        case BOTTOM:
            extendDownward(location);
            break;

        case LEFT:
            extendLeft(location);
            break;

        case RIGHT:
            extendRight(location);
            break;

        case TOP_RIGHT:
            extendRight(location);
            extendUpward(location);
            break;

        case BOTTOM_RIGHT:
            extendRight(location);
            extendDownward(location);
            break;

        case TOP_LEFT:
            extendLeft(location);
            extendUpward(location);
            break;

        case BOTTOM_LEFT:
            extendLeft(location);
            extendDownward(location);
            break;

        default:
            break;
        }
    }

    protected void end(Viewer viewer) {
        view.dragTo(this);
    }

    /*
     * public ViewResizeOutline(View forView, int direction) { this(forView,
     * direction, forView.getAbsoluteLocation(), forView.getSize()); }
     * 
     * public ViewResizeOutline(View forView, int direction, Location location,
     * Size size) { super(forView.getContent(), null, null);
     * 
     * Logger.getLogger(getClass()).debug("drag outline for " + forView);
     * setLocation(location); setSize(size);
     * 
     * Logger.getLogger(getClass()).debug("drag outline initial size " +
     * getSize() + " " + forView.getSize());
     * 
     * origin = getBounds();
     * 
     * switch (direction) { case TOP: getViewManager().showResizeUpCursor();
     * break;
     * 
     * case BOTTOM: getViewManager().showResizeDownCursor(); break;
     * 
     * case LEFT: getViewManager().showResizeLeftCursor(); break;
     * 
     * case RIGHT: getViewManager().showResizeRightCursor(); break;
     * 
     * case TOP_LEFT: getViewManager().showResizeUpLeftCursor(); break;
     * 
     * case TOP_RIGHT: getViewManager().showResizeUpRightCursor(); break;
     * 
     * case BOTTOM_LEFT: getViewManager().showResizeDownLeftCursor(); break;
     * 
     * case BOTTOM_RIGHT: getViewManager().showResizeDownRightCursor(); break;
     * 
     * case CENTER: getViewManager().showMoveCursor(); break;
     * 
     * default : break; } }
     */

    private void extendDownward(Location location) {
        overlay.markDamaged();
        int height = location.getY() - anchor.getY();
        int width = overlay.getSize().getWidth();
        overlay.setSize(new Size(width, height));
        overlay.markDamaged();
    }

    private void extendLeft(Location location) {
        overlay.markDamaged();
        int height = overlay.getSize().getHeight();
        int width = anchor.getX() - location.getX();
        overlay.setSize(new Size(width, height));
        int x = anchor.getX() - width;
        int y = anchor.getY();
        overlay.setBounds(new Bounds(x, y, width, height));
        overlay.markDamaged();
    }

    private void extendRight(Location location) {
        overlay.markDamaged();
        int height = overlay.getSize().getHeight();
        int width = location.getX() - anchor.getX();
        if(maximumSize != null && width > maximumSize.getWidth()) {
            width = maximumSize.getWidth();
        }
        if(minimumSize != null && width < minimumSize.getWidth()) {
            width = minimumSize.getWidth();
        }
        overlay.setSize(new Size(width, height));
        overlay.markDamaged();
    }

    private void extendUpward(Location location) {
        overlay.markDamaged();
        int height = anchor.getY() - location.getY();
        int width = overlay.getSize().getWidth();
        overlay.setSize(new Size(width, height));
        int x = anchor.getX();
        int y = anchor.getY() - height;
        overlay.setBounds(new Bounds(x, y, width, height));
        overlay.markDamaged();
    }

    public int getDirection() {
        return direction;
    }

    public Location getLocation() {
        Size size = overlay.getSize();
        return new Location(size.getWidth(), size.getHeight());
    }

    public View getOverlay() {
        return overlay;
    }

    protected void start(Viewer viewer) {}

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