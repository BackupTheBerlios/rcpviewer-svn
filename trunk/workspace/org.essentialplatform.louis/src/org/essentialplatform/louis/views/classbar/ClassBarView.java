package org.essentialplatform.louis.views.classbar;

import java.util.Iterator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;
import org.essentialplatform.louis.LouisPlugin;
import org.essentialplatform.louis.jobs.JobAction;
import org.essentialplatform.louis.jobs.NewDomainObjectJob;
import org.essentialplatform.louis.jobs.ReportJob;
import org.essentialplatform.louis.jobs.SearchJob;
import org.essentialplatform.louis.util.DomainRegistryUtil;
import org.essentialplatform.louis.util.FontUtil;
import org.essentialplatform.louis.util.ImageUtil;
import org.essentialplatform.louis.widgets.DefaultSelectionAdapter;

import de.berlios.rcpviewer.domain.IDomainClass;
import de.berlios.rcpviewer.domain.IRuntimeDomainClass;

/**
 * Outlook-style bar for each class in Domain.
 * @author Mike
 */
public class ClassBarView extends ViewPart {

	public static final String ID = ClassBarView.class.getName();
	public static final String EMPTY_DOMAIN_MSG_KEY = "ClassBarView.EmptyDomain"; //$NON-NLS-1$
	
	private static final Point IMAGE_SIZE = new Point( 32, 32 );
	
	@Override
	public void createPartControl( final Composite parent) {
		if ( parent == null ) throw new IllegalArgumentException();

		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 0;
		parent.setLayout( layout );
		
		// get all classes from domain(s)
		Iterator<IDomainClass> it = DomainRegistryUtil.iterateAllClasses(
				DomainRegistryUtil.Filter.INSTANTIABLE );
		boolean empty = true;
		while ( it.hasNext() ) {
			doAddClass( (IRuntimeDomainClass)it.next(), parent  );
			empty = false;
    	}
		
		// error message on status line if necessary
		if ( empty ) {
			UIJob job = new ReportJob(
					LouisPlugin.getResourceString( EMPTY_DOMAIN_MSG_KEY ),
					ReportJob.ERROR );
			job.schedule();
		}
	}
	
	/**
	 * As it says
	 * @param clazz
	 */
	private void doAddClass( IRuntimeDomainClass<?> clazz, Composite parent ) {
		assert clazz != null;
		assert parent != null;
		
		Button button = new Button( parent, SWT.BORDER );
		button.setLayoutData( 
				new GridData( GridData.HORIZONTAL_ALIGN_CENTER ) );
		button.setImage( 
				ImageUtil.resize( 
						ImageUtil.getImage( clazz ), 
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
		
		final IAction open = new JobAction( new NewDomainObjectJob( clazz ) );
		
		// click - opens a new instance
		button.addSelectionListener( new DefaultSelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				open.run();
			}
		});
		
		// right click - menu offering different options
		MenuManager mgr = new MenuManager();
		mgr.add( open );
		mgr.add( new JobAction( new SearchJob( clazz ) ) );
		Menu menu = mgr.createContextMenu(  button );
		button.setMenu( menu );			
	}

	@Override
	public void setFocus() {
		// does nowt
	}

}
