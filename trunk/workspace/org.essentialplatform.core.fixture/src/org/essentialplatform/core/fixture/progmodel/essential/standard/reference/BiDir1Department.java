package org.essentialplatform.core.fixture.progmodel.essential.standard.reference;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.OppositeOf;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.progmodel.standard.*;

import java.util.HashSet;
import java.util.Set;

/**
 * The @OppositeOf annotation is for these fixture classes applied to the
 * "parewnt" end. 
 */
@InDomain
public class BiDir1Department {
	Set<BiDir1Employee> employees = new HashSet<BiDir1Employee>();
	/**
	 * Should be picked up as a 1:m bidirectional reference to BiDirEmployee.
	 * @return
	 */
	@OppositeOf("department")
	@TypeOf(BiDir1Employee.class)
	public Set<BiDir1Employee> getEmployees() {
		return employees ;
	}
	void addToEmployees(final BiDir1Employee employee) {
		employees.add(employee);
		employee.setDepartment(this);
	}
	void removeFromEmployees(final BiDir1Employee employee) {
		employees.remove(employee);
		employee.setDepartment(null);
	}
}