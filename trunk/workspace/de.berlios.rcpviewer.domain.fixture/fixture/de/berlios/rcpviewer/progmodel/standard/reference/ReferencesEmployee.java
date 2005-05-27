package de.berlios.rcpviewer.progmodel.standard.reference;
import de.berlios.rcpviewer.progmodel.standard.*;

/**
 * Has a m:1 bidirectional relationship with Employee.
 * 
 */
@InDomain
public class ReferencesEmployee {
	public ReferencesEmployee(String firstName, String lastName) {
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
	 * Should be picked up as representing a 1:1 reference to Department.
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