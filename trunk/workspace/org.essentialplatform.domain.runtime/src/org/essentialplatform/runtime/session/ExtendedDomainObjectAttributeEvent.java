package org.essentialplatform.runtime.session;

import java.util.EventObject;

import org.essentialplatform.progmodel.extended.IPrerequisites;


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
			final IDomainObject.IObjectAttribute source, // TODO: workaround ? 
			final IPrerequisites newPrerequisites) {
		super(source);
		this.newPrerequisites = newPrerequisites;
	}
	
	/**
	 * Type-safe accessor to the source of this event.
	 * 
	 * @return the attribute that raised this event.
	 */
	public IDomainObject.IObjectAttribute getAttribute() {
		return (IDomainObject.IObjectAttribute)getSource();
	}

	private final IPrerequisites newPrerequisites;
	public IPrerequisites getNewPrerequisites() {
		return newPrerequisites;
	}
	
}
