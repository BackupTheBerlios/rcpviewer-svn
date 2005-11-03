package org.essentialplatform.louis.factory.reference.collection;

import java.util.List;

import org.eclipse.ui.forms.IManagedForm;
import org.essentialplatform.louis.configure.IConfigurable;
import org.essentialplatform.louis.factory.DomainClassPart;
import org.essentialplatform.louis.factory.reference.IReferencePartDisplayListener;

import org.essentialplatform.runtime.session.IDomainObject;


/**
 * Handles dynamic behaviour for the collection gui.
 */
class CollectionMasterChildPart implements ICollectionChildPart, IConfigurable {
	
	private final CollectionTablePart _masterPart;
	private final DomainClassPart _childPart;
	
	CollectionMasterChildPart( CollectionTablePart masterPart,
			                   DomainClassPart childPart ) {
		assert masterPart != null;
		assert childPart != null;
		_masterPart = masterPart;
		_childPart = childPart;
	}
	
	/* ICollectionChildPart contract - either dummy or delegate to master part */
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	public void commit(boolean onSave) {
		assert false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	public void dispose() {
		_masterPart.dispose();
		_childPart.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm)
	 */
	public void initialize(IManagedForm form) {
		_masterPart.initialize( form );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	public boolean isDirty() {
		assert false;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	public boolean isStale() {
		assert false;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	public void refresh() {
		assert false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	public void setFocus() {
		_masterPart.setFocus();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	public boolean setFormInput(Object input) {
		assert false;
		return false;
	}
	
	/**
	 * @param display
	 */
	public void display(List<IDomainObject<?>> display) {
		_masterPart.display( display );
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.factory.reference.collection.ICollectionChildPart#addDisplayListener(org.essentialplatform.louis.factory.reference.IReferencePartDisplayListener)
	 */
	public void addDisplayListener(IReferencePartDisplayListener listener) {
		_masterPart.addDisplayListener( listener);
	}

	/* (non-Javadoc)
	 * @see org.essentialplatform.louis.factory.reference.collection.ICollectionChildPart#setParent(org.essentialplatform.louis.factory.reference.collection.CollectionPart)
	 */
	public void setParent(CollectionPart parent) {
		_masterPart.setParent( parent );
	}
	
	/* IConfigurableFormPart contract - delegate to child part as that is the 
	 * configurable part of the gui */
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.IConfigurableFormPart#addListener(org.essentialplatform.gui.factory.IConfigurableFormPart.IGuiConfiguratorListener)
	 */
	public boolean addConfigurableListener(IConfigurableListener listener) {
		return _childPart.addConfigurableListener( listener );
	}
	
	/* (non-Javadoc)
	 * @see org.essentialplatform.gui.factory.IConfigurableFormPart#removeListener(org.essentialplatform.gui.factory.IConfigurableFormPart.IGuiConfiguratorListener)
	 */
	public boolean removeConfigurableListener(IConfigurableListener listener) {
		return _childPart.removeConfigurableListener( listener );
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		_childPart.run();
	}
}