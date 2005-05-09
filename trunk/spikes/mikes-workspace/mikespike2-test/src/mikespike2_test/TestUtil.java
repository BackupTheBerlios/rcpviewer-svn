package mikespike2_test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.Assert;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * Lots of static methods to access workbench resources whilst testing.
 * <br>Each method includes its own test asserts for the assumptions made.
 * @author Mike
 *
 */
public class TestUtil extends Assert {

	/**
	 * Runs assert checks but could return <code>null</code>.
	 * @return
	 */
	public static IWorkbenchPage getActivePage() {
		assertNotNull( PlatformUI.getWorkbench() );
		assertNotNull( PlatformUI.getWorkbench().getActiveWorkbenchWindow() ); 
		// this test also checks that the current thread is the GUI thread.
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
	
	/**
	 * Will return <code>null</code> if view not open in active page.
	 * @param id
	 * @return
	 */
	public static IViewPart getViewPart( String id ) {
		assertNotNull( id );
		return getActivePage().findView( id );
	}
	
	/**
	 * Accesses the field of the passed name on the passed instance.  
	 * This will only find fields on the direct class of the object, not subclasses.
	 * @param instance
	 * @param name
	 * @return
	 */
	public static Object getField( Object instance, String name ) {
		assertNotNull( instance );
		assertNotNull( name );
		try {
			Field field = instance.getClass().getDeclaredField( name );		
			assertNotNull( field );
	        field.setAccessible( true );
	        return field.get( instance );
		}
		catch ( NoSuchFieldException nsfe ) {
			fail( nsfe.toString() );
		}
		catch ( IllegalAccessException iae ) {
			fail( iae.toString() );
		}
		return null;
		
	}
	
	/**
	 * Return all open editors
	 * @return
	 */
	public static IEditorPart[] getOpenEditors() {
		IEditorReference[] refs = getActivePage().getEditorReferences();
		IEditorPart[] parts = new IEditorPart[ refs.length ];
		for ( int i=0, num = refs.length; i < num ; i++ ) {
			parts[i] = refs[i].getEditor( false );
		}
		return parts;
	}
	
	/**
	 * Send a mouse double click event to the passed widget
	 */
	public static void sendDoubleClick( Widget widget ) {
		if (widget == null) throw new IllegalArgumentException();
		try {
			Method method = Widget.class.getDeclaredMethod(
					"sendEvent", new Class[] { Event.class } );
			method.setAccessible( true );
			Event event = new Event();
			event.type = SWT.MouseDoubleClick;
			event.display = widget.getDisplay();
			event.widget = widget;
			event.button = 1;
			method.invoke( widget, new Object[]{ event } );
		}
		catch ( NoSuchMethodException nsme ) {
			fail( nsme.toString() );
		}
		catch ( IllegalAccessException iae ) {
			fail( iae.toString() );
		}
		catch ( InvocationTargetException ite ) {
			fail( ite.toString() );
		}
	}
}
