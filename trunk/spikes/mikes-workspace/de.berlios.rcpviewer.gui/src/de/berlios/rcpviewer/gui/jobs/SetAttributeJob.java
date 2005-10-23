package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.gui.GuiPlugin;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Sets the passed attribute on the passed object to the passed value.
 * @author Mike
 *
 */
public class SetAttributeJob extends AbstractDomainObjectJob {

	private final IDomainObject.IAttribute _domainObjectAttribute;
	private final Object _value;
	

	public SetAttributeJob( IDomainObject object,
							EAttribute attribute,
							Object value ) {
		super( SetAttributeJob.class.getName(), object );
		if ( attribute == null ) throw new IllegalArgumentException();
		_value = value; // value can be null
		_domainObjectAttribute = object.getAttribute( attribute);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		_domainObjectAttribute.set( _value );
		ReportJob report = new ReportJob(
				GuiPlugin.getResourceString( "SetAttributeJob.Ok"), //$NON-NLS-1$
				ReportJob.INFO,
				_domainObjectAttribute.getEAttribute().getName() );
		report.schedule();
		return Status.OK_STATUS;
	}

}
