package de.berlios.rcpviewer.session;

import java.util.EventObject;

import org.eclipse.emf.ecore.EReference;

import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject;
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedReference;


/**
 * Event object for events that impact an {@link IDomainObject}.
 * 
 * @author Dan Haywood
 */
public final class ExtendedDomainObjectReferenceEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
 	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public ExtendedDomainObjectReferenceEvent(
			final IExtendedDomainObject.IExtendedReference source, 
			final IPrerequisites newPrerequisites) {
		super(source);
		this.newPrerequisites = newPrerequisites;
	}
	
	/**
	 * Type-safe accessor to the source of this event.
	 * 
	 * @return the extended attribute that raised this event.
	 */
	public IExtendedDomainObject.IExtendedAttribute getExtendedAttribute() {
		return (IExtendedDomainObject.IExtendedAttribute)getSource();
	}



	private final IPrerequisites newPrerequisites;
	public IPrerequisites getNewPrerequisites() {
		return newPrerequisites;
	}
	
}
