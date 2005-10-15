package org.essentialplatform.louis.factory.reference.collection;

import java.util.List;

import org.eclipse.ui.forms.IFormPart;
import org.essentialplatform.louis.configure.IConfigurable;
import org.essentialplatform.louis.factory.reference.IReferencePartDisplayListener;

import de.berlios.rcpviewer.session.IDomainObject;

/**
 * Indicates that the part's model functionality is delegated to the 
 * parent <code>CollectionPart</code>.
 * @author Mike
 *
 */
interface ICollectionChildPart extends IFormPart, IConfigurable {
	
	/**
	 * Sets reference to parent.
	 * @param parent
	 */
	void setParent( CollectionPart parent );

	/**
	 * Display the passed list of domain objects.
	 * @param display
	 */
	void display( List<IDomainObject<?>> display ) ;
	
	/**
	 * Listens in on selected elemnt of collection 
	 * @param listener
	 */
	void addDisplayListener( IReferencePartDisplayListener listener );
}
