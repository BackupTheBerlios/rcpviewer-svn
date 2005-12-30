package org.essentialplatform.server.tests.remoting.activemq;

import java.util.HashSet;
import java.util.Set;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.runtime.shared.domain.IDomainObject;
import org.essentialplatform.runtime.shared.session.IClientSession;
import org.essentialplatform.runtime.shared.session.ClientSessionManager;


@InDomain
public class Department  {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private int rank;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	
	public void addEmployee(final String firstName, final String surname) {
		IClientSession session = ClientSessionManager.instance().getCurrentSession(Domain.domainFor(this.getClass()));
		IDomainObject<Employee> employeeDO = session.create(Domain.lookupAny(Employee.class));
		Employee employee = employeeDO.getPojo();
		employee.setFirstName(firstName);
		employee.setSurname(surname);
		addToEmployees(employee);
	}
	
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

	
	/**
	 * public to allow value to be read during testing.
	 */
	public boolean movedOffice = false;
	/**
	 * A void operation to be tested.
	 */
	public void moveOffice() {
		movedOffice = true;
	}

	/**
	 * Used as the title.
	 */
	public String toString() {
		return name;
	}

}
