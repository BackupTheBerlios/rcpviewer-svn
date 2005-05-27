package de.berlios.rcpviewer.progmodel.standard.reference;
import de.berlios.rcpviewer.progmodel.standard.*;
import java.util.HashSet;
import java.util.Set;


/**
 * Has a 1:m unidirectional relationship with Employee.
 */
@InDomain
public class DepartmentImmutableEmployeeCollection {
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
}