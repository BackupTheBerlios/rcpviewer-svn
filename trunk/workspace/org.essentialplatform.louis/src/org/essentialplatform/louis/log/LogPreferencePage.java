/**
 * 
 */
package org.essentialplatform.louis.log;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.essentialplatform.louis.LouisPlugin;

/**
 * @author Mike
 *
 */
public class LogPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	
	/**
	 * Constructor sets title.
	 */
	public LogPreferencePage() {
		super( LouisPlugin.getResourceString( "LogPreferencePage.Title" ), //$NON-NLS-1$
               FieldEditorPreferencePage.GRID );
	}
	
	/**
	 * Must implement interface else workbench cannot open it - does nowt
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		
        addField( new BooleanFieldEditor( 
                LogController.CONSOLE_APPENDER_KEY,
                LouisPlugin.getResourceString( "LogPreferencePage.ConsoleAppender" ), //$NON-NLS-1$
                getFieldEditorParent() ) );

        addField( new BooleanFieldEditor( 
                LogController.SOCKET_APPENDER_KEY,
                LouisPlugin.getResourceString( "LogPreferencePage.SocketAppender" ), //$NON-NLS-1$
                getFieldEditorParent() ) );

        addField( new StringFieldEditor(
                LogController.SOCKET_APPENDER_HOST_NAME_KEY,
                LouisPlugin.getResourceString( "LogPreferencePage.SocketAppenderHostName" ), //$NON-NLS-1$
                getFieldEditorParent() ) );

        addField( new IntegerFieldEditor(
                LogController.SOCKET_APPENDER_PORT_KEY,
                LouisPlugin.getResourceString( "LogPreferencePage.SocketAppenderPort" ), //$NON-NLS-1$
                getFieldEditorParent() ) );


	}
	
	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return LouisPlugin.getDefault().getPreferenceStore();
	}



}
