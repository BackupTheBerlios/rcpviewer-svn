package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.NakedObjects;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.reflect.Action;
import org.nakedobjects.object.reflect.PojoAdapter;
import org.nakedobjects.viewer.skylark.MenuOption;
import org.nakedobjects.viewer.skylark.MenuOptionSet;


public class ClassOption {
    public static void menuOptions(NakedObjectSpecification specificaton, MenuOptionSet menuOptionSet) {
        PojoAdapter nakedClass = (PojoAdapter) PojoAdapter.createAdapter(NakedObjects.getObjectManager().getNakedClass(specificaton));
        
        Action[] actions;
        actions = specificaton.getClassActions(Action.USER);
        for (int i = 0; i < actions.length; i++) {
            addOption(nakedClass, menuOptionSet, actions[i], MenuOptionSet.OBJECT);
        }
        
        actions = nakedClass.getSpecification().getObjectActions(Action.USER);
        for (int i = 0; i < actions.length; i++) {
            addOption(nakedClass, menuOptionSet, actions[i], MenuOptionSet.OBJECT);
        }
        
        actions = specificaton.getClassActions(Action.EXPLORATION);
        if (actions.length > 0) {
            for (int i = 0; i < actions.length; i++) {
                addOption(nakedClass, menuOptionSet, actions[i], MenuOptionSet.EXPLORATION);
            }
        }
        
        actions = nakedClass.getSpecification().getObjectActions(Action.EXPLORATION);
        if (actions.length > 0) {
            for (int i = 0; i < actions.length; i++) {
                addOption(nakedClass, menuOptionSet, actions[i], MenuOptionSet.EXPLORATION);
            }
        }
    }
    
    private static void addOption(PojoAdapter cls, MenuOptionSet menuOptionSet, Action action, int type) {
        MenuOption option;
        if (action.parameters().length == 0) {
            option = ImmediateObjectOption.createOption(action, cls);
        } else {
            option = DialogedObjectOption.createOption(action, cls);
        }
        if(option != null) {
            menuOptionSet.add(type, option);
        }
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

