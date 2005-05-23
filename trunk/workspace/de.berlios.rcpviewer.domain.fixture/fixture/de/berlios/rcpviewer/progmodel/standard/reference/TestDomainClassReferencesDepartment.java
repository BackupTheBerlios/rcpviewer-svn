package de.berlios.rcpviewer.progmodel.standard.reference;

import java.util.HashSet;
import java.util.Set;

import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

/**
 * Has a 1:m bidirectional relationship with Employee.
 * 
 * TODO: DomainMarker is workaround
 */
@InDomain
public class TestDomainClassReferencesDepartment implements DomainMarker {
	Set<TestDomainClassReferencesEmployee> employees = new HashSet<TestDomainClassReferencesEmployee>();
	/**
	 * Should be picked up as a 1:m reference to Employee.
	 * @return
	 */
	@Associates(TestDomainClassReferencesEmployee.class)
	public Set<TestDomainClassReferencesEmployee> getEmployees() {
		return employees ;
	}
	void addToEmployees(final TestDomainClassReferencesEmployee employee) {
		employees.add(employee);
		employee.setDepartment(this);
	}
	void removeFromEmployees(final TestDomainClassReferencesEmployee employee) {
		employees.remove(employee);
		employee.setDepartment(null);
	}
}