package de.berlios.rcpviewer.progmodel.standard.reference;
import de.berlios.rcpviewer.progmodel.standard.*;

/**
 * Has a 1:1 unidirectional immutable relationship with Name.
 * 
 */
@InDomain
public class EmployeeImmutableNameRef {
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