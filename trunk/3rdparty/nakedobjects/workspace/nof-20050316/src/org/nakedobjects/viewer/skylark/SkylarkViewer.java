package org.nakedobjects.viewer.skylark;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.ApplicationContext;
import org.nakedobjects.object.persistence.defaults.LocalObjectManager;
import org.nakedobjects.object.reflect.PojoAdapter;
import org.nakedobjects.viewer.ObjectViewingMechanismListener;
import org.nakedobjects.viewer.skylark.special.RootWorkspaceSpecification;

public class SkylarkViewer {
    private ViewUpdateNotifier updateNotifier;
    ViewerFrame frame;
    Viewer viewer;
    
    public SkylarkViewer() {

        frame = new ViewerFrame();
        

        viewer = new Viewer();
        viewer.setRenderingArea(frame);

        frame.setViewer(viewer);
        
        updateNotifier = new ViewUpdateNotifier();
        
        InteractionSpy spy = new InteractionSpy();

        ViewerAssistant viewerAssistant = new ViewerAssistant();
        viewerAssistant.setViewer(viewer);
        viewerAssistant.setDebugFrame(spy);
        viewerAssistant.setUpdateNotifier(updateNotifier);

        viewer.setUpdateNotifier(updateNotifier);
        viewer.setSpy(spy);

        setShutdownListener(new ObjectViewingMechanismListener() {
            public void viewerClosing() {
                System.out.println("EXITED");
                System.exit(0);
            }
        });
        
        viewer.start();
    }
    
    public void show() {        
        frame.setBounds(10, 10, 800, 600);
        viewer.sizeChange();
        frame.show();    
    }

    public void setApplication(ApplicationContext applicationContext) {
        NakedObject rootObject = PojoAdapter.createNOAdapter(applicationContext);
        RootWorkspaceSpecification spec = new RootWorkspaceSpecification();
        View view = spec.createView(new RootObject(rootObject), null);
        viewer.setRootView(view);
        
        frame.setTitle(applicationContext.name());
    }

    public void setObjectManager(LocalObjectManager objectManager) {
        objectManager.setNotifier(updateNotifier);
    }

    public void setShutdownListener(ObjectViewingMechanismListener listener) {
        viewer.setListener(listener);
    }
}


/*
Naked Objects - a framework that exposes behaviourally complete
business objects directly to the user.
Copyright (C) 2000 - 2004  Naked Objects Group Ltd

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