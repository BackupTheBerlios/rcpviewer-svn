package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.domain.EmfFacade;
import de.berlios.rcpviewer.domain.IDomainAnalyzer;
import de.berlios.rcpviewer.domain.IDomainClass;

/**
 * Builds standard domain model.
 * 
 * <p>
 * Specifically, these means:
 * <ul>
 * <li>class attributes
 * <li>class operatrions
 * <li>class references
 * </ul>
 * <p>
 * A limited set of semantics (broadly, those intrinsically supported by EMF) 
 * are identified.
 * 
 * @author Dan Haywood
 *
 */
public class StandardProgModelExtension implements IDomainAnalyzer {

	public void analyze(IDomainClass<?> domainClass) {
		domainClass.init();
	}
	
	private final EmfFacade emfFacade = new EmfFacade();
}