package org.essentialplatform.louis.configure;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.factory.IGuiFactory;
import org.essentialplatform.louis.util.ImageUtil;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;

/**
 * Static method to create configure buttons.
 * @author Mike
 *
 */
public class ConfigureWidgetFactory {
	
	/**
	 * Creates a button with <code>GridData</code> layout.
	 * @param parent
	 * @param toolkit
	 * @param config
	 * @return
	 */
	public static Button createButton(
			Composite parent,
			FormToolkit toolkit,
			final IConfigurable config ) {
		Button configGui = toolkit.createButton( parent, "", SWT.PUSH );  //$NON-NLS-1$
		GridData configData = new GridData();
		configData.widthHint = IGuiFactory.PART_ICON_SIZE.x;
		configData.heightHint = IGuiFactory.PART_ICON_SIZE.y;
		configGui.setLayoutData( configData );
		configGui.addSelectionListener( new DefaultSelectionAdapter(){
			public final void widgetSelected(SelectionEvent event) {
				config.run();
			}
		} );
		configGui.setImage(
				ImageUtil.resize(
						ImageUtil.getImage( 
								LouisPlugin.getDefault(), 
								"icons/configure_gui.png" ), //$NON-NLS-1$
								IGuiFactory.PART_ICON_SIZE ) );
		configGui.setToolTipText( LouisPlugin.getResourceString( "ConfigureGui" ) ); //$NON-NLS-1$
		return configGui;
	}
	
	/**
	 * Creates an <code>Action</code>
	 * @param config
	 * @return
	 */
	public static IAction createAction( final IConfigurable config ) {
		
		Action action = new Action(){
			@Override
			public void run() {
				config.run();
			}
		};
		action.setImageDescriptor(
				ImageUtil.getImageDescriptor( 
						LouisPlugin.getDefault(), 
						"icons/configure_gui.png" ) );//$NON-NLS-1$
		action.setToolTipText( LouisPlugin.getResourceString( "ConfigureGui" ) ); //$NON-NLS-1$
		return action;
	}	

	

	// prevent instantiation
	private ConfigureWidgetFactory() {
		super();
	}

}
