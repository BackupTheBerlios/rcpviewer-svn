package org.essentialplatform.core.fixture.progmodel.essential.standard.reference;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.progmodel.standard.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Has a 1:m relationship with Employee.
 * 
 * <p>
 * Although there is a back reference m:1 from Employee to Department, they
 * are not annotated to represent a bidirectional relationship. 
 * 
 */
@InDomain
public class Department {
	Set<Employee> employees = new HashSet<Employee>();
	/**
	 * Should be picked up as a 1:m reference to Employee.
	 * @return
	 */
	@TypeOf(Employee.class)
	public Set<Employee> getEmployees() {
		return employees ;
	}
	void addToEmployees(final Employee employee) {
		employees.add(employee);
		employee.setDepartment(this);
	}
	void removeFromEmployees(final Employee employee) {
		employees.remove(employee);
		employee.setDepartment(null);
	}
}