package org.essentialplatform.core.fixture.progmodel.essential.standard.reference;
import org.essentialplatform.progmodel.essential.app.Derived;
import org.essentialplatform.progmodel.essential.app.Immutable;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.progmodel.standard.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Has a 1:m unidirectional relationship with Employee, and a separate
 * 1:m derived unidirectional relationship with Employee.
 */
@InDomain
public class DepartmentDerivedReferences {
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

	/**
	 * Should be picked up as a derived 1:m reference to Employee.
	 * @return
	 */
	@Derived
	@TypeOf(Employee.class)
	public Set<Employee> getTerminatedEmployees() {
		Set<Employee> employeesNamed = new HashSet<Employee>();
		for(Employee e: employees) {
			if (e.isTerminated()) {
				addTo(employeesNamed, e);
			}
		}
		return employeesNamed;
	}
	
	/**
	 * workaround for the declare error that prevents adding to collections
	 * outside addTo methods :-)
	 * 
	 * @param <Q>
	 * @param col
	 * @param e
	 */
	private <Q> void addTo(Collection<Q> col, Q e) {
		col.add(e);
	}
	
	/**
	 * Should be picked up as a derived 1:1 reference to Employee.
	 * @return
	 */
	@Derived
	public Employee getMostRecentJoiner() {
		throw new RuntimeException("not implemented");
	}

}