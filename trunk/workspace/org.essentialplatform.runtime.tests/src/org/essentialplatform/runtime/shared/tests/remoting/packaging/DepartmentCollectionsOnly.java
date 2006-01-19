package org.essentialplatform.runtime.shared.tests.remoting.packaging;

import java.util.HashSet;
import java.util.Set;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.runtime.client.session.ClientSessionManager;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.domain.IDomainObject;


@InDomain
public class DepartmentCollectionsOnly  {
	
//	public void addEmployee(final String firstName, final String surname) {
//		IClientSession session = ClientSessionManager.instance().getCurrentSession(Domain.domainFor(this.getClass()));
//		IDomainObject<Employee> employeeDO = session.create(Domain.lookupAny(Employee.class));
//		Employee employee = employeeDO.getPojo();
//		employee.setFirstName(firstName);
//		employee.setSurname(surname);
//		addToEmployees(employee);
//	}
	
	private Set<Employee> employees = new HashSet<Employee>();
	@TypeOf(Employee.class)
	public Set<Employee> getEmployees() {
		return employees;
	}
	public void addToEmployees(final Employee employee) {
		this.employees.add(employee);
	}
	public void removeFromEmployees(final Employee employee) {
		this.employees.remove(employee);
	}

}
