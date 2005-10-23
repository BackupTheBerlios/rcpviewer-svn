package de.berlios.rcpviewer.progmodel.standard.reference;
import de.berlios.rcpviewer.progmodel.standard.*;

@InDomain
public class Employee {
	public Employee(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	String firstName;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	String lastName;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	Department department;
	/**
	 * Should be picked up as representing a 1:1 reference to 
	 * Department.
	 * 
	 *  * <p>
	 * Although there is a reference 1:m from Department to Employee, they
	 * are not annotated to represent a bidirectional relationship. 
	 * 
	 * @return
	 */
	public Department getDepartment() {
		return department;
	}
	/**
	 * not public since not an operation.
	 * @param department
	 */
	void setDepartment(Department department) {
		department.addToEmployees(this);
	}
	/**
	 * presence to indicate an optional reference.
	 *
	 */
	public void clearDepartment() {
		department.removeFromEmployees(this);
	}
	public boolean isTerminated() {
		return false;
	}
}