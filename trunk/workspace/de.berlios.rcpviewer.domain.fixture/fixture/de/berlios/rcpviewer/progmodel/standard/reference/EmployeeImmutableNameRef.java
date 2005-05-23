package de.berlios.rcpviewer.progmodel.standard.reference;
import de.berlios.rcpviewer.progmodel.standard.*;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

/**
 * Has a 1:1 unidirectional immutable relationship with Name.
 * 
 * TODO: DomainMarker is workaround
 */
@InDomain
public class EmployeeImmutableNameRef implements DomainMarker {
	public EmployeeImmutableNameRef(String firstName, String lastName) {
		this.name = new ReferencesName(firstName, lastName);
	}
	ReferencesName name;
	/**
	 * Should be picked up as an immutable 1:1 reference
	 * @return
	 */
	@Immutable
	public ReferencesName getName() {
		return name;
	}
}