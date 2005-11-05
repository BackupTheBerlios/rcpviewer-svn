package org.essentialplatform.progmodel.standard.reference;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.progmodel.standard.*;

import java.util.HashSet;
import java.util.Set;

/**
 * The @OppositeOf annotation is for these fixture classes applied to the
 * "child" end. 
 */
@InDomain
public class BiDir2Department {
	Set<BiDir2Employee> employees = new HashSet<BiDir2Employee>();
	/**
	 * Should be picked up as a 1:m bidirectional reference to BiDirEmployee.
	 * @return
	 */
	@TypeOf(BiDir2Employee.class)
	public Set<BiDir2Employee> getEmployees() {
		return employees ;
	}
	void addToEmployees(final BiDir2Employee employee) {
		employees.add(employee);
		employee.setDepartment(this);
	}
	void removeFromEmployees(final BiDir2Employee employee) {
		employees.remove(employee);
		employee.setDepartment(null);
	}
}