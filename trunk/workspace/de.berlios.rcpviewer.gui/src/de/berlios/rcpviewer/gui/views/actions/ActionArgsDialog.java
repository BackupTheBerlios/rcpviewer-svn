package de.berlios.rcpviewer.gui.views.actions;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.swt.widgets.Shell;

import de.berlios.rcpviewer.gui.widgets.AbstractFormDialog;
import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Displays / edits arguments for an action on a domain object. 
 * @author Mike
 *
 */
class ActionArgsDialog extends AbstractFormDialog {

	private final IDomainObject _object;
	private final EOperation _op;
	
	ActionArgsDialog( 
			Shell parent, IDomainObject obj, EOperation op ) {
		super(parent);
		if ( obj == null ) throw new IllegalArgumentException();
		if ( op == null ) throw new IllegalArgumentException();
		_object = obj;
		_op = op;
	}

}
