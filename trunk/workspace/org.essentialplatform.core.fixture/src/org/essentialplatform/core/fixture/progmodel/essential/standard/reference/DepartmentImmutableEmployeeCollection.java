package org.essentialplatform.core.fixture.progmodel.essential.standard.reference;
import java.util.HashSet;
import java.util.Set;

import org.essentialplatform.progmodel.essential.app.Immutable;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;


/**
 * Has a 1:m unidirectional relationship with Employee.
 */
@InDomain
public class DepartmentImmutableEmployeeCollection {
	Set<Employee> employees = new HashSet<Employee>();
	/**
	 * Should be picked up as an immutable 1:m reference to Employee.
	 * @return
	 */
	@Immutable
	@TypeOf(Employee.class)
	public Set<Employee> getEmployees() {
		return employees ;
	}
	void addToEmployees(final Employee employee) {
		employees.add(employee);
	}
	void removeFromEmployees(final Employee employee) {
		employees.remove(employee);
	}
}