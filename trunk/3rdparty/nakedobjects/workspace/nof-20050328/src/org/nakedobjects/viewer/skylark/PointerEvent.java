package org.nakedobjects.viewer.skylark;

import java.awt.event.InputEvent;

/**
 * Details an event involving the pointer, such as a click or drag.
 */
public abstract class PointerEvent {
    protected int mods;

 	/**
 	 * Creates a new pointer event object.
 	 * @param mods  the button and key modifiers (@see java.awt.event.MouseEvent)
 	 */
    PointerEvent(int mods) {
        this.mods = mods;
    }

    /**
     * Returns true if the 'Alt' key is depressed
     */
    public boolean isAlt() {
        return (mods & InputEvent.ALT_MASK) > 0;
    }

    /**
     * Returns true if the left-hand button on the mouse is depressed
     */
    private boolean isButton1() {
        return (mods & InputEvent.BUTTON1_MASK) > 0;
    }

    /**
     * Returns true if the middle button on the mouse is depressed
     */
    private boolean isButton2() {
        return (mods & InputEvent.BUTTON2_MASK) > 0;
    }

    /**
     * Returns true if the right-hand button on the mouse is depressed
     */
    private boolean isButton3() {
        return (mods & InputEvent.BUTTON3_MASK) > 0;
    }

    /**
     * Returns true if the control key is depressed
     */
    public boolean isCtrl() {
        return (mods & InputEvent.CTRL_MASK) > 0;
    }

    /**
     * Returns true if the 'Alt' key is depressed
     */
    public boolean isMeta() {
        return (mods & InputEvent.META_MASK) > 0;
    }

    /**
     * Returns true if the shift key is depressed
     */
    public boolean isShift() {
        return (mods & InputEvent.SHIFT_MASK) > 0;
    }

    public String toString() {
        String buttons = (isButton1() ? "^" : "-") + (isButton2() ? "^" : "-") + (isButton3() ? "^" : "-");
        String modifiers = (isShift() ? "S" : "-") + (isAlt() ? "A" : "-") + (isCtrl() ? "C" : "-");

        return "buttons=" + buttons + ",modifiers=" + modifiers;
    }
    
    public boolean button1() {
        return (isButton1() && !isShift()) || (isButton2() && isShift());
    }

    public boolean button2() {
        return (isButton2() && !isShift()) || (isButton1() && isShift());
    }
    
    public boolean button3() {
        return isButton3();
    }
/*
    public boolean button4() {
        return isButton3() && !isCtrl() && !isShift() && isAlt();
    }
    
    public boolean button5() {
        return isButton3() && isShift() && !isCtrl() && !isAlt();
    }

    public boolean button6() {
        return isButton3() && !isAlt() && !isShift() && isCtrl();
    }
    */
    
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
