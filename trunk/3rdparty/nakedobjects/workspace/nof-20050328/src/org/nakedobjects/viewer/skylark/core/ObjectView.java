 package org.nakedobjects.viewer.skylark.core;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.utility.Assert;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Click;
import org.nakedobjects.viewer.skylark.CollectionContent;
import org.nakedobjects.viewer.skylark.Color;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.ContentDrag;
import org.nakedobjects.viewer.skylark.Drag;
import org.nakedobjects.viewer.skylark.DragStart;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.ObjectContent;
import org.nakedobjects.viewer.skylark.Offset;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.Skylark;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewDrag;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.basic.DragContentIcon;

import org.apache.log4j.Logger;


public abstract class ObjectView extends AbstractView {
    private static final Logger LOG = Logger.getLogger(ObjectView.class);
    
    public ObjectView(Content content, ViewSpecification design, ViewAxis axis) {
        super(content, design, axis);
        if (!(content instanceof ObjectContent) && !(content instanceof CollectionContent)) {
            throw new IllegalArgumentException("Content must be ObjectContent or AssociateContent: " + content);
        }
        getViewManager().addToNotificationList(this);
    }

    public void dispose() {
        getViewManager().removeFromNotificationList(this);
        super.dispose();
    }

    public void dragIn(ContentDrag drag) {
        Consent perm = getContent().canDrop(drag.getSourceContent());
         if (perm.isAllowed()) {
            getViewManager().setStatus(perm.getReason());
            getState().setCanDrop();
        } else {
            getViewManager().setStatus(perm.getReason());
            getState().setCantDrop();
        }
        markDamaged();
    }

    public void dragOut(ContentDrag drag) {
        getState().clearObjectIdentified();
        markDamaged();
    }

    public Drag dragStart(DragStart drag) {
        View subview = subviewFor(drag.getLocation());
        if (subview != null) {
            drag.subtract(subview.getLocation());
            return subview.dragStart(drag);
        } else {
            if (drag.isCtrl()) {
                View dragOverlay = new DragViewOutline(getView());
                return new ViewDrag(this, new Offset(drag.getLocation()), dragOverlay);
            } else {
                View dragOverlay = new DragContentIcon(getContent());
                return new ContentDrag(this, drag.getLocation(), dragOverlay);
            }
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (AbstractView.DEBUG) {
            Size size = getSize();
            canvas.drawRectangle(0, 0, size.getWidth() - 1, size.getHeight() - 1, Color.DEBUG_VIEW_BOUNDS);
            canvas.drawLine(0, size.getHeight() / 2, size.getWidth() - 1, size.getHeight() / 2, Color.DEBUG_VIEW_BOUNDS);
            canvas.drawLine(0, getBaseline(), size.getWidth() - 1, getBaseline(), Color.DEBUG_BASELINE);
        }
    }

    /**
     * Called when a dragged object is dropped onto this view. The default
     * behaviour implemented here calls the action method on the target, passing
     * the source object in as the only parameter.
     */
    public void drop(ContentDrag drag) {
        Assert.assertTrue(drag.getSourceContent() instanceof ObjectContent);

        Naked result = getContent().drop(drag.getSourceContent());
        if (result != null) {
            View view = getWorkspace().createSubviewFor(result, false);
            Location location = new Location();
            location.move(10, 10);
            view.setLocation(location);
            getWorkspace().addView(view);
        }

        markDamaged();
    }


    public void firstClick(Click click) {
        View subview = subviewFor(click.getLocation());
        if (subview != null) {
            click.subtract(subview.getLocation());
            subview.firstClick(click);
        } else {
            if (click.button2()) {
                View view = getWorkspace().createSubviewFor(getContent().getNaked(), false);
                Size size = view.getRequiredSize();
                view.setSize(size);
                Location location = new Location(click.getLocationWithinViewer());
                location.subtract(size.getWidth() / 2, size.getHeight() / 2);
                view.setLocation(location);
                getViewManager().setOverlayView(view);
            }
        }
    }


    public void secondClick(Click click) {
        View subview = subviewFor(click.getLocation());
        if (subview != null) {
            click.subtract(subview.getLocation());
            subview.secondClick(click);
        } else {
            Location location = getAbsoluteLocation();
            location.translate(click.getLocation());
            View openWindow = Skylark.getViewFactory().createWindow(getContent());
            openWindow.setLocation(location);
            getWorkspace().addView(openWindow);
        }
    }
    
    public void updateView() {
        if(getContent().objectChanged()) {
            LOG.debug("object changed; view updated: " + getView());
            getView().refresh();
        }

        super.updateView();
    }
    
    public String toString() {
       return super.toString() + ": " + getContent(); 
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