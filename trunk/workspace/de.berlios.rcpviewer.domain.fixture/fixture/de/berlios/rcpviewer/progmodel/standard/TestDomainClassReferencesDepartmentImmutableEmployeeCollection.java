package de.berlios.rcpviewer.progmodel.standard;

import java.util.HashSet;
import java.util.Set;

import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

/**
 * Has a 1:m unidirectional relationship with Employee.
 * TODO: DomainMarker is workaround
 */
@InDomain
public class TestDomainClassReferencesDepartmentImmutableEmployeeCollection implements DomainMarker {
	Set<TestDomainClassReferencesEmployee> employees = new HashSet<TestDomainClassReferencesEmployee>();
	/**
	 * Should be picked up as an immutable 1:m reference to Employee.
	 * @return
	 */
	@Immutable
	@Associates(TestDomainClassReferencesEmployee.class)
	public Set<TestDomainClassReferencesEmployee> getEmployees() {
		return employees ;
	}
	void addToEmployees(final TestDomainClassReferencesEmployee employee) {
		employees.add(employee);
	}
	void removeFromEmployees(final TestDomainClassReferencesEmployee employee) {
		employees.remove(employee);
	}
}