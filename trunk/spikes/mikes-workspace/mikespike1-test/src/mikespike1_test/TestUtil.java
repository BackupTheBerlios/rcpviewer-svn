package mikespike1_test;

import java.lang.reflect.Field;

import junit.framework.Assert;

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
}
