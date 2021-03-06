package org.essentialplatform.runtime.shared.tests.session.fixture;

import java.util.HashSet;
import java.util.Set;

import org.essentialplatform.progmodel.essential.app.InDomain;
import org.essentialplatform.progmodel.essential.app.TypeOf;

@InDomain
public class Department {
	private String name;
	public String getName() {
		return name;
	}
	
	private int rank;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setName(String name) {
		this.name = name;
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
