package de.berlios.rcpviewer.gui.jobs;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.gui.util.StatusUtil;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Used to remove a value from a multiple reference or dissociate if a single reference.
 * @author Mike
 *
 */
public class RemoveReferenceJob extends AbstractDomainObjectJob {

	private final EReference _ref;
	private final IDomainObject<?> _value;
	
	/**
	 * No arg can be <code>null.</code>
	 * @param object
	 * @param ref
	 * @param value
	 */
	public RemoveReferenceJob( 
			IDomainObject<?> object,
			EReference ref,
			IDomainObject<?> value ) {
		super( RemoveReferenceJob.class.getName(), object );
		if ( value == null ) throw new IllegalArgumentException();
		if ( ref == null ) throw new IllegalArgumentException();
		_value = value;
		_ref = ref;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		IStatus status;
		if ( _ref.isMany() ) {
			getDomainObject().getCollectionReference( _ref ).removeFromCollection( _value );
			status = Status.OK_STATUS;
		}
		else {
			Method dissociator
				= getDomainObject().getDomainClass().getDissociatorFor( _ref );
			assert dissociator != null;
			try {
				dissociator.invoke( 
						getDomainObject().getPojo(),
						new Object[]{ _value.getPojo() } );
				status = Status.OK_STATUS;		
			}
			catch (Exception ex ) {
				status = StatusUtil.createError(
						GuiPlugin.getDefault(),
						"RemoveReferenceJob.Error", //$NON-NLS-1$
						ex );
			}
		}
		ReportJob report;
		if ( Status.OK_STATUS == status ) {
			report = new ReportJob( 
					GuiPlugin.getResourceString( "RemoveReferenceJob.Ok"),  //$NON-NLS-1$
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
