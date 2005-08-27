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
 * Used to add a value to a multiple reference or associate if a single reference.
 * @author Mike
 *
 */
public class AddReferenceJob extends AbstractDomainObjectJob {

	private final EReference _ref;
	private final IDomainObject<?> _value;
	
	/**
	 * No arg can be <code>null.</code>
	 * @param object
	 * @param ref
	 * @param value
	 */
	public AddReferenceJob( IDomainObject<?> object,
							EReference ref,
							IDomainObject<?> value ) {
		super( AddReferenceJob.class.getName(), object );
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
			getDomainObject().getCollectionReference( _ref ).addToCollection( _value );
			status =  Status.OK_STATUS;
		}
		else {
//			Method associator
//				= getDomainObject().getDomainClass().getAssociatorFor( _ref );
//			assert associator != null;
			try {
//				associator.invoke( 
//						getDomainObject().getPojo(),
//						new Object[]{ _value.getPojo() } );
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
