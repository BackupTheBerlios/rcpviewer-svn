package mikespike4_test.util;

import java.lang.reflect.Field;

import junit.framework.Assert;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * Static methods to 'read' the GUI during testing.
 * <br>This relies on reflection and hence any security manager restrictions 
 * might cause its methods to fail.
 * <br>Each method includes its own test asserts for the assumptions made.
 * @author Mike
 *
 */
public class GuiReadUtil extends Assert {

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
	 * Returns the error text for the status line or null if no text or no
	 * status line displayed.
	 * <br>A clunky implementation dependent on platform internals.
	 * @return
	 */
	public static String getStatusLineErrorText() {
		Shell shell = PlatformUI.getWorkbench()
		                        .getActiveWorkbenchWindow()
		                        .getShell();
		Control[] children = shell.getChildren();
		for ( int i=0; i < children.length ; i++ ) {
			// compiler does not know about package private class StatusLine
			if (  children[i].getClass().getSimpleName().equals( "StatusLine" ) ) {
				return (String)getField( children[i],  "fErrorText" );
			}
		}
		return null;
	}
}
