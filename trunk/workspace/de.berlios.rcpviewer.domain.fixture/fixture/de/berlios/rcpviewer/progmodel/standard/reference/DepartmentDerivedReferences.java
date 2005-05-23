package de.berlios.rcpviewer.progmodel.standard.reference;
import de.berlios.rcpviewer.progmodel.standard.*;
import java.util.HashSet;
import java.util.Set;

import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;

/**
 * Has a 1:m unidirectional relationship with Employee, and a separate
 * 1:m derived unidirectional relationship with Employee.
 * 
 * TODO: DomainMarker is workaround
 */
@InDomain
public class DepartmentDerivedReferences implements DomainMarker {
	Set<ReferencesEmployee> employees = new HashSet<ReferencesEmployee>();
	/**
	 * Should be picked up as an immutable 1:m reference to Employee.
	 * @return
	 */
	@Immutable
	@Associates(ReferencesEmployee.class)
	public Set<ReferencesEmployee> getEmployees() {
		return employees ;
	}
	void addToEmployees(final ReferencesEmployee employee) {
		employees.add(employee);
	}
	void removeFromEmployees(final ReferencesEmployee employee) {
		employees.remove(employee);
	}

	/**
	 * Should be picked up as a derived 1:m reference to Employee.
	 * @return
	 */
	@Derived
	@Associates(ReferencesEmployee.class)
	public Set<ReferencesEmployee> getTerminatedEmployees() {
		Set<ReferencesEmployee> employeesNamed = new HashSet<ReferencesEmployee>();
		for(ReferencesEmployee e: employees) {
			if (e.isTerminated()) {
				employeesNamed.add(e);
			}
		}
		return employeesNamed;
	}
	
	/**
	 * Should be picked up as a derived 1:1 reference to Employee.
	 * @return
	 */
	@Derived
	public ReferencesEmployee getMostRecentJoiner() {
		throw new RuntimeException("not implemented");
	}

}