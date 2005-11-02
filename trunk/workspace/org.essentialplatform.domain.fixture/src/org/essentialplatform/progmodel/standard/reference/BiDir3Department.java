package org.essentialplatform.progmodel.standard.reference;
import org.essentialplatform.progmodel.standard.*;

import java.util.HashSet;
import java.util.Set;

/**
 * The @OppositeOf annotation is for these fixture classes applied to both ends. 
 */
@InDomain
public class BiDir3Department {
	Set<BiDir3Employee> employees = new HashSet<BiDir3Employee>();
	/**
	 * Should be picked up as a 1:m bidirectional reference to BiDirEmployee.
	 * @return
	 */
	@TypeOf(BiDir3Employee.class)
	@OppositeOf("department")
	public Set<BiDir3Employee> getEmployees() {
		return employees ;
	}
	void addToEmployees(final BiDir3Employee employee) {
		employees.add(employee);
		employee.setDepartment(this);
	}
	void removeFromEmployees(final BiDir3Employee employee) {
		employees.remove(employee);
		employee.setDepartment(null);
	}
}