package de.berlios.rcpviewer.gui.views.classbar;

import java.util.Collection;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;

import de.berlios.rcpviewer.domain.Domain;
import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.jobs.FindInstanceJob;
import de.berlios.rcpviewer.gui.jobs.JobAction;
import de.berlios.rcpviewer.gui.jobs.NewInstanceJob;
import de.berlios.rcpviewer.gui.util.FontUtil;
import de.berlios.rcpviewer.gui.util.ImageUtil;
import de.berlios.rcpviewer.gui.widgets.DefaultSelectionAdapter;

/**
 * Outlook-style bar for each class in Domain.
 * @author Mike
 */
public class ClassBarView extends ViewPart {

	public static final String ID = ClassBarView.class.getName();
	public static final String EMPTY_DOMAIN_MSG_KEY = "ClassBarView.EmptyDomain";
	
	private static final Point IMAGE_SIZE = new Point( 32, 32 );
	
	@Override
	public void createPartControl( final Composite parent) {
		if ( parent == null ) throw new IllegalArgumentException();

		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 0;
		parent.setLayout( layout );
		
		final Collection<IDomainClass<?>> classes = Domain.instance().classes();
		if ( classes.isEmpty() ) {
			// error message if no classes
			getViewSite().getActionBars().getStatusLineManager().setErrorMessage(
					ImageUtil.resize(
							Display.getCurrent().getSystemImage( SWT.ICON_ERROR ),
							ImageUtil.STATUS_BAR_IMAGE_SIZE ),
					GuiPlugin.getResourceString( EMPTY_DOMAIN_MSG_KEY ) );
		}
		else {
			for( IDomainClass<?> clazz : classes ) {
				
				Button button = new Button( parent, SWT.BORDER );
				button.setLayoutData( 
						new GridData( GridData.HORIZONTAL_ALIGN_CENTER ) );
				button.setImage( 
						ImageUtil.resize( 
								ImageUtil.getImage( clazz), 
								IMAGE_SIZE ) ) ;
				button.setToolTipText( clazz.getDescription() ); 

				Label label = new Label( parent, SWT.CENTER );
				label.setText( clazz.getName() );
				label.setFont( FontUtil.getLabelFont() );
				label.setLayoutData( new GridData( 
						GridData.HORIZONTAL_ALIGN_CENTER ) );
				label.setToolTipText( clazz.getDescription() ); 
				
				// vertical spacer
				new Label( parent, SWT.CENTER );
				
				final IAction open = new JobAction( new NewInstanceJob( clazz ) );
				
				// click - opens a new instance
				button.addSelectionListener( new DefaultSelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						open.run();
					}
				});
				
				// right click - menu offering different options
				MenuManager mgr = new MenuManager();
				mgr.add( open );
				mgr.add( new JobAction( new FindInstanceJob( clazz ) ) );
				Menu menu = mgr.createContextMenu(  button );
				button.setMenu( menu );
				
			}
		}
	}

	@Override
	public void setFocus() {
		// does nowt
	}

}