package de.berlios.rcpviewer.views;

import java.util.Enumeration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.ViewPart;
import org.nakedobjects.object.InternalCollection;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.reflect.NakedObjectField;
import org.nakedobjects.object.security.ClientSession;

import de.berlios.rcpviewer.app.ExplorationPlugin;

public class ClassBar extends ViewPart {
	
	public static final String ID = ClassBar.class.getName();

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		assert parent != null;
		
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		parent.setLayout( new GridLayout() );
		
		NakedObject rootObj = ExplorationPlugin.getDefault().getRootObject();
		NakedObjectField[] fields
			= rootObj.getSpecification().getVisibleFields(
					rootObj, ClientSession.getSession() );
		for ( int i=0, num = fields.length ; i < num ; i++ ) {
			if (fields[i].getName().equals("classes") && fields[i].isCollection()) {
				Naked attribute = rootObj.getField(fields[i]);
                Enumeration elements = ((InternalCollection) attribute).elements();
                while (elements.hasMoreElements()) {
					Object focus = elements.nextElement();
					assert focus instanceof NakedObject;
					addButton( parent, (NakedObject)focus );
                }
			}

		}


	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// TODO Auto-generated method stub
	}
	
	private void addButton( Composite parent, NakedObject obj ) {
		assert parent != null;
		assert obj != null;
		
		assert obj.getObject() instanceof NakedClass;
		NakedClass cls = (NakedClass)obj.getObject();
		
		Label label = new Label( parent, SWT.CENTER );
		// this does not strip the package name off as it should as the 
		// underlying reflector is PrimitiveReflector rather than JavaReflector
		// due to classlodaing issues in installFixtures() within ExplorationPlugin
		label.setText( cls.getSingularName() );
		
		// dummy menu
		Menu menu = new Menu( label );
		for ( int i=0 ; i < 3 ; i++ ) {
			MenuItem item = new MenuItem( menu, SWT.PUSH ) ;
			item.setText( "menu " + i );
		}
		label.setMenu( menu );
	}

}
