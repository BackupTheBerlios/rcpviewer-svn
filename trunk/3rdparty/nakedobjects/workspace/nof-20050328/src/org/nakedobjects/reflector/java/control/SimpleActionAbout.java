package org.nakedobjects.reflector.java.control;

import org.nakedobjects.application.control.ActionAbout;
import org.nakedobjects.application.control.Role;
import org.nakedobjects.application.control.State;
import org.nakedobjects.application.control.User;
import org.nakedobjects.object.security.Session;


/**
 * An About for contolling the action methods within a NakedObject.
 */
public class SimpleActionAbout extends AbstractAbout implements ActionAbout {
    private Object[] defaultValues;
    private String[] labels;

    public SimpleActionAbout(Session session, Object object, Object[] parameters) {
        super(session, object);

        int noParams = parameters.length;
        labels = new String[noParams];
        defaultValues = new Object[noParams];
    }

    public void changeNameIfUsable(String name) {
        if (canUse().isAllowed()) {
            setName(name);
        }
    }

    public Object[] getDefaultParameterValues() {
        return defaultValues;
    }

    public String[] getParameterLabels() {
        return labels;
    }

    public void invisible() {
        super.invisible();
    }

    public void invisibleToUser(User user) {
        super.invisibleToUser(user);
    }

    public void invisibleToUsers(User[] users) {
        super.invisibleToUsers(users);
    }

    public void setParameter(int index, Object defaultValue) {
        if (index < 0 || index >= defaultValues.length) {
            throw new IllegalArgumentException("No parameter index " + index);
        }
        defaultValues[index] = defaultValue;
    }

    public void setParameter(int index, String label) {
        if (index < 0 || index >= defaultValues.length) {
            throw new IllegalArgumentException("No parameter index " + index);
        }
        labels[index] = label;
    }

    public void setParameter(int index, String label, Object defaultValue) {
        if (index < 0 || index >= defaultValues.length) {
            throw new IllegalArgumentException("No parameter index " + index);
        }
        labels[index] = label;
        defaultValues[index] = defaultValue;
    }

    public void setParameters(Object[] defaultValues) {
        if (this.defaultValues.length != defaultValues.length) {
            throw new IllegalArgumentException("Expected " + this.defaultValues.length + " defaults but got "
                    + defaultValues.length);
        }
        this.defaultValues = defaultValues;
    }

    public void setParameters(String[] labels) {
        if (this.labels.length != labels.length) {
            throw new IllegalArgumentException("Expected " + this.labels.length + " defaults but got " + labels.length);
        }
        this.labels = labels;
    }

    public void unusable() {
        super.unusable("Cannot be invoked");
    }

    public void unusable(String reason) {
        super.unusable(reason);
    }

    public void unusableInState(State state) {
        super.unusableInState(state);
    }

    public void unusableInStates(State[] states) {
        super.unusableInStates(states);
    }

    public void unusableOnCondition(boolean conditionMet, String reasonNotMet) {
        super.unusableOnCondition(conditionMet, reasonNotMet);
    }

    public void usableOnlyInState(State state) {
        super.usableOnlyInState(state);
    }

    public void usableOnlyInStates(State[] states) {
        super.usableOnlyInStates(states);
    }

    public void visibleOnlyToRole(Role role) {
        super.visibleOnlyToRole(role);
    }

    public void visibleOnlyToRoles(Role[] roles) {
        super.visibleOnlyToRoles(roles);
    }

    public void visibleOnlyToUser(User user) {
        super.visibleOnlyToUser(user);
    }

    public void visibleOnlyToUsers(User[] users) {
        super.visibleOnlyToUsers(users);
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
