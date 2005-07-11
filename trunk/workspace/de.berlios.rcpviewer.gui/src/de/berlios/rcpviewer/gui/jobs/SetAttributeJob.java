package de.berlios.rcpviewer.gui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Sets the passed attribute on the passed object to the passed value.
 * @author Mike
 *
 */
public class SetAttributeJob extends AbstractDomainObjectJob {

	private final EAttribute _attribute;
	private final Object _value;
	

	public SetAttributeJob( IDomainObject object,
							EAttribute attribute,
							Object value ) {
		super( SetAttributeJob.class.getName(), object );
		if ( attribute == null ) throw new IllegalArgumentException();
		// value can be null
		_attribute = attribute;
		_value = value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		getDomainObject().set( _attribute, _value );
		return Status.OK_STATUS;
	}

}
