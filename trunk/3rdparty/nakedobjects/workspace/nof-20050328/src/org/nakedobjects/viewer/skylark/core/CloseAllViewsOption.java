package org.nakedobjects.viewer.skylark.core;

import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.MenuOption;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.Workspace;


public class CloseAllViewsOption extends MenuOption {
    public CloseAllViewsOption() {
        super("Close all others");
    }

    public void execute(Workspace workspace, View view, Location at) {
        View views[] = view.getWorkspace().getSubviews();

        for (int i = 0; i < views.length; i++) {
            View otherView = views[i];
            if (otherView.getSpecification().isOpen() && otherView != view) {
                otherView.dispose();
                //				workspace.removeView(otherView);
                //				otherView.markDamaged();
            }
        }
    }

    public String getDescription(View view) {
        return "Close all view except " + view.getSpecification().getName().toLowerCase();
    }

    public String toString() {
        return "CloseAllViewsOption";
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
