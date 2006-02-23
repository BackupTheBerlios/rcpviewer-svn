package org.essentialplatform.runtime.server.tests.session.hibernate;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

import org.essentialplatform.core.domain.Domain;
import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;
import org.essentialplatform.runtime.client.session.ClientSessionManager;
import org.essentialplatform.runtime.client.session.IClientSession;
import org.essentialplatform.runtime.shared.domain.IDomainObject;


@Entity
@InDomain
public class Department  {
	
	private Integer id;
	@Id(generate=GeneratorType.AUTO)
	@org.essentialplatform.progmodel.essential.app.Id(1)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
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
	@javax.persistence.OneToMany(mappedBy="department")
	@javax.persistence.OrderBy("surname")
	public Set<Employee> getEmployees() {
		return employees;
	}
	/**
	 * Required for Hibernate.
	 * @param employees
	 */
	private void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
	public void addToEmployees(final Employee employee) {
		this.employees.add(employee);
		employee.setDepartment(this);
	}
	public void removeFromEmployees(final Employee employee) {
		this.employees.remove(employee);
		employee.setDepartment(this);
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
