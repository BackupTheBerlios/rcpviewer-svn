package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.util.StatusUtil;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Used to add a value to a multiple reference or associate if a single reference.
 * @author Mike
 *
 */
public class AddReferenceJob extends AbstractDomainObjectJob {

	private final EReference _ref;
	private final IDomainObject<?> _value;
	private final Viewer _viewer;
	
	/**
	 * No arg can be <code>null</code>
	 * @param object
	 * @param ref
	 * @param value
	 */
	public AddReferenceJob( IDomainObject<?> object,
							EReference ref,
							IDomainObject<?> value ) {
		this( object, ref, value, null );
	}
	
	/**
	 * The viewer arg is the only one that can be <code>null</code>
	 * @param object
	 * @param ref
	 * @param value
	 */
	public AddReferenceJob( IDomainObject<?> object,
							EReference ref,
							IDomainObject<?> value,
							Viewer viewer) {
		super( AddReferenceJob.class.getName(), object );
		if ( value == null ) throw new IllegalArgumentException();
		if ( ref == null ) throw new IllegalArgumentException();
		// viewer can be null;
		_value = value;
		_ref = ref;
		_viewer = viewer;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		IStatus status;
		if ( _ref.isMany() ) {
			getDomainObject().getCollectionReference( _ref ).addToCollection( _value );
			status =  Status.OK_STATUS;
		}
		else {
			try {
				getDomainObject().getOneToOneReference(_ref).set( _value );
				status = Status.OK_STATUS;		
			}
			catch (Exception ex ) {
				status = StatusUtil.createError(
						GuiPlugin.getDefault(),
						"AddReferenceJob.Error", //$NON-NLS-1$
						ex );
			}
		}
		ReportJob report;
		if ( Status.OK_STATUS == status ) {
			if ( _viewer != null ) {
				_viewer.setSelection( new StructuredSelection( _value ) );
			}
			report = new ReportJob( 
					GuiPlugin.getResourceString( "AddReferenceJob.Ok"),  //$NON-NLS-1$
					ReportJob.INFO,
					_ref.getName() );
		}
		else {
			report = new ReportJob( status.getMessage(), ReportJob.ERROR );
		}
		report.schedule();
		return status;
	}

}
