package org.nakedobjects.viewer.skylark.metal;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedCollection;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.control.Hint;
import org.nakedobjects.object.defaults.FastFinder;
import org.nakedobjects.object.reflect.PojoAdapter;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Click;
import org.nakedobjects.viewer.skylark.Color;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.MenuOptionSet;
import org.nakedobjects.viewer.skylark.ObjectContent;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.Style;
import org.nakedobjects.viewer.skylark.UiConfiguration;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.basic.ClassOption;
import org.nakedobjects.viewer.skylark.basic.ClassTitleText;
import org.nakedobjects.viewer.skylark.basic.IconGraphic;
import org.nakedobjects.viewer.skylark.basic.ObjectBorder;
import org.nakedobjects.viewer.skylark.core.ObjectView;


public class ClassIcon extends ObjectView {
    
    public static class Specification implements ViewSpecification {

		public boolean canDisplay(Content content) {
			return content.isObject() && content.getNaked() instanceof  NakedClass;
		}

        public View createView(Content content, ViewAxis axis) {
    		return new ObjectBorder(new ClassIcon(content, this, axis));
        }

		public String getName() {
			return "class icon";
		}

		public boolean isOpen() {
			return false;
		}

		public boolean isReplaceable() {
			return false;
		}

		public boolean isSubView() {
			return false;
		}
    }
    private IconGraphic iconUnselected;
    private IconGraphic iconSelected;
    private IconGraphic icon;
    private final ClassTitleText title;

    public ClassIcon(final Content content, ViewSpecification specification, ViewAxis axis) {
        super(content, specification, axis);
        
        int iconSize = UiConfiguration.getInstance().classIconSize(); 
            
        iconUnselected = new ClassIconGraphic(this, iconSize); 
        iconSelected = new ClassIconGraphic(this, iconSize); 
        icon = iconUnselected;
       
       title = new ClassTitleText(this, Style.CLASS);
    }

    public void exited() {
        icon = iconUnselected;
        markDamaged();
        super.exited();
    }
    
    public void entered() {
        icon = iconSelected;
        markDamaged();
        super.entered();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(DEBUG) {
            canvas.drawRectangle(getSize(), Color.DEBUG_BASELINE);
        }
       
        int x = 0;
        int y = icon.getBaseline();
        icon.draw(canvas, x, y);
 
		int w = title.getSize().getWidth();
		int x2 = (w > icon.getSize().getWidth()) ? x : getSize().getWidth() / 2 - w / 2;
		int y2 = icon.getSize().getHeight() + Style.CLASS.getAscent() + VPADDING;
		title.draw(canvas, x2, y2);
    }

    public int getBaseline() {
        return icon.getBaseline();
    }

    public Size getRequiredSize() {
        final Size iconSize = icon.getSize();
        final Size textSize = title.getSize();
        iconSize.extendHeight(VPADDING + textSize.getHeight()  + VPADDING);
        iconSize.ensureWidth(textSize.getWidth());
        return iconSize;
    }

    public boolean isOpen() {
        return false;
    }
    
    public void secondClick(Click click) {
        Hint classAbout = getNakedClass().forObjectType().getClassHint();
            if (classAbout == null || classAbout.canAccess().isAllowed()) {
          //  View view = null;
            Naked object = null;
            if (click.isCtrl()) {
                NakedCollection instances = getNakedClass().allInstances();
                object = instances;
//                view = Skylark.getViewFactory().createOpenRootView(instances);
                
            } else {
                if (getNakedClass() instanceof NakedClass) {

                    FastFinder finder = getNakedClass().explorationActionFind();
                    object = PojoAdapter.createAdapter(finder);
                    //view = Skylark.getViewFactory().createOpenRootView(PojoAdapter.createAdapter(finder));
                }
            }
            if(object != null) {
                Location location = getView().getAbsoluteLocation();
                location.subtract(getWorkspace().getAbsoluteLocation());
                location.add(getSize().getWidth() + 10, 0);
                getWorkspace().addOpenViewFor(object, location);
            }
            /*
            if (view != null) {
                Location location = getView().getAbsoluteLocation();
                location.subtract(getWorkspace().getAbsoluteLocation());
                location.add(getSize().getWidth() + 10, 0);
                view.setLocation(location);
                getWorkspace().addView(view);
            }
            */
        }
    }
    
    private NakedClass getNakedClass()
    {
        NakedObject object = ((ObjectContent) getContent()).getObject();
		return (NakedClass) object.getObject();
    }
    
    public void contentMenuOptions(MenuOptionSet options) {
        NakedObjectSpecification spec = getNakedClass().forObjectType();
        ClassOption.menuOptions(spec, options);
        options.setColor(Style.CONTENT_MENU);
    }

    public String toString() {
        return "MetalClassIcon" + getId();
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
