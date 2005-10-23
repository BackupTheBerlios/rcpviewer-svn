package de.berlios.rcpviewer.session;

import java.util.EventObject;

import org.eclipse.emf.ecore.EAttribute;

import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject;
import de.berlios.rcpviewer.progmodel.extended.ExtendedDomainObject; 
import de.berlios.rcpviewer.progmodel.extended.IPrerequisites;
import de.berlios.rcpviewer.progmodel.extended.IExtendedDomainObject.IExtendedAttribute;


/**
 * Event object for events that impact an {@link IExtendedDomainObject}.
 * 
 * @author Dan Haywood
 */
public final class ExtendedDomainObjectAttributeEvent extends EventObject  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
 	 * Standard constructor for {@link EventObject}s.
	 * 
	 * @param source
	 */
	public ExtendedDomainObjectAttributeEvent(
			final ExtendedDomainObject.ExtendedAttribute source, // TODO: workaround ? 
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
