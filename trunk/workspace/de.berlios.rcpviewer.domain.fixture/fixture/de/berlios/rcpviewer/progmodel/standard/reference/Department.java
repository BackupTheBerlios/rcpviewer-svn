package de.berlios.rcpviewer.progmodel.standard.reference;
import de.berlios.rcpviewer.progmodel.standard.*;

import java.util.HashSet;
import java.util.Set;

import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

/**
 * Has a 1:m bidirectional relationship with Employee.
 * 
 * TODO: DomainMarker is workaround
 */
@InDomain
public class Department implements DomainMarker {
	Set<ReferencesEmployee> employees = new HashSet<ReferencesEmployee>();
	/**
	 * Should be picked up as a 1:m reference to Employee.
	 * @return
	 */
	@Associates(ReferencesEmployee.class)
	public Set<ReferencesEmployee> getEmployees() {
		return employees ;
	}
	void addToEmployees(final ReferencesEmployee employee) {
		employees.add(employee);
		employee.setDepartment(this);
	}
	void removeFromEmployees(final ReferencesEmployee employee) {
		employees.remove(employee);
		employee.setDepartment(null);
	}
}