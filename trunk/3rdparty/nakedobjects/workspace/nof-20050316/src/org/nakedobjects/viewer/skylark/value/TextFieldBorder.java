package org.nakedobjects.viewer.skylark.value;

import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Style;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.core.AbstractBorder;

public class TextFieldBorder extends AbstractBorder {
    
    public TextFieldBorder(View view) {
        super(view);
    }

    public void draw(Canvas canvas) {
        int height = getSize().getHeight() - 2;
        int width = getSize().getWidth();
        canvas.drawSolidRectangle(0, 1, width - 1, height - 2, Style.WHITE);
        canvas.drawRectangle(0, 1, width - 3, height - 2, Style.SECONDARY1);
        canvas.drawRectangle(1, 2, width - 1, height - 2, Style.WHITE);
        
        super.draw(canvas);
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