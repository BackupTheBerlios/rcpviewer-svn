package de.berlios.rcpviewer.progmodel.standard;

import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

/**
 * Has a 1:1 unidirectional immutable relationship with Name.
 * 
 * TODO: DomainMarker is workaround
 */
@InDomain
public class TestDomainClassReferencesEmployeeImmutableNameRef implements DomainMarker {
	public TestDomainClassReferencesEmployeeImmutableNameRef(String firstName, String lastName) {
		this.name = new TestDomainClassReferencesName(firstName, lastName);
	}
	TestDomainClassReferencesName name;
	/**
	 * Should be picked up as an immutable 1:1 reference
	 * @return
	 */
	@Immutable
	public TestDomainClassReferencesName getName() {
		return name;
	}
}