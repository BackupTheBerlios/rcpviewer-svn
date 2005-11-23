/**
 * 
 */
package org.essentialplatform.louis.log;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
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
                LogController.SHOW_CONSOLE_KEY,
                LouisPlugin.getResourceString( "LogPreferencePage.ShowConsole" ), //$NON-NLS-1$
                getFieldEditorParent() ) );

	}
	
	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return LouisPlugin.getDefault().getPreferenceStore();
	}



}
