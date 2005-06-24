package mikespike4.test;

import mikespike4.GuiPlugin;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;


/**
 * Static methods that decorate base Eclipse dialog functionality.
 * @author Mike
 *
 */
public class DialogUtil {
	
    /**
     * As <code>MessageDialog.openError()</code> but aware of test sessions.
     * <br>Also adds a default title if the passed title is null.
     * @link MessageDialog
     */
    public static void openError(Shell parent, String title, String message) {
		if ( title == null ) {
			title = GuiPlugin.getResourceString( "DialogUtil.ErrorTitle" );
		}
		if ( TestSession.isActive() ) {
	        MessageDialog dialog = new MessageDialog(
					parent, 
					title, 
					null, 
	                message, 
					MessageDialog.ERROR,
	                new String[] { IDialogConstants.OK_LABEL }, 
	                0);
			TestSession.recordEvent( dialog );
		}
		else {
			MessageDialog.openError( parent, title, message );
		}
    }

}
