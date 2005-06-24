package mikespike4_test.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.Assert;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

/**
 * Static methods to 'write' to the GUI during testing.
 * <br>This relies on reflection and hence any security manager restrictions 
 * might cause its methods to fail.
 * <br>Each method includes its own test asserts for the assumptions made.
 * @author Mike
 *
 */
public class GuiWriteUtil extends Assert {

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
