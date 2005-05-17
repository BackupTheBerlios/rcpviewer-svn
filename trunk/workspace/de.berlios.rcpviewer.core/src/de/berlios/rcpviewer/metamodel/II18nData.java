package de.berlios.rcpviewer.metamodel;

/**
 * Holds set of internalization information relating to a feature.
 * 
 * @author Dan Haywood
 */
public interface II18nData {
	
	/**
	 * Key to the internalitionalized name.
	 */
	String nameKey();

	/**
	 * Key to the internalitionalized description.
	 */
	String descriptionKey();

}
