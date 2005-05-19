package de.berlios.rcpviewer.domain;

import java.util.HashSet;
import java.util.Set;

import de.berlios.rcpviewer.progmodel.standard.Associates;
import de.berlios.rcpviewer.progmodel.standard.InDomain;
import de.berlios.rcpviewer.progmodel.standard.impl.DomainMarker;


/**
 * TODO: implementing DomainMarker is a work-around; the annotation should be enough.
 */
@InDomain
public class Department implements DomainMarker {
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
	
	private Set<Employee> employees = new HashSet<Employee>();
	@Associates(Employee.class)
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
	 * Expect the programming model aspect to intercept.
	 *
	 */
	public void save() {
		
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
