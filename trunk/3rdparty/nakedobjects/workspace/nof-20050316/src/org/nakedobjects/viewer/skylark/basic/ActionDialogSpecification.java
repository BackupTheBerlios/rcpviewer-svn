package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.object.Naked;
import org.nakedobjects.object.control.Consent;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.Skylark;
import org.nakedobjects.viewer.skylark.UserAction;
import org.nakedobjects.viewer.skylark.ValueContent;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.Workspace;
import org.nakedobjects.viewer.skylark.core.AbstractCompositeViewSpecification;
import org.nakedobjects.viewer.skylark.core.BackgroundTask;
import org.nakedobjects.viewer.skylark.core.BackgroundThread;
import org.nakedobjects.viewer.skylark.metal.ButtonAction;
import org.nakedobjects.viewer.skylark.metal.ButtonBorder;
import org.nakedobjects.viewer.skylark.metal.WindowBorder;
import org.nakedobjects.viewer.skylark.special.StackLayout;
import org.nakedobjects.viewer.skylark.special.SubviewSpec;
import org.nakedobjects.viewer.skylark.util.ViewFactory;


public class ActionDialogSpecification extends AbstractCompositeViewSpecification {

    private static class CloseAction extends ButtonAction {
        public CloseAction() {
            super("Cancel");
        }

        public void execute(Workspace workspace, View view, Location at) {
            workspace.removeView(view);
        }
    }

    private static class DialogFormSubviews implements SubviewSpec {

        public View createSubview(Content content, ViewAxis axis) {
            if (content instanceof ValueParameter) {
                ViewFactory factory = Skylark.getViewFactory();
                ViewSpecification specification = factory.getValueFieldSpecification((ValueContent) content);
                return specification.createView(content, axis);
            } else if (content instanceof ObjectParameter) {
                ObjectParameter parameterContent = (ObjectParameter) content;
                if (parameterContent.getNaked() == null) {
                    return (new EmptyField.Specification()).createView(content, axis);
                } else {
                    //return new SubviewIconSpecification().createView(content,
                    // axis);

                    ViewFactory factory = Skylark.getViewFactory();
                    ViewSpecification specification = factory.getIconizedSubViewSpecification(parameterContent);
                    return specification.createView(content, axis);
                }
            }

            return null;
        }

        public View decorateSubview(View view) {
            return view;
        }
    }

    private static class ExecuteAction extends ButtonAction {
        public ExecuteAction() {
            this("Apply");
        }

        public ExecuteAction(String name) {
            super(name);
        }

        public Consent disabled(View view) {
            ActionContent actionContent = ((ActionContent) view.getContent());
            return actionContent.disabled();
        }

        public void execute(final Workspace workspace, final View view, final Location at) {
            BackgroundThread.run(view, new BackgroundTask() {
                protected void execute() {
                    ActionContent actionContent = ((ActionContent) view.getContent());
                    Naked result = actionContent.execute();
                    /*                 NakedObject actionObject = actionContent.getObject();
                     if (!(actionObject.getObject() instanceof NakedClass)) {
                        //actionObject.getContext().getObjectManager().objectChanged(actionObject);
                        actionObject.getContext().getObjectManager().saveChanges();
                    } */
                    if (result != null) {
                        move(at);
                        workspace.addOpenViewFor(result, at);
                    }
                }
            });
        }

        protected void move(Location at) {
            at.move(30, 60);
        }
    }

    private static class ExecuteAndCloseAction extends ExecuteAction {
        public ExecuteAndCloseAction() {
            super("OK");
        }

        public void execute(Workspace workspace, View view, Location at) {
            super.execute(workspace, view, at);
            workspace.removeView(view);
        }

        protected void move(Location at) {}
    }

    public ActionDialogSpecification() {
        builder = new StackLayout(new ActionFieldBuilder(new DialogFormSubviews()));
    }

    public boolean canDisplay(Content content) {
        return content instanceof ActionContent;
    }

    public View createView(Content content, ViewAxis axis) {
        UserAction[] actions = new UserAction[] { new ExecuteAndCloseAction(), new CloseAction(), new ExecuteAction(), };
        return new WindowBorder(new ButtonBorder(actions, super.createView(content, new LabelAxis())), false);
    }

    public String getName() {
        return "Action Dialog";
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