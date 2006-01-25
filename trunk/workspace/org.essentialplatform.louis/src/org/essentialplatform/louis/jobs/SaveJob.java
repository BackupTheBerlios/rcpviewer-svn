/**
 * 
 */
package org.essentialplatform.louis.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.forms.IManagedForm;
import org.essentialplatform.louis.LouisPlugin;

import org.essentialplatform.runtime.client.transaction.ITransactable;
import org.essentialplatform.runtime.client.transaction.ITransaction;
import org.essentialplatform.runtime.client.transaction.TransactionManager;
import org.essentialplatform.runtime.shared.domain.IDomainObject;

/**
 * 
 * @author Mike
 */
public class SaveJob extends AbstractDomainObjectJob {
	
	// argument checks
	private static IDomainObject<?> validateArg( IManagedForm form ) {
		if ( form.getInput() == null ) {
			throw new IllegalArgumentException();
		}
		if ( !(form.getInput() instanceof IDomainObject<?> ) ){
			throw new IllegalArgumentException();
		}
		return (IDomainObject<?>)form.getInput();
	}
	
	
	private final IManagedForm _form;

	/**
	 * @param name
	 * @param object
	 */
	public SaveJob( IManagedForm form ) {
		super(
			SaveJob.class.getName(),
			validateArg( form ) );
		_form = form;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		_form.commit(true);
		ITransactable transactable = (ITransactable)getDomainObject().getPojo();
		ITransaction transaction
			= TransactionManager.instance().getCurrentTransactionFor(
					transactable);
		if ( transaction != null ) {
			transaction.commit();
		}
		ReportJob report = new ReportJob(
				LouisPlugin.getResourceString( "SaveJob.OK"), //$NON-NLS-1$
				ReportJob.INFO,
				LouisPlugin.getText( getDomainObject() ) );
		return Status.OK_STATUS;
	}

}
