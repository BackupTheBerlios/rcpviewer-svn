package de.berlios.rcpviewer.metamodel;

import java.util.EventObject;

/**
 * Event object for events that impact an {@link MetaModel}.
 * 
 * @author Dan Haywood
 */
public class MetaModelEvent extends EventObject {

	/**
	 * Package local visibility so that the only subclasses must be in this
	 * package.
	 * 
	 * @param source
	 */
	MetaModelEvent(final MetaModel source) {
		super(source);
	}
	
	/**
	 * Type-safe access to the source of this event.
	 * 
	 * @return the metamodel that raised the event.
	 */
	public MetaModel getMetaModel() {
		return (MetaModel)this.getSource();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
