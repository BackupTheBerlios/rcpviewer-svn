package org.nakedobjects.viewer.skylark.special;

import org.nakedobjects.object.NakedObjectRuntimeException;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.ObjectContent;
import org.nakedobjects.viewer.skylark.OneToManyField;
import org.nakedobjects.viewer.skylark.Skylark;
import org.nakedobjects.viewer.skylark.ValueContent;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.core.AbstractCompositeViewSpecification;
import org.nakedobjects.viewer.skylark.util.ViewFactory;


public class TableRowSpecification extends AbstractCompositeViewSpecification {

    private static class Cells implements SubviewSpec {
        public View createSubview(Content content, ViewAxis axis) {
            ViewFactory factory = Skylark.getViewFactory();

            ViewSpecification cellSpec;

            if (content instanceof OneToManyField) {
                return null;
            } else if (content instanceof ValueContent) {
                cellSpec = factory.getValueFieldSpecification((ValueContent) content);
            } else if (content instanceof ObjectContent) {
                cellSpec = factory.getIconizedSubViewSpecification((ObjectContent) content);
            } else {
                throw new NakedObjectRuntimeException();
            }

            return cellSpec.createView(content, axis);
        }

        public View decorateSubview(View cell) {
            return cell;
        }
    }

    public TableRowSpecification() {
        builder = new TableRowLayout(new ObjectFieldBuilder(new Cells(), true));
    }

    public boolean canDisplay(Content content) {
        return content.isObject();
    }

    public View createView(Content content, ViewAxis axis) {
        View view = super.createView(content, axis);

        return new TableRowBorder(view);
    }

    public String getName() {
        return "Table Row";
    }

    public boolean isReplaceable() {
        return false;
    }

    public boolean isSubView() {
        return true;
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
